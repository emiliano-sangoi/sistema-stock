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
import tp_final.model.Product;

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
				
			}else {
				model.addAttribute("flashMsgText", apiHandler.getLastError());
				model.addAttribute("flashMsgResult", "danger");		
			}
			
			status.setComplete();
			
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
			model.addAttribute("order", order);
			
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
				return setMsgYRedireccionar(redirectAttributes, "El pedido se ha creado correctamente.", "success");				
			}else {
				return setMsgYRedireccionar(redirectAttributes, apiHandler.getLastError(), "warning");
			}

		}
		
		@RequestMapping(value= {"/pedidos/modificar/{id}"}, method=RequestMethod.GET)
		public String modificarPedidoGET(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
			
			APIHandler apiHandler = new APIHandler();	

			//Guardar y redireccionar:
			if(apiHandler.fetchOrder(id) && apiHandler.getOrders().length >= 1) {	
				Order order = apiHandler.getOrders()[0];
				if(order.isOpen() == false) {
					return setMsgYRedireccionar(redirectAttributes, "El pedido: " + order.getId() + " no se puede modificar porque ya ha sido marcado como 'received'", "warning");
				}
				model.addAttribute("productos", this.getProductos());		
				model.addAttribute("order", order );
			}else {
				return setMsgYRedireccionar(redirectAttributes, apiHandler.getLastError(), "warning");
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
				return setMsgYRedireccionar(redirectAttributes, "El pedido ha sido modificado exitósamente.", "success");
			}else {
				return setMsgYRedireccionar(redirectAttributes, apiHandler.getLastError(), "warning");					
			}

		}
		
		@RequestMapping(value= {"/pedidos/borrar/{id}"}, method=RequestMethod.GET)
		public String borrarPedido(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {	
			
			APIHandler apiHandler = new APIHandler();
			
			//Guardar y redireccionar:							
			if(apiHandler.deleteOrder(id)) {
				return setMsgYRedireccionar(redirectAttributes, "El pedido ha sido borrado exitosamente.", "success");				
			}else {
				return setMsgYRedireccionar(redirectAttributes, apiHandler.getLastError(), "warning");
			}			
									
		}			
		
		
		@RequestMapping(value= {"/pedidos/confirmar/{id}"}, method=RequestMethod.GET)
		public String confirmarPedido(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
			
			APIHandler apiHandler = new APIHandler();	
			
			//Guardar y redireccionar:
			if(apiHandler.markAsReceived(id)) {					
				return setMsgYRedireccionar(redirectAttributes, "El pedido con código: "+ id + " ha sido confirmado exitosamente.", "success");
			}else {
				return setMsgYRedireccionar(redirectAttributes, apiHandler.getLastError(), "warning");
			}
	
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
					String msg = "No pueden existir dos items referenciando el mismo producto.  Producto nro. " + (i+1) + " repetido. Código: " + prodId;
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
		private List<Product> getProductos() {
			
			EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();			
			String sql = "SELECT p FROM Product p";			
			Query query = em.createQuery(sql);
			List<Product> productos = query.getResultList();
			
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
		 * Funcion auxiliar para setear un msg y redireccionar al home de pedidos en donde se muestran
		 * 
		 * @param redirectAttributes
		 * @param msg
		 * @param result
		 * @return
		 */
		private String setMsgYRedireccionar(RedirectAttributes redirectAttributes, String msg, String result) {
			redirectAttributes.addFlashAttribute("flashMsgText", msg);
			redirectAttributes.addFlashAttribute("flashMsgResult", result);
			return "redirect:/pedidos";
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
