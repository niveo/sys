package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CidadeCriarDto(@JsonProperty("descricao") String descricao, @JsonProperty("estado") Long estado) {
}
