package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmpresaRegistrarEnderecoDto(String logradouro, String numero, String cep, String complemento,
		EmpresaRegistrarEnderecoCidadeBairroDto cidade, EmpresaRegistrarEnderecoCidadeBairroDto bairro) {

}
