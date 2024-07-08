package br.com.ams.sys.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.ams.sys.config.WebSecurityConfig;
import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.repository.UsuarioRepository;
import br.com.ams.sys.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	@Autowired
	JwtService jwtService;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (!checkIfEndpointIsNotPublic(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = recoveryToken(request);
			if (token == null)
				throw new RuntimeException("O token está ausente.");

			final String userEmail = jwtService.extractUsername(token);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (userEmail != null && authentication == null) {
				System.out.println(userEmail);
				UserDetails userDetails = usuarioRepository.findByEmail(userEmail).map(m -> new UserDetailsImpl(m))
						.get();

				if (jwtService.isTokenValid(token, userDetails)) {

					var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					throw new RuntimeException("O token é invalido.");
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			exception.printStackTrace();
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}

	}

	// Recupera o token do cabeçalho Authorization da requisição
	private String recoveryToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}

		return null;
	}

	// Verifica se o endpoint requer autenticação antes de processar a requisição
	private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return !Arrays.asList(WebSecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
	}

}