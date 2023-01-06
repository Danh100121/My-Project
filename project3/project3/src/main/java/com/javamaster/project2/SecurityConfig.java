package com.javamaster.project2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;





@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, // phan quyen tren method
	prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
//	@Autowired
//	JwtTokenFilter jwtTokenFilter;


	//xac thuc

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	// lam api thi bat
//	@Override
//	@Bean
//	protected AuthenticationManager authenticationManager() throws Exception {
//		return super.authenticationManager();
//	}

	
	
	// phan quyen
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin/**")
//				.hasAnyRole("ADMIN","SUBADMIN")
				//ROLE_
				.hasAnyAuthority("Admin")
				.antMatchers("/member/**").authenticated()
				.anyRequest()
				.permitAll()
				.and().csrf().disable() // neu dung api thi ko can

//				.httpBasic()
		
				.formLogin()
				
				.loginPage("/login") // dung form cua minh 
//				.successHandler(loginSuccessHandler).failureUrl("/login?err")
				
				
				
				.loginProcessingUrl("/login")
				
				.failureUrl("/login?err")
				.defaultSuccessUrl("/user/search", true)
				.and().exceptionHandling()
				.accessDeniedPage("/login");
		
		// Apply JWT
//		http.addFilterBefore(jwtTokenFilter, 
//				UsernamePasswordAuthenticationFilter.class);
	}
}
