package tp_final.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="productos")
public class Producto {
	
	@GeneratedValue
	@Id
	private Long id;
	
	@Column(name="codigo", nullable=false)
	@NotNull(message="Este campo no puede quedar vacio")
	@Size(min=1, max=128, message="El codigo del producto no puede quedar vacio y debe tener como maximo 128 caracteres")
	private String codigo;
	
	@Column(name="descripcion", nullable=false)
	@NotNull(message="Este campo no puede quedar vacio")
	@Size(min=5, max=512, message="La descripcion del producto debe tener entre 5 y 512 caracteres")
	private String descripcion;
	
	@Column(name="precio", nullable=false)
	@NotNull(message="Este campo no puede quedar vacio")
	private Float precio;
	
	@Column(name="cantidad", nullable=false)
	@NotNull(message="Este campo no puede quedar vacio")	
	private Integer cantidad;
	

	public Producto(String codigo, String descripcion, Float precio, Integer cantidad) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.cantidad = cantidad;
	}
	
	public Producto() {		
		this.codigo = null;
		this.descripcion = null;
		this.precio = null;
		this.cantidad = null;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.codigo;
	}
	
	public String getLabel() {
		return this.codigo + " - " + this.descripcion;
	}
	
	
	
	
	
	

}
