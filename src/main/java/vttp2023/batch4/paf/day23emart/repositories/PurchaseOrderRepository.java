package vttp2023.batch4.paf.day23emart.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.day23emart.models.PurchaseOrder;

@Repository
public class PurchaseOrderRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean storeOrder(String poId, PurchaseOrder order) {
        return template.update(Queries.SQL_INSERT_PURCHASE_ORDER, poId, order.getName(), order.getAddress()) > 0;
    }

    public boolean haveId(String poId) {
        SqlRowSet rs = template.queryForRowSet(Queries.SQL_SELECT_PURCHASE_ORDER_BY_ID, poId);
        if (rs.next()) {
            return true;
        }
        return false;
    }
}
