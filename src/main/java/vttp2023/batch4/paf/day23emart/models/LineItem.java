package vttp2023.batch4.paf.day23emart.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class LineItem {

   private int id;
   private String item;
   private int quantity;

   public int getId() { return id; }
   public void setId(int id) { this.id = id; }

   public String getItem() { return item; }
   public void setItem(String item) { this.item = item; }

   public int getQuantity() { return quantity; }
   public void setQuantity(int quantity) { this.quantity = quantity; }

   @Override
   public String toString() {
      return "LineItem [id=" + id + ", item=" + item + ", quantity=" + quantity + "]";
   }

   public JsonObject toJSON() {
      return Json.createObjectBuilder()
         .add("item", item)
         .add("quantity", quantity)
         .build();
   }

   public static LineItem createJSON(JsonObject obj) {
      LineItem li = new LineItem();
      li.setItem(obj.getString("item"));
      li.setQuantity(obj.getJsonNumber("quantity").intValue());
      return li;
   }


}
