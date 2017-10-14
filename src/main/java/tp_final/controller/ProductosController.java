package tp_final.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tp_final.helpers.APIHandler;
import tp_final.helpers.JPAUtil;
import tp_final.model.Product;

@Controller
@SessionAttributes({"flashMsgText", "flashMsgResult"})
public class ProductosController {
	
	@RequestMapping(value = {"/productos"})
	public String index(Model model,
			@ModelAttribute("flashMsgText") final String msg,
			@ModelAttribute("flashMsgResult") final String result,
			SessionStatus status) {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		Query query = em.createQuery("SELECT p FROM Product p");
		List<Product> productos = query.getResultList();		
		model.addAttribute("productos", productos);								
		em.close();	
		
		status.setComplete();
		
		return "homeProductos";
	}
	
	@RequestMapping(value= {"/productos/nuevo"}, method=RequestMethod.GET)
	public String nuevoProductoGET(Model model) {
		model.addAttribute("product", new Product());
		return "nuevoProducto";
	}
	
	@RequestMapping(value= {"/productos/nuevo"}, method=RequestMethod.POST)
	public String nuevoProductoPOST(@Valid Product producto,
			Errors errores,
			Model model, RedirectAttributes redirectAttributes) {		
		
		APIHandler apiHandler = new APIHandler();
		boolean producto_existe = apiHandler.productoExiste(producto.getCodigo());		
		model.addAttribute("producto_existe", producto_existe);
		
		if(errores.hasErrors() || producto_existe == false) {
			return "nuevoProducto";
		}
		
		//Guardar registro:
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		try {
			
			em.persist(producto);
			tx.commit();
			return setMsgYRedireccionar(redirectAttributes,
					"El producto se ha creado correctamente.", "success");
			
		}catch(Exception e) {
			tx.rollback();
		}
		
		em.close();
		
		return "redirect:/productos";
	}
	
	@RequestMapping(value= {"/productos/editar/{id}"}, method=RequestMethod.GET)
	public String modificarProductoGET(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		Product producto = em.find(Product.class, id);
		if(producto != null) {
			model.addAttribute("product", producto);
			return "editarProducto";
		}
		
		return setMsgYRedireccionar(redirectAttributes,
				"No se encontro ningun producto con id: '" + id + "'.", "warning");

	}
	
	@RequestMapping(value= {"/productos/editar/{id}"}, method=RequestMethod.POST)
	public String modificarProductoGuardar(@PathVariable Long id, 
			@ModelAttribute("product") @Valid Product product, 
			Errors errores, 
			Model model, RedirectAttributes redirectAttributes) {														

		APIHandler apiHandler = new APIHandler();
		boolean producto_existe = apiHandler.productoExiste(product.getCodigo());		
		if(errores.hasErrors() || producto_existe == false) {
			model.addAttribute("producto_existe", producto_existe);
			return "editarProducto";				
		}
	
		//si no hay errores, actualizar...	
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Product prodOld = em.find(Product.class, id);
		tx.begin();
		try {	
			prodOld.setCodigo(product.getCodigo());
			prodOld.setDescripcion(product.getDescripcion());
			prodOld.setPrecio(product.getPrecio());
			prodOld.setCantidad(product.getCantidad());
			tx.commit();
			em.close();
			return setMsgYRedireccionar(redirectAttributes,
					"El producto '" + product.getDescripcion() + "' ha sido modificado correctamente.", "success");
			
		}catch(Exception e) {
			tx.rollback();
		}
		
		em.close();
		return setMsgYRedireccionar(redirectAttributes,
				"No se pudo modificar el producto '" + product.getDescripcion() + "'.", "warning");
	}
	
	
	@RequestMapping(value= {"/productos/borrar/{id}"}, method=RequestMethod.GET)
	public String borrarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		Product producto = em.find(Product.class, id);
		if(producto != null) {
			
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			
			try {				
				em.remove(producto);
				tx.commit();
				em.close();
				return setMsgYRedireccionar(redirectAttributes,
						"El producto '" + producto.getDescripcion() + "' se ha borrado correctamente.", "success");
				
			}catch(Exception e) {
				tx.rollback();
			}
						
			em.close();			
			return setMsgYRedireccionar(redirectAttributes,
					"No se pudo borrar el producto '" + producto.getDescripcion() + "'.", "warning");
			
		}
		
		
		
		return "productos";
	}
	
	/**
	 * Funcion auxiliar para setear un msg y redireccionar al home de productos en donde se muestran
	 * 
	 * @param redirectAttributes
	 * @param msg
	 * @param result
	 * @return
	 */
	private String setMsgYRedireccionar(RedirectAttributes redirectAttributes, String msg, String result) {
		redirectAttributes.addFlashAttribute("flashMsgText", msg);
		redirectAttributes.addFlashAttribute("flashMsgResult", result);
		return "redirect:/productos";
	}
	
	@ModelAttribute("flashMsgText")
    public String getFlashMsgText() {
        return new String();

    }
	
	@ModelAttribute("flashMsgResult")
    public String getFlashMsgResult() {
        return new String();

    }
	

}
