package tp_final.helpers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import tp_final.api.OrderRequest;
import tp_final.api.OrderResponse;
import tp_final.model.Item;
import tp_final.model.Order;
import tp_final.model.Product;

public class APIHandler {
	
	private String endpointBase;
	private HttpEntity<String> entity;
	private HttpHeaders headers;
	private JsonDeserializer<OrderResponse> orderResponseDeserializer;
	private Order[] orders;
	private String lastError;	
    
    public APIHandler() {
    	
    	headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("auth", "emiliano.sangoi@gmail.com");
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        entity = new HttpEntity<String>("parameters", headers);
        this.lastError = "";
        
        //endpoints:
        endpointBase = "http://curso.kaitzen-solutions.com:3000/";                
        
        /**
         * Este deserializador personalizado se utiliza cuando se trae un solo pedido del backend.
         * En el response viene un solo pedido y Gson espera un array de pedidos, lo que produce un error.
         * 
         * https://futurestud.io/tutorials/gson-advanced-custom-deserialization-basics
         * 
         */
        this.orderResponseDeserializer = new JsonDeserializer<OrderResponse>() {

			@Override
			public OrderResponse deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext arg2)
					throws JsonParseException {								
				
				JsonObject jsonAPIResponse = jsonElement.getAsJsonObject();												
				JsonObject dataAsJson = jsonAPIResponse.get("data").getAsJsonObject();								
				
				Gson gson = new Gson();
				Order order = gson.fromJson(dataAsJson, Order.class);
				
				OrderResponse orderResponse = new OrderResponse();							
				orderResponse.setData(order);
				orderResponse.setStatus(jsonAPIResponse.get("status").getAsString());
	
				return orderResponse;
			}
		};
        
    	
    }
	
    /**
     * Verifica la existencia de un producto
     * 
     * @param codProducto
     * @return
     */
	public boolean productoExiste(String codProducto) {
		this.lastError = "";


		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> result = restTemplate.exchange(endpointBase + "product/" + codProducto,
					HttpMethod.GET, this.entity, String.class);

			if (result.getStatusCode().is2xxSuccessful()) {
				return true;
			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			// si se le pasa cualquier cosa como id la api deberia devolver 404, pero
			// devuelve 500
			this.lastError = "Ocurrio un error al intentar verificar la existencia del producto.";

		}

		return false;
	}
	
	
	

	/**
	 * Trae todos los pedidos existentes en el servidor
	 * 
	 * @return
	 */
	public boolean fetchOrders() {
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "order" , //endpoint
				HttpMethod.GET, 
				this.entity, // headers con la info de autorizacion
				String.class);
		
		System.out.println("=> fetchOrders(): " + result.getBody());
		
		//Para serializar deserializar el JSON:		
		Gson gson = new Gson();
		OrderResponse response = gson.fromJson(result.getBody(), OrderResponse.class);
		
		this.lastError = "";
		
		if(result.getStatusCode().is2xxSuccessful()) {			
			this.orders = response.getOrders();
			return true;
		}
		
		this.lastError = "Ocurrio un error al intentar guardar el pedido. Respuesta de la API: " + result.getStatusCodeValue();
		return false;
		
	}
	
	/**
	 * Busca el pedido con el id especificado
	 * 
	 * @param id
	 * @return
	 */
	public boolean fetchOrder(String id) {
		
		if(id.length() <= 0) {
			this.lastError = "El id debe poseer al menos un caracter";
			return false;
		}
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "order/" + id , //endpoint
				HttpMethod.GET, 
				this.entity, // headers con la info de autorizacion
				String.class
		);
		
		System.out.println("=> fetchOrder(..): " + result.getBody());
		
		//Conversion string -> objeto
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(OrderResponse.class, this.orderResponseDeserializer);
		Gson gson = gsonBuilder.create();
		OrderResponse response = gson.fromJson(result.getBody(), OrderResponse.class);
		
		if(result.getStatusCode().is2xxSuccessful()) {			
			this.orders = response.getOrders();
			return true;
		}
		
		this.lastError = "Ocurrio un error al intentar guardar el pedido. Respuesta de la API: " + result.getStatusCodeValue();
		return false;	

	}
		
	public Order[] getOrders() {
		return orders;
	}



	/**
	 * Crea un nuevo pedido en la API
	 * 
	 * @param order
	 * @return
	 */
	public boolean newOrder(Order order) {		
		
		OrderRequest newOrderRequest = new OrderRequest();
		newOrderRequest.setOrder(order);

		Gson gson = new Gson();
		String newOrderRequestJson = gson.toJson(newOrderRequest, OrderRequest.class);
				
		HttpEntity<String> httpEntity = new HttpEntity<String>(newOrderRequestJson, this.headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "order" , //endpoint
				HttpMethod.POST, 
				httpEntity, // headers con la info de autorizacion
				String.class);				
	
		System.out.println("=> newOrder(..): " + result.getBody());
				
		this.lastError = "";
		/**
		 * tb podria compararse si es igual a 200
		 */
		if (!result.getStatusCode().is2xxSuccessful()) {
			this.lastError = "Ocurrio un error al intentar guardar el pedido. Respuesta de la API: " + result.getStatusCodeValue();
			return false;
		}
		
		return true;
	}				



	/**
	 * Actualiza un pedido
	 * 
	 * @param order
	 * @return
	 */
	public boolean updateOrder(Order order) {
		
		OrderRequest newOrderRequest = new OrderRequest();
		newOrderRequest.setOrder(order);
		
		Gson gson = new Gson();
		String newOrderRequestJson = gson.toJson(newOrderRequest, OrderRequest.class);
				
		HttpEntity<String> httpEntity = new HttpEntity<String>(newOrderRequestJson, this.headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "order" , //endpoint
				HttpMethod.PUT, 
				httpEntity, // headers con la info de autorizacion
				String.class);
		
		System.out.println("=> updateOrder(..): " + result.getBody());
		
		this.lastError = "";
		/**
		 * tb podria compararse si es igual a 200
		 */
		if (! result.getStatusCode().is2xxSuccessful()) {
			this.lastError = "Ocurrio un error al intentar modificar el pedido. Respuesta de la API: " + result.getStatusCodeValue();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Borrar un pedido del backend
	 * 
	 * @param idOrder
	 * @return
	 */
	public boolean deleteOrder(String idOrder) {					
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<String>("", this.headers);
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "order/" + idOrder , //endpoint
				HttpMethod.DELETE, 
				httpEntity, // headers con la info de autorizacion
				String.class);			
		
		System.out.println("=> deleteOrder(..): " + result.getBody());
		
		this.lastError = "";

		if (! result.getStatusCode().is2xxSuccessful()) {
			this.lastError = "Ocurrio un error al intentar borrar el pedido. Codigo de respuesta de la API: " + result.getStatusCodeValue();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Marca un pedido como recibido
	 * 
	 * @param id
	 * @return
	 */
	public boolean markAsReceived(String id) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<String>("", this.headers);//sin esto tira error: 400 bad request
		ResponseEntity<String> result = restTemplate.exchange(
				endpointBase + "received_order/" + id , //endpoint
				HttpMethod.PUT, 
				httpEntity, // headers con la info de autorizacion
				String.class);		
		
		System.out.println("=> markAsReceived(..): " + result.getBody());
		
		this.lastError = "";

		if (! result.getStatusCode().is2xxSuccessful()) {
			this.lastError = "Ocurrio un error al intentar confirmar el pedido. Codigo de respuesta de la API: " + result.getStatusCodeValue();
			return false;
		}
		
		//actualizar productos:
		this.updateProducts(result);
		
		return true;
	}
	
	/**
	 * Funcion auxiliar que actualiza el stock de productos
	 * 
	 * @param result
	 */
	private void updateProducts(ResponseEntity<String> result) {
		
		//Conversion string -> objeto
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(OrderResponse.class, this.orderResponseDeserializer);
		Gson gson = gsonBuilder.create();
		OrderResponse response = gson.fromJson(result.getBody(), OrderResponse.class);
				
		ArrayList<Item> products = (response.getOrders()[0]).getProducts();
		System.out.println("=> productos: " + products.size() );
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		String jpql = "SELECT p FROM Product p WHERE codigo = :codigo";
		Query query = em.createQuery(jpql);
		
		for(int i=0 ; i < products.size(); i++) {
				
			//buscar producto
			Item item = products.get(i);
			query.setParameter("codigo", item.getId());
			Product prod = (Product) query.getSingleResult();
			
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				
				//actualizar stock
				Integer dif = prod.getCantidad() - item.getCount() ;	
				prod.setCantidad( dif );				
				tx.commit();
				
				//puede quedar negativo pero el TP no pedia ningún tipo de control..
				
			}catch(Exception e) {
				tx.rollback();
			}
							
		}
	
		em.close();				
	}
	
	public String getLastError() {
		return lastError;
	}


	public void setLastError(String lastError) {
		this.lastError = lastError;
	}


}
