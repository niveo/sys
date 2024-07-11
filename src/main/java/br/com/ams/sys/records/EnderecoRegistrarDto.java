package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record EnderecoRegistrarDto(String logradouro, String numero, String cep, String complemento,
		EnderecoCidadeBairroRegistrarDto cidade, EnderecoCidadeBairroRegistrarDto bairro) {

}
