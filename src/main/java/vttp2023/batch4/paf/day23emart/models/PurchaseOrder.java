package vttp2023.batch4.paf.day23emart.models;

import java.util.Date;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import java.util.LinkedList;

public class PurchaseOrder {
   private String orderId;
   private String name;
   private String address;
   private Date createdOn;
   private Date lastUpdate;
	private List<LineItem> lineItems = new LinkedList<>();

   public String getOrderId() { return orderId; }
   public void setOrderId(String orderId) { this.orderId = orderId; }

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }

   public String getAddress() { return address; }

   public void setAddress(String address) { this.address = address; }

   public Date getCreatedOn() { return createdOn; }
   public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

   public Date getLastUpdate() { return lastUpdate; }
   public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

	public List<LineItem> getLineItems() { return lineItems; }
	public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }

   @Override
   public String toString() {
      return "PurchaseOrder [orderId=" + orderId + ", name=" + name 
            + ", address=" + address + ", createdOn="
            + createdOn + ", lastUpdate=" + lastUpdate + "]";
   }

   public JsonObject toJSON() {
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      List<JsonObject> lineItems = this.getLineItems()
         .stream()
         .map(t -> t.toJSON())
         .toList();

      for (JsonObject obj :lineItems) {
         arrayBuilder.add(obj);
      }

      return Json.createObjectBuilder()
         .add("customer_name", name)
         .add("ship_address", address)
         .add("line_items", arrayBuilder)
         .build();
   }

   public static PurchaseOrder createJSON (JsonObject obj) {
      PurchaseOrder po = new PurchaseOrder();
      po.setName(obj.getString("customer_name"));
      po.setAddress(obj.getString("ship_address"));
      List<LineItem> lineItems = obj.getJsonArray("line_items")
         .stream()
         .map(jsonValue -> LineItem.createJSON(jsonValue.asJsonObject()))
         .toList();
      po.setLineItems(lineItems);
      return po;
   }
}
