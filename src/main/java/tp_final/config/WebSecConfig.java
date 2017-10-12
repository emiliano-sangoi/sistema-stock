package tp_final.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebMvcSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {			 
	 auth.userDetailsService(userDetailsService()).passwordEncoder(passwordencoder());;
		
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {  
         
        /*http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").hasRole("ADMIN")
                .antMatchers("/home").hasRole("ADMIN")
                .antMatchers("/pedidos").hasRole("ADMIN")
                .antMatchers("/productos").hasRole("ADMIN")
                .and().formLogin()
                .and().logout();*/
        

        http.csrf().disable()
        .authorizeRequests()
       // .antMatchers("/login").hasRole("USER")
        .antMatchers("/").hasRole("ADMIN")        
        .antMatchers("/pedidos").hasRole("ADMIN")
        .antMatchers("/productos").hasRole("ADMIN")
        .and().formLogin().loginPage("/login")
        .and().logout();
        
    } 
    
    @Bean(name="passwordEncoder")
    public PasswordEncoder passwordencoder(){
    	return new BCryptPasswordEncoder();
    }
    
	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/sgs_db");
	    driverManagerDataSource.setUsername("root");
	    driverManagerDataSource.setPassword("barcelona");
	    return driverManagerDataSource;
	}
    
    @Bean(name="userDetailsService")
    public UserDetailsService userDetailsService(){
    	JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
    	jdbcImpl.setDataSource(dataSource());    	
    	jdbcImpl.setUsersByUsernameQuery("select username,password, enabled from users where username=?");
    	jdbcImpl.setAuthoritiesByUsernameQuery("select b.username, a.role from user_roles a, "
    			+ "users b where b.username=? and a.userid=b.userid");
    	return jdbcImpl;
    }
}






