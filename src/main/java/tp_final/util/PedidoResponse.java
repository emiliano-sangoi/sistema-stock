package tp_final.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tp_final.model.LineaPedido;
import tp_final.model.Pedido;

public class PedidoResponse {
	
	private String status;
	
	private ResponseData[] data;
	
	public PedidoResponse() {}
	
	
	@Override
	public String toString() {
		return "Cantidad de pedidos: " + this.data.length;	
	}


	public String getResponseStatus() {
		return status;
	}

	
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


	/**
	 * 
	 * @author emiliano
	 *
	 */
	private class ResponseData{
		
		private String status;
		private String date;
		private String owner;
		private String _id;
		private LineaPedidoResponse[] products;		
				
		public ResponseData() {
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

		public LineaPedido[] getDetalle() {
			int n = this.products.length;
			LineaPedido[] detalle = new LineaPedido[n];
			for(int i = 0; i < n ; i++) {
				detalle[i] = this.products[i].getLineaPedido();
			}
			return detalle;
		}


		/**
		 * 
		 * Detalle del pedido. Cada elemento del arreglo "products" guarda el id del producto y la cantidad definida
		 * en el pedido.
		 *
		 */
		private class LineaPedidoResponse{
			
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

	}

}
