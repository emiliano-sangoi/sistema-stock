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
	
		System.out.println(result.getBody());
		
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
	
	public String getLastError() {
		return lastError;
	}



	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	
	
	/*
	public boolean altaPedido(Pedido pedido) {
		
		Order order = new Order();
		
		int c = pedido.getDetalle().size();
		Item[] products = new Item[c];
		for(int i = 0; i < c ; i++) {
			Item item = new Item();
			item.set_id("595ccec925647b09f0eb54aa");
			item.setCount(pedido.getDetalle().get(i).getCant());
			products[i] = item;
		}
		
		order.setProducts(products);
	
		String uri = endpointBase + "order";		
		MultiValueMap<String, String> headerss = new LinkedMultiValueMap<String, String>();
		headerss.add("Content-Type", "application/json");
		headerss.add("auth", "emiliano.sangoi@gmail.com");
			
		PedidoRequest ped = new PedidoRequest();
		ped.setOrder(order);


		HttpEntity<PedidoRequest> request = new HttpEntity<PedidoRequest>(ped, headerss);
		RestTemplate restTemplate = new RestTemplate();		  
		Order result = restTemplate.postForObject( uri, request, Order.class);

		//System.out.println( "SI!!!! -> " + result.getProducts().length);
		
		
		
		return true;
	}*/
	
/*public boolean altaPedido2(Pedido pedido) {
		
		Gson gson = new Gson();
		String pedidoJson = gson.toJson(pedido.getDetalle());
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>(); 
		body.add("products", pedidoJson);
		
		System.out.println( "body " + pedidoJson);
		
		MultiValueMap<String, String> headerss = new LinkedMultiValueMap<String, String>();
		headerss.add("Content-Type", "application/json");
		headerss.add("auth", "emiliano.sangoi@gmail.com");
		
		// Note the body object as first parameter!
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headerss);

		
		String uri = endpointBase + "order";
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> model = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

		
	
		
		return true;
	}*/
	
	
	

}
