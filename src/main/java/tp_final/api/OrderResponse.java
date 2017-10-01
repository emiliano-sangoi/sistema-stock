package tp_final.api;

import tp_final.model.Order;

public class OrderResponse {
	
	private String status;
	
	private Order[] data;

	public OrderResponse() {
		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Order[] getOrders() {
		return data;
	}

	public void setData(Order[] data) {
		this.data = data;
	}
	
	public void setData(Order data) {
		this.data = new Order[1];
		this.data[0] = data;
	}	
	

}
