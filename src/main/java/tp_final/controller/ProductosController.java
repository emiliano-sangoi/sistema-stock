package tp_final.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.validation.Valid;

import org.hibernate.mapping.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import tp_final.helpers.APIHandler;
import tp_final.helpers.JPAUtil;
import tp_final.model.Producto;

@Controller
public class ProductosController {
	
	@RequestMapping(value = {"/productos"})
	public String index(Model model) {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		Query query = em.createQuery("SELECT p FROM Producto p");
		List<Producto> productos = query.getResultList();		
		model.addAttribute("productos", productos);								
		em.close();	
		
		return "homeProductos";
	}
	
	@RequestMapping(value= {"/productos/nuevo"}, method=RequestMethod.GET)
	public String nuevoProductoGET(Model model) {
		model.addAttribute("producto", new Producto());
		return "nuevoProducto";
	}
	
	@RequestMapping(value= {"/productos/nuevo"}, method=RequestMethod.POST)
	public String nuevoProductoPOST(@Valid Producto producto, Errors errores, Model model) {
		
		
		Boolean codigoOk = APIHandler.productoExiste(producto);
		if(codigoOk) {
			System.out.println("codigo de producto OK");
		}else {
			System.out.println("codigo de producto invalido");
		}
		model.addAttribute("codigoOk", codigoOk);
		
		if(errores.hasErrors() || codigoOk == false) {
			return "nuevoProducto";
		}
		
		//Guardar registro:
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		try {
			
			em.persist(producto);
			tx.commit();
			
		}catch(Exception e) {
			tx.rollback();
		}
		
		em.close();
		
		return "redirect:/productos";
	}

}
