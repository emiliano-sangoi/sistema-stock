package tp_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductosController {
	
	@RequestMapping(value = {"/productos"})
	public String index(Model model) {
		return "homeProductos";
	}

}
