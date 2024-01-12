package vttp2023.batch4.paf.day23emart.exceptions;

public class PurchaseOrderException extends Exception {
    public PurchaseOrderException() {
        super();
    }

    public PurchaseOrderException(String msg) {
        super(msg);
        // System.out.println(msg);
    }
}
