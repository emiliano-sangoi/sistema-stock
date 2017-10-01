package tp_final.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tp_final.model.LineaPedido;
import tp_final.model.Pedido;
import tp_final.util.LineaPedidoResponse;

/**
 * Esta clase re
 * @author emiliano
 *
 */
public class APIOrderResponseData {
	
	private String status;
	private String date;
	private String owner;
	private String _id;
	private LineaPedidoResponse[] products;		
			
	public APIOrderResponseData() {
		this.products = new LineaPedidoResponse[0];			
	}
	
	public Pedido getPedido() {
		Pedido pedido = new Pedido();
		
		//id:
		pedido.setId(this._id);
		
		//estado (open o received):
		pedido.setEstado(this.status);
		
		//fecha de creacion:		
		pedido.setFechaCreacion(this.getDate());
		
		//detalle del producto:
		pedido.setDetalle(this.getDetalle());
							
		return pedido;
	}

	@Override
	public String toString() {			
		return "Producto con ID: " + this._id + " / Cantidad de productos: " + products.length;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * Este metodo convierte la fecha de creacion recibida (que viene como str) a un objeto
	 * del tipo Date que es mas comodo para su manipulacion y uso.
	 * 
	 * 
	 * @return
	 */
	public Date getDate() {
		
		try {
			/**
			 * Referencia:
			 * 		https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
			 */
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Date fechaAsDate = formatter.parse(this.date);				
			return fechaAsDate;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public ArrayList<LineaPedido> getDetalle() {
		int n = this.products.length;
		//LineaPedido[] detalle = new LineaPedido[n];
		ArrayList<LineaPedido>detalleList = new ArrayList<LineaPedido>();
		for(int i = 0; i < n ; i++) {
			//detalle[i] = this.products[i].getLineaPedido();
			detalleList.add(this.products[i].getLineaPedido());
		}
		return detalleList;
	}			


	


}	
	


