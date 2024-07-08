package br.com.ams.sys.security;



import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	JwtAuthenticationException(String msg) {
		super(msg);
	}
}