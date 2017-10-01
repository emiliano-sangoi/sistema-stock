package tp_final.api;

import tp_final.model.Order;

public class OrderRequest {
	
	private Order order;

	public OrderRequest(Order order) {
		this.order = order;
	}
	
	public OrderRequest() {
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	

}
