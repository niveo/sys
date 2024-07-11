package br.com.ams.sys.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.entity.Usuario;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public Usuario getUsuario() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		var userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
		return userDetailsImpl.getUsuario();
	}

	@Override
	public UserDetailsImpl getUserDetails() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		var userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
		return userDetailsImpl;
	}
}