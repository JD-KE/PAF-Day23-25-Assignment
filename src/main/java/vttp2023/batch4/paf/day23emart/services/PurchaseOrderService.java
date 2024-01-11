package vttp2023.batch4.paf.day23emart.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean storeOrder(PurchaseOrder order) {
        String poId = generateId();
        while (poRepo.haveId(poId)) {
            poId = generateId();
        }
        
        boolean orderStored = poRepo.storeOrder(poId, order);
        if (!orderStored) return false;
        for (LineItem li : order.getLineItems()) {
            boolean lineItemsStored = liRepo.storeLineItems(poId, li);
            if (!lineItemsStored) {
                return false;
            }
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
