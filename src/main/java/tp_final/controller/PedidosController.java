package tp_final.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tp_final.helpers.APIHandler;
import tp_final.helpers.JPAUtil;
import tp_final.model.Pedido;
import tp_final.model.Producto;

@Controller
public class PedidosController {
	
		@RequestMapping(value= {"/pedidos"})
		public String index(Model model) {
			
			APIHandler apiHandler = new APIHandler();			
			model.addAttribute("pedidos", apiHandler.getPedidos());
			
			return "homePedidos";
		}
		
		@RequestMapping(value= {"/pedidos/nuevo"}, method=RequestMethod.GET)
		public String nuevoPedidoGET(Model model) {
			model.addAttribute("pedido", new Pedido());
			return "nuevoPedido";
		}
		
		@RequestMapping(value= {"/pedidos/nuevo"}, method=RequestMethod.POST)
		public String nuevaOrdenPOST(@Valid Pedido pedido, Errors errores, Model model) {
			
			
			
			return "redirect:/homePedidos";
		}
}
