package tp_final.controller;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Null;

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
import tp_final.model.Item;
import tp_final.model.Order;
import tp_final.model.Producto;

@Controller
@SessionAttributes({"flashMsgText", "flashMsgResult"})
public class PedidosController {	
	
		/**
		 * Listado de pedidos
		 * 
		 * @param model
		 * @return
		 */
		@RequestMapping(value= {"/pedidos"})
		public String index(Model model, 
				@ModelAttribute("flashMsgText") final String msg,
				@ModelAttribute("flashMsgResult") final String result,
				SessionStatus status) {
			
			APIHandler apiHandler = new APIHandler();
			if(apiHandler.fetchOrders()){
				model.addAttribute("pedidos", apiHandler.getOrders() );				
				/*if(msg != null && msg.length() > 0) {
					model.addAttribute("flashMsgText", msg );
					model.addAttribute("flashMsgResult", result );
				}	*/			
				
			}else {
				model.addAttribute("flashMsgText", apiHandler.getLastError());
				model.addAttribute("flashMsgResult", "danger");		
			}
			
			status.setComplete();
			
			//apiHandler.fetchOrder("59bcafb5b2d7841d360ab268");
			//apiHandler.fetchOrders();
			
			
			return "homePedidos";
		}
		
		/**
		 * Crear nuevo pedido (GET)
		 * 
		 * @param model
		 * @return
		 */
		@RequestMapping(value= {"/pedidos/nuevo"}, method=RequestMethod.GET)
		public String nuevoPedidoGET(Model model,  SessionStatus status) {
			
			Order order = new Order();
			//order.addItem(new Item());			
			model.addAttribute("order", order);
			
			status.setComplete();
			
			return "nuevoPedido";
		}
		
		/**
		 * Crear nuevo pedido (POST)
		 * 
		 * @param order
		 * @param errores
		 * @param model
		 * @param request
		 * @return
		 */
		@RequestMapping(value= {"/pedidos/nuevo"}, method=RequestMethod.POST)
		public String nuevoPedidoPOST(@Valid Order order,
				Errors errores, 
				Model model, 
				RedirectAttributes redirectAttributes,
				@Null HttpServletRequest request) {	
			
						
			model.addAttribute("productos", this.getProductos());						
			
			int cod = order.getCodOp();
			if(cod >= 0) {
				//borrar item
				order.removeItem(cod);
				return "nuevoPedido";
				
			}else if(cod == Order.ACTION_NUEVO_ITEM) {
				//agregar item
				order.addItem(new Item());
				return "nuevoPedido";
			}
			
			//guardar pedido...			
			
			//verificar que no haya productos duplicados:
			this.checkearItemsDuplicados(order, model, errores);							
			
			if(errores.hasErrors()) {
				return "nuevoPedido";			
			}
			
			
			APIHandler apiHandler = new APIHandler();	
			//Guardar y redireccionar:
			if(apiHandler.newOrder(order)) {
				redirectAttributes.addFlashAttribute("flashMsgText", "El pedido se ha creado correctamente.");
				redirectAttributes.addFlashAttribute("flashMsgResult", "success");
				return "redirect:/pedidos";
			}
			
			model.addAttribute("msgGlobalTexto", apiHandler.getLastError());
			model.addAttribute("msgGlobalResultado", "danger");		
			return "nuevoPedido";

						
		}
		
		@RequestMapping(value= {"/pedidos/modificar/{id}"}, method=RequestMethod.GET)
		public String modificarPedidoGET(@PathVariable String id, Model model) {
			
			APIHandler apiHandler = new APIHandler();	
			apiHandler.fetchOrder(id);
			//Guardar y redireccionar:
			if(apiHandler.fetchOrder(id) && apiHandler.getOrders().length >= 1) {	
				model.addAttribute("productos", this.getProductos());		
				model.addAttribute("order", apiHandler.getOrders()[0] );
			}else {
				model.addAttribute("msgGlobalTexto", apiHandler.getLastError());
				model.addAttribute("msgGlobalResultado", "danger");		
			}
						
			return "modificarPedido";
		
		}
		
		@RequestMapping(value= {"/pedidos/modificar"}, method=RequestMethod.POST)
		public String modificarPedidoPOST(@Valid Order order,
				Errors errores, 
				Model model,
				RedirectAttributes redirectAttributes
				) {						
			
			model.addAttribute("productos", this.getProductos());			
			
			int cod = order.getCodOp();
			if(cod >= 0) {
				//borrar item
				order.removeItem(cod);
				return "modificarPedido";
				
			}else if(cod == Order.ACTION_NUEVO_ITEM) {
				//agregar item
				order.addItem(new Item());
				return "modificarPedido";
			}
			
			//guardar pedido...			
			
			//verificar que no haya productos duplicados:
			this.checkearItemsDuplicados(order, model, errores);
			
			if(errores.hasErrors()) {
				return "modificarPedido";			
			}
			
			APIHandler apiHandler = new APIHandler();
			//Guardar y redireccionar:
			if(apiHandler.updateOrder(order)) {
				redirectAttributes.addFlashAttribute("flashMsgText", "El pedido ha sido modificado exitosamente.");
				redirectAttributes.addFlashAttribute("flashMsgResult", "success");
				return "redirect:/pedidos";
			}else {
				
				model.addAttribute("flashMsgText", apiHandler.getLastError());
				model.addAttribute("flashMsgResult", "danger");					
			}
				
			return "modificarPedido";
		
		}
		
		@RequestMapping(value= {"/pedidos/borrar/{id}"}, method=RequestMethod.GET)
		public String borrarPedido(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {	
			
			APIHandler apiHandler = new APIHandler();
			
			//Guardar y redireccionar:
			if(apiHandler.deleteOrder(id)) {
				//redirectAttributes.addAttribute(attributeValue)
				
				redirectAttributes.addFlashAttribute("flashMsgText", "El pedido ha sido borrado exitosamente.");
				redirectAttributes.addFlashAttribute("flashMsgResult", "success");
				return "redirect:/pedidos";
			}else {
				redirectAttributes.addAttribute("msgGlobalTexto", apiHandler.getLastError());
			}			
			
			
			return "redirect:/pedidos";
		}
		
		/**
		 * Funcion auxiliar que comprueba que no existan dos items referenciando al mismo producto
		 * 
		 * @param order
		 * @param model
		 * @param errores
		 * @return
		 */
		private boolean checkearItemsDuplicados(Order order, Model model, Errors errores) {
			
			HashSet<String> items = new HashSet<String>();
			for(int i=0; i < order.getProducts().size() ; i++) {
				String prodId = order.getProducts().get(i).getId();
				if(!items.contains(prodId)) {
					items.add(prodId);
				}else {
					//si algun id del producto se definio dos veces -> error
					String msg = "No pueden existir dos items referenciando el mismo producto.  Producto nro. " + (i+1) + " repetido. Codigo: " + prodId;
					model.addAttribute("errorItemsDuplicados", msg);	
					errores.reject(msg);
					return true;
				}
				
			}
			
			return false;			
		}

		/**
		 * Devuelve todos los productos
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private List<Producto> getProductos() {
			
			EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();			
			String sql = "SELECT p FROM Producto p";			
			Query query = em.createQuery(sql);
			List<Producto> productos = query.getResultList();
			
			return productos;			
		}
		
		
		@ModelAttribute("flashMsgText")
	    public String getFlashMsgText() {
	        return new String();

	    }
		
		@ModelAttribute("flashMsgResult")
	    public String getFlashMsgResult() {
	        return new String();

	    }
		
		/**
		 * Devuelve los productos disponibles en funcion de los productos ya elegidos en el pedido.
		 * 
		 * @param order
		 * @return
		 */
		/*private List<Producto> getProductosDisponibles(Order order){
			
			EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();		
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
			
			Root<Producto> producto = cq.from(Producto.class);
			for(int i=0; i < order.getProducts().size(); i++) {
				Item item = order.getProducts().get(i);
				cq.where(cb.notEqual(producto.get("codigo"), item.getId()));
			}			
			cq.select(producto);
			TypedQuery<Producto> q = em.createQuery(cq);					
			
			List<Producto> productos = q.getResultList();
			
			System.out.println("c. productos: " + productos.size());
			
			return productos;
		}*/
		
}
