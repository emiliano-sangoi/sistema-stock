package tp_final.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Pedido {
	
	final static String ESTADO_OPEN = "open";
	final static String ESTADO_RECEIVED = "received";
	
	private String id;
	
	private Date fechaCreacion;
	
	private String estado;
	
	private LineaPedido[] detalle;
	
	
	
	public Pedido() {	
		this.fechaCreacion = new Date();
		this.estado = Pedido.ESTADO_OPEN;
	}

	public Pedido(String id, Date fechaCreacion, String estado) {
		this.id = id;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public boolean isOpen() {
		return this.estado == Pedido.ESTADO_OPEN;
	}
	
	public boolean isReceived() {
		return this.estado == Pedido.ESTADO_RECEIVED;
	}
	
	public String getEstado() {
		//return this.estado == Pedido.ESTADO_OPEN ? "Abierto" : "Recibido";
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LineaPedido[] getDetalle() {
		return detalle;
	}

	public void setDetalle(LineaPedido[] detalle) {
		this.detalle = detalle;
	}
	
	
	
	

}
