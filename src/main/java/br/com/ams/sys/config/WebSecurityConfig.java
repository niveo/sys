package br.com.ams.sys.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.repository.UsuarioRepository;
import br.com.ams.sys.security.JwtAuthenticationFilter;

//https://medium.com/@felipeacelinoo/protegendo-sua-api-rest-com-spring-security-e-autenticando-usu%C3%A1rios-com-token-jwt-em-uma-aplica%C3%A7%C3%A3o-d70e5b0331f9
//https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;

	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = { "/usuarios/login", "/usuarios" };

	// Endpoints que requerem autenticação para serem acessados
	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = { "/usuarios/test", "/clientes",
			"/clientes/*", "/cidades", "/cidades/*", "/bairros", "/bairros/*", "/estados", "/estados/*", "/cep/*",
			"/usuarios/empresas", "/clienteenderecos", "/clienteenderecos/*" };

	// Endpoints que só podem ser acessador por usuários com permissão de cliente
	public static final String[] ENDPOINTS_CUSTOMER = { "/usuarios/test/customer" };

	// Endpoints que só podem ser acessador por usuários com permissão de
	// administrador
	public static final String[] ENDPOINTS_ADMIN = { "/usuarios/test/administrator", "/empresas", "/empresas/*", };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(config -> config.configure(http));

		http.authorizeHttpRequests(request -> request.requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
				.permitAll().requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
				.requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR").requestMatchers(ENDPOINTS_CUSTOMER)
				.hasRole("CUSTOMER").anyRequest().denyAll());

		http.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> usuarioRepository.findByEmail(username).map(m -> new UserDetailsImpl(m))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	CorsFilter corsFilter() {
		var source = new UrlBasedCorsConfigurationSource();
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedHeader("empresa");
		config.addAllowedMethod(HttpMethod.GET);
		config.addAllowedMethod(HttpMethod.POST);
		config.addAllowedMethod(HttpMethod.PUT);
		config.addAllowedMethod(HttpMethod.DELETE);
		config.addAllowedMethod(HttpMethod.OPTIONS);
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
