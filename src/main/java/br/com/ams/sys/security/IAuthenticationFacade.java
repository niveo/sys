package br.com.ams.sys.security;

import org.springframework.security.core.Authentication;

import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.entity.Usuario;

public interface IAuthenticationFacade {
	Authentication getAuthentication();

	Usuario getUsuario();

	UserDetailsImpl getUserDetails();
}