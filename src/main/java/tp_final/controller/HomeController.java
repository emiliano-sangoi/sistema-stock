package tp_final.controller;

//import javax.persistence.EntityManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {

	@RequestMapping(value = {"/"})
    public String home(Model model) {		 
    	return "home";
    }	
	

}
