package com.felipe.helpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
@Configuration

public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationFilter filter;
    @Autowired
    private AuthenticationProvider provider;
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/autenticacao/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/clientes").permitAll()
                .requestMatchers(HttpMethod.GET, "/administradores").hasAuthority("ROLE_ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/usuarios").hasAuthority("ROLE_USUARIO")
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
        @Override
        public void addCorsMappings (CorsRegistry registry){
            registry.addMapping("/**");
        }
        
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
        	return new BCryptPasswordEncoder();
        }
    }








/** public class SecurityConfig {

	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	

	@Bean
	public void SecurityFilterChain (HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated());
		http.addFilter(new JWTAuthenticationFilter(AuthenticationManager, jwtUtil));
		//return http.build();
		
		//return http.build();
		//return http.cors().and().csrf().disable();
		//http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	/* @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
//
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
**/

