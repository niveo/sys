package br.com.ams.sys.security;

import java.io.Serial;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
	@Serial
	private static final long serialVersionUID = 1L;

	JwtAuthenticationException(String msg) {
		super(msg);
	}
}