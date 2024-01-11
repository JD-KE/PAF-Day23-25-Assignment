package vttp2023.batch4.paf.day23emart.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.day23emart.models.LineItem;

@Repository
public class LineItemRepository {

    @Autowired
    JdbcTemplate template;

    public boolean storeLineItems(String poId, LineItem li) {
        return template.update(Queries.SQL_INSERT_LINE_ITEM, li.getItem(), li.getQuantity(), poId)>0;
    }

}
