package tp_final.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Item {
	
	
	@NotNull(message="Este campo no puede quedar vacio")
	private String _id;
	
	@NotNull(message="Este campo no puede quedar vacio")
	@Min(value=1, message="Este campo debe ser mayor o igual a 1")
	private Integer count;
	

	public Item() {
		this.count = 1;
		
	}
	
	
	
	public Item(String _id, Integer count) {
		this._id = _id;
		this.count = count;
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}
	
	

	

	/*public boolean isBorrar() {
		return borrar;
	}

	public void setBorrar(boolean borrar) {
		this.borrar = borrar;
	}*/

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
	
	

}
