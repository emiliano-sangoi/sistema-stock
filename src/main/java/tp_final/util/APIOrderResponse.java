package tp_final.util;


import tp_final.model.Pedido;
import tp_final.util.APIOrderResponseData;

/**
 * Representa el HTTP response obtenido al pegarle al endpoint de pedidos
 * 
 * @author emiliano
 *
 */
public class APIOrderResponse {
	
	private String status;
	
	private APIOrderResponseData[] data;
	
	public APIOrderResponse() {}			

	
	/**
	 * Devuelve todos los pedidos existentes obtenidas de la respuesta HTTP. 
	 * 
	 * @return Pedido[] pedidos existentes en el servidor
	 */
	public Pedido[] getPedidos() {
		
		int n = this.data.length;
		Pedido[] pedidos = new Pedido[n];
		
		for(int i = 0 ; i < n ; i++) {
			pedidos[i] = this.data[i].getPedido();
		}
									
		return pedidos;
	}
	
	
	@Override
	public String toString() {
		return "Cantidad de pedidos: " + this.data.length;	
	}


	public String getResponseStatus() {
		return status;
	}

}
