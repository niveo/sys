package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(@JsonProperty("email") String email, @JsonProperty("senha") String senha) {
}
