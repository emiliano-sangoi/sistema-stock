package tp_final.helpers;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import tp_final.model.Producto;

public class APIHandler {
	
	
	
	public static Boolean productoExiste(Producto producto) {
		
		try {
			if(producto.getCodigo() != null) {
				final String uri = "http://curso.kaitzen-solutions.com:3000/product/{codigo}";
				
				//System.out.println(producto.getCodigo().toString());
			     
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("codigo", producto.getCodigo().toString());
				//params.put("codigo", "595ccd3225647b09f0eb54a9");							
				
				HttpHeaders headers = new HttpHeaders();
			    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			    headers.set("auth", "emiliano.sangoi@gmail.com");
			    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
				     
				RestTemplate restTemplate = new RestTemplate();
				//String result = restTemplate.getFor.getForObject(uri, String.class, entity,  params);
				ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);							
			
				System.out.println(result);
				
				
				return true;
				
			}
				
		}catch(org.springframework.web.client.HttpServerErrorException e) {						
				
		}
		
		
		return false;
		
		
	}
	
	
	

}
