package vttp2023.batch4.paf.day23emart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import vttp2023.batch4.paf.day23emart.exceptions.PurchaseOrderException;
import vttp2023.batch4.paf.day23emart.models.LineItem;
import vttp2023.batch4.paf.day23emart.models.PurchaseOrder;
import vttp2023.batch4.paf.day23emart.services.PurchaseOrderService;

@Controller
@RequestMapping
public class PurchaseOrderController {

	@Autowired
	private PurchaseOrderService poSvc;

	@GetMapping(path = {"/", "/index.html"})
	public ModelAndView getIndex(HttpSession sess) {
		ModelAndView mav = new ModelAndView("index.html");
		if (sess.getAttribute("po")==null) {
			mav.addObject("po", new PurchaseOrder());
		} else {
			mav.addObject("po", sess.getAttribute("po"));
			System.out.println("get from session");
		}
		
		return mav;
	}

	@PostMapping(path = "/order")
	public ModelAndView getOrder(@ModelAttribute PurchaseOrder po, @RequestParam String item, @RequestParam int quantity, HttpSession sess) {

		ModelAndView mav = new ModelAndView("index.html");
		if (sess.getAttribute("po") != null) {
			PurchaseOrder storedOrder = (PurchaseOrder) sess.getAttribute("po");
			po.setLineItems(storedOrder.getLineItems());
		}

		LineItem itemToAdd = new LineItem();
		itemToAdd.setItem(item);
		itemToAdd.setQuantity(quantity);
		List<LineItem> items = po.getLineItems();
		// System.out.println(items);
		items.add(itemToAdd);
		po.setLineItems(items);
		// one line add below
		// po.getLineItems().add(itemToAdd);
		// System.out.println(po.getLineItems());

		sess.setAttribute("po", po);
		mav.addObject("po", sess.getAttribute("po"));

		return mav;
	}

	@PostMapping(path = "/checkout")
	public ModelAndView checkoutOrder(HttpSession sess) {
		ModelAndView mav = new ModelAndView("redirect:/index.html");

		PurchaseOrder order = (PurchaseOrder) sess.getAttribute("po");
		try {
			boolean orderStored = poSvc.storeOrder(order);
			sess.invalidate();
		} catch (PurchaseOrderException e) {
			mav.setViewName("index.html");
			mav.setStatus(HttpStatusCode.valueOf(500));
			mav.addObject("po", sess.getAttribute("po"));
		}
		

		return mav;
	}
}
