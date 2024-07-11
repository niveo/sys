package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://viacep.com.br/
 */
public record CepViaCepDto(@JsonProperty("logradouro") String logradouro, @JsonProperty("bairro") String bairro,
		@JsonProperty("localidade") String localidade, @JsonProperty("uf") String uf,
		@JsonProperty("ibge") String ibge) {

}
