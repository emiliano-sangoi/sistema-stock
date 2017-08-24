package tp_final.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class [] {WebConfig.class};
	}

	@Override
	//  Con el metodo getServletMappings estamos indicando que el Dispatcher Servlet va a procesar la ruta "/" 
	//	siendo asi el servlet predeterminado de nuestra aplicacion web
	protected String[] getServletMappings() { 
		return new String[] {"/"};
	}

}



