package tp_final.helpers;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

import javax.validation.constraints.Null;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import tp_final.model.Pedido;
import tp_final.model.Producto;
import tp_final.util.PedidoRequest;
import tp_final.util.APIOrderResponse;

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
	
	
	
	public Boolean productoExiste(Producto producto) {
		
		try {
			if(producto.getCodigo() != null) {
				final String uri = endpointBase + "product/{codigo}";
				
				//System.out.println(producto.getCodigo().toString());
			     
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("codigo", producto.getCodigo().toString());
				//params.put("codigo", "595ccd3225647b09f0eb54a9");							
				
				/*HttpHeaders headers = new HttpHeaders();
			    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			    headers.set("auth", "emiliano.sangoi@gmail.com");
			    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);*/
				     
				RestTemplate restTemplate = new RestTemplate();
				//String result = restTemplate.getFor.getForObject(uri, String.class, entity,  params);
				ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, this.entity, String.class, params);							
			
				System.out.println(result);
				
				
				return true;
				
			}
				
		}catch(org.springframework.web.client.HttpServerErrorException e) {						
				
		}
		
		
		return false;
		
		
	}
	
	


	/**
	 * Obtiene todos los pedidos de la API
	 * @return
	 */
/*	public Pedido[] getPedidos() {
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.set("auth", "emiliano.sangoi@gmail.com");
	    //headers.setContentType(MediaType.APPLICATION_JSON);
		
		String uri = endpointBase + "order";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, this.entity, String.class);
		
		//Convertir el response HTTP en un arreglo de Pedidos
		// para manejarlos mas comodamente:
		Gson gson = new Gson();
		APIOrderResponse response = gson.fromJson(result.getBody(), APIOrderResponse.class);
		
		Pedido[] pedidos = response.getPedidos();		
		//System.out.println(response.toString());
		//System.out.println("C = " + pedidos.length);
		//System.out.println("LP = " + pedidos[0].getDetalle().size());
		
		
		return response.getPedidos();
		
	}*/
	

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
		
		//System.out.println(result.getBody());
		//System.out.println(response.getData().length + "");
		//System.out.println(response.getStatus());
		//System.out.println(response.getOrders() == null ? "es null" : "no es null");
		//System.out.println("" + response.getOrders().length);
		
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
		
		
		/*Item item = new Item();
		item.setId("595ccec925647b09f0eb54aa");
		item.setCount(2);
		Item item2 = new Item();
		item2.setId("595ccd3225647b09f0eb54a9");
		item2.setCount(88);*/
		//Item[] products = new Item[2];
		
		//products[0] = item;
		//products[1] = item2;
		/*Order o = new Order();
		o.addItem(item);
		o.addItem(item2);*/
		//o.setProducts(products);
		
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
		
		return true;
	}
	
	public String getLastError() {
		return lastError;
	}


	public void setLastError(String lastError) {
		this.lastError = lastError;
	}


}
