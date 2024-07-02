package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.ams.sys.enuns.RoleName;

public record UsuarioCriarDto(@JsonProperty("email") String email, 
		@JsonProperty("senha") String senha,
		@JsonProperty("role") RoleName role) {

}
