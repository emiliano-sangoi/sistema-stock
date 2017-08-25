package tp_final.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import tp_final.helpers.JPAUtil;
import tp_final.model.Producto;

@Controller
public class OrdenesController {
	
		@RequestMapping(value= {"/ordenes"})
		public String index(Model model) {								
			
			return "homeOrdenes";
		}
}
