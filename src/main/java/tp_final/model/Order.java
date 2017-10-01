package tp_final.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

public class Order {	
	
	/**
	 * Id del pedido
	 */
	private String _id;
	
	/**
	 * Estado del pedido. Al crearse recibe el estado de "abierto"
	 */
	private String status;
	
	/**
	 * Fecha de creacion
	 */
	private String date;		
		
	@Valid
	private ArrayList<Item> products;
	
	
	/**
	 * Esto se usa para determinar que accion quiso realizar el usuario
	 * 
	 * 0 -> Nada
	 * 1 -> Agregar un item
	 * -1 -> Eliminar un item
	 * 2 -> Crear pedido
	 */
	private transient Integer codOp;
	
	/**
	 * Valores posibles para codOp
	 * 
	 * Estos codigos deben tener todos valores negativos. Los positivos son tomados como indice de los items en products
	 */
	public transient static final int ACTION_NUEVO_ITEM = -1;
	//public transient static final int ACTION_BORRAR_ITEM = -1;
	public transient static final int ACTION_DEFAULT = -15;
	public transient static final int ACTION_GUARDAR_PEDIDO = -487;	
	

	public Order() {
		
		this.products = new ArrayList<Item>();
		this.codOp = new Integer( ACTION_DEFAULT );
		
	}

	public String getId() {
		return _id;
	}


	public ArrayList<Item> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Item> products) {
		this.products = products;
	}
	
	public void addItem(Item item) {
		this.products.add(item);
	}
	
	public void removeItem(Item item) {
		this.products.remove(item);
	}
	
	public void removeItem(int idx) {				
		this.products.remove( this.products.get(idx) );
	}
	
	public Item getItem(int index) {
		return this.products.get(index);
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
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

	public Integer getCodOp() {
		return codOp;
	}

	public void setCodOp(Integer codOp) {
		this.codOp = codOp;
	}		
	
	
	
	
	
	

}
