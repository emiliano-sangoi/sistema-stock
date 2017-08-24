/*
 * tp_final/src/main/java/tp_final/config/WebConfig.java
 */

package tp_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@EnableWebMvc //Habilitamos Spring MVC
//escanea paquetes en busca de componentes:
@ComponentScan(basePackages = {"tp_final.controller", "tp_final.service"}) 
public class WebConfig extends WebMvcConfigurerAdapter{
	  
	  @Bean
	  public TilesConfigurer tilesConfigurer() { 
		// nos servira para establecer la configurarcion del framework
	    TilesConfigurer tiles = new TilesConfigurer();
	    tiles.setDefinitions(new String[] { 
	    	//  indicamos los archivos de configuracion XML para Tiles, puede ser uno o mas de ellos.
	        "/WEB-INF/tiles/tiles.xml"
	    });
	    tiles.setCheckRefresh(true);
	    return tiles;
	  }
	  
	  // es el encargado de resolver la vista
	  // tomar el nombre enviado por el controlador y elegir el archivo JSP adecuado.
	  @Bean
	  public ViewResolver viewResolver() {
	    return new TilesViewResolver();
	  }	
	  
	  @Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/assets/**").addResourceLocations("/WEB-INF/assets/");
	  }
	  

}



