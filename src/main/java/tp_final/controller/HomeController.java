package tp_final.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

//import javax.persistence.EntityManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {

	@RequestMapping(value = {"/"})
    public String home(Model model) {	
		
    	return "home";
    }		
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
  	public String login(
  		@RequestParam(value = "error", required = false) String error,
  		@RequestParam(value = "logout", required = false) String logout, ModelAndView model) {
  		
  		if (error != null) {
  			model.addObject("error", "Invalid username and password!");
  		}

  		if (logout != null) {
  			model.addObject("msg", "You've been logged out successfully.");
  		}  		
  		
  		return "login";
  	}

	

}
