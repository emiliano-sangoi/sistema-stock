package tp_final.helpers;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import tp_final.model.Pedido;
import tp_final.model.Producto;
import tp_final.util.PedidoResponse;

public class APIHandler {
	
	private String endpointBase;
	private HttpEntity<String> entity;
	
    
    public APIHandler() {
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("auth", "emiliano.sangoi@gmail.com");
        entity = new HttpEntity<String>("parameters", headers);
        
        //endpoints:
        endpointBase = "http://curso.kaitzen-solutions.com:3000/";
        
    	
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
	
	public Pedido[] getPedidos() {
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.set("auth", "emiliano.sangoi@gmail.com");
		
		String uri = endpointBase + "order";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, this.entity, String.class);
		
		//Convertir el response HTTP en un arreglo de Pedidos
		// para manejarlos mas comodamente:
		Gson gson = new Gson();
		PedidoResponse response = gson.fromJson(result.getBody(), PedidoResponse.class);
		
		//Pedido[] pedidos = response.getPedidos();		
		//System.out.println(response.toString());
		//System.out.println("C = " + pedidos.length);
		
		
		return response.getPedidos();
		
	}
	
	
	
	
	

}
