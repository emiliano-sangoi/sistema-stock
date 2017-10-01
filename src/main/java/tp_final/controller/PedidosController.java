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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tp_final.helpers.APIHandler;
import tp_final.helpers.JPAUtil;
import tp_final.model.Item;
import tp_final.model.Order;
import tp_final.model.Producto;

@Controller
public class PedidosController {
	
		/**
		 * Listado de pedidos
		 * 
		 * @param model
		 * @return
		 */
		@RequestMapping(value= {"/pedidos"})
		public String index(Model model) {
			
			APIHandler apiHandler = new APIHandler();
			if(apiHandler.fetchOrders()){
				model.addAttribute("pedidos", apiHandler.getOrders() );
			}else {
				model.addAttribute("errorGlobal", apiHandler.getLastError());
			}
			
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
		public String nuevoPedidoGET(Model model) {
			
			Order order = new Order();
			//order.addItem(new Item());			
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
				return "redirect:/pedidos";
			}
			
			model.addAttribute("errorGlobal", apiHandler.getLastError());
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
				model.addAttribute("errorGlobal", "No existe ningun pedido con el id proporcionado." );
			}
						
			return "modificarPedido";
		
		}
		
		@RequestMapping(value= {"/pedidos/modificar"}, method=RequestMethod.POST)
		public String modificarPedidoPOST(@Valid Order order,
				Errors errores, 
				Model model) {						
			
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
				return "redirect:/pedidos";
			}else {
				model.addAttribute("errorGlobal", apiHandler.getLastError());				
			}
				
			return "modificarPedido";
		
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
