package tp_final.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LineaPedido {
	
	
	private String id;
	
	@NotNull(message="Este campo no puede quedar vacio")
	private Integer cant;	
	
	@NotNull(message="Este campo no puede quedar vacio")
	private String codigoProducto;			
	
	public LineaPedido() {		
	}
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
	public String getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}
	
	
	
	
	

}
