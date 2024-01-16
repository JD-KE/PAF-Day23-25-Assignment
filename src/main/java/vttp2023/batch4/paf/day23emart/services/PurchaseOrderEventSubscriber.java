package vttp2023.batch4.paf.day23emart.services;

import java.io.ByteArrayInputStream;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch4.paf.day23emart.exceptions.PurchaseOrderException;
import vttp2023.batch4.paf.day23emart.models.LineItem;
import vttp2023.batch4.paf.day23emart.models.PurchaseOrder;
import vttp2023.batch4.paf.day23emart.repositories.LineItemRepository;
import vttp2023.batch4.paf.day23emart.repositories.PurchaseOrderRepository;

@Service
public class PurchaseOrderEventSubscriber implements MessageListener{

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private LineItemRepository liRepo;

    // basically get message and do whatever you want with it
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] data = message.getBody();
        System.out.println(message.toString());
        JsonReader reader = Json.createReader(new ByteArrayInputStream(data));
        JsonObject obj = reader.readObject();
        PurchaseOrder po = PurchaseOrder.createJSON(obj);
        try {
            storeOrder(po);
        } catch (PurchaseOrderException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println("Order not stored");
        }
    }

    // rollbackFor is to target which exceptions will trigger the rollback when thrown by the method
    // also can adjust isolation lvl with attribute Isolation = ISOLATION.value
    @Transactional(rollbackFor = PurchaseOrderException.class)
    public boolean storeOrder(PurchaseOrder order) throws PurchaseOrderException {
        String poId = generateId();
        while (poRepo.haveId(poId)) {
            poId = generateId();
        }
        System.out.println(poId);
        order.setOrderId(poId);
        
        boolean orderStored = poRepo.storeOrder(order);
        // to test custom exception for inserting purchase order; fake failure by assigning false to boolean variable returned by storeOrder method
        // orderStored = false;

        // if purchase order fail to be inserted into database, throws the custom exception with custom message
        if (!orderStored) throw new PurchaseOrderException("Purchase order not inserted into database: " + order.toString());
        int count = 0;
        for (LineItem li : order.getLineItems()) {
            boolean lineItemsStored = liRepo.storeLineItems(poId, li);
            // to test custom exception for inserting line item; fake failure by assigning false to boolean variable returned by storeLineItems method
            // lineItemsStored = false;

            // if line item fail to be inserted into database, throws the custom exception with custom message
            if (!lineItemsStored) {
                throw new PurchaseOrderException("Line item not inserted into database: " + li.toString() + " for order %s".formatted(poId));
            }
            // random test for custom exception in case any method condition fail
            // if (count > 0) throw new PurchaseOrderException("Test error lol");
            // count++;
        }

        return true;

    }

    private String generateId() {
        // one line generate Id
        // String poId = UUID.randomUUID().toString.substring(0,8);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
}
