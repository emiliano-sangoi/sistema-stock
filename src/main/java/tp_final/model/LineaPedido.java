package tp_final.model;

public class LineaPedido {
	
	private String id;
	private Integer cant;		
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCant() {
		return cant;
	}
	public void setCant(Integer cant) {
		this.cant = cant;
	}
	
	public Producto getProducto() {
		return new Producto();
	}
	
	

}
