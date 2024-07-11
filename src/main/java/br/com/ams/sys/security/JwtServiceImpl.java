package br.com.ams.sys.security;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.ams.sys.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long tokenExpiration;

	@Value("${jwt.issuer}")
	private String issuer;

	@Override
	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	List<String> extractRoles(String jwt) {
		return extractClaim(jwt, claims -> (List<String>) claims.get("roles"));
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		return generateToken(Map.of(), userDetails);
	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String jwt) {
		return extractClaim(jwt, Claims::getExpiration).before(new Date());
	}

	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		long currentTimeMillis = System.currentTimeMillis();
		return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername()).issuer(issuer)
				.claim("roles",
						userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.map(role -> role.substring("ROLE_".length())).toArray())
				.issuedAt(new Date(currentTimeMillis)).expiration(new Date(currentTimeMillis + 
						tokenExpiration * 1000))
				.signWith(getSigningKey(), Jwts.SIG.HS256).compact();
	}

	private <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(jwt);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String jwt) {
	 
			return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwt).getPayload();
		 
	}

	private SecretKey getSigningKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}
}
