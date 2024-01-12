package vttp2023.batch4.paf.day23emart.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.batch4.paf.day23emart.exceptions.PurchaseOrderException;
import vttp2023.batch4.paf.day23emart.models.LineItem;
import vttp2023.batch4.paf.day23emart.models.PurchaseOrder;
import vttp2023.batch4.paf.day23emart.repositories.LineItemRepository;
import vttp2023.batch4.paf.day23emart.repositories.PurchaseOrderRepository;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private LineItemRepository liRepo;

    @Transactional(rollbackFor = PurchaseOrderException.class)
    public boolean storeOrder(PurchaseOrder order) throws PurchaseOrderException {
        String poId = generateId();
        while (poRepo.haveId(poId)) {
            poId = generateId();
        }
        System.out.println(poId);
        order.setOrderId(poId);
        
        boolean orderStored = poRepo.storeOrder(order);
        // orderStored = false;
        if (!orderStored) throw new PurchaseOrderException("Purchase order not inserted into database: " + order.toString());
        int count = 0;
        for (LineItem li : order.getLineItems()) {
            boolean lineItemsStored = liRepo.storeLineItems(poId, li);
            // lineItemsStored = false;
            if (!lineItemsStored) {
                throw new PurchaseOrderException("Line item not inserted into database: " + li.toString() + " for order %s".formatted(poId));
            }
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
