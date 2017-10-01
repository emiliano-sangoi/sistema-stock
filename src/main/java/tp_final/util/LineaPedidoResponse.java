package tp_final.util;

import tp_final.model.LineaPedido;

/**
 * 
 * Detalle del pedido. Cada elemento del arreglo "products" guarda el id del producto y la cantidad definida
 * en el pedido.
 *
 * Esta clase se pudo haber definido como privada en ResponseData pero por claridad se saco afuera.
 * 
 */
public class LineaPedidoResponse {
	
	private String _id;
	private int count;
	
	public LineaPedidoResponse(String id, int count) {
		this._id = id;
		this.count = count;
	}
	
	public LineaPedidoResponse() {				
		this.count = 0;
	}
	
	public LineaPedido getLineaPedido() {
		
		LineaPedido linea = new LineaPedido();
		linea.setId(this._id);
		linea.setCant(this.count);
		
		return linea;
		
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
