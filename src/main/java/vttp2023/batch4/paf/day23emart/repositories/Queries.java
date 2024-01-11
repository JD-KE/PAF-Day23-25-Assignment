package vttp2023.batch4.paf.day23emart.repositories;

public class Queries {
    public static final String SQL_INSERT_PURCHASE_ORDER = """
        insert into purchase_order(po_id, name, address)
            values
                (?,?,?);
    """;

    public static final String SQL_INSERT_LINE_ITEM = """
        insert into line_item(item, quantity, po_id)
            values
                (?,?,?);
    """;

    public static final String SQL_SELECT_PURCHASE_ORDER_BY_ID = """
        select * from purchase_order
            where po_id = ?;
     """;
    
}
