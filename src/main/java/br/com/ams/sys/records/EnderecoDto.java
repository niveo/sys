package br.com.ams.sys.records;

public record EnderecoDto(String logradouro, String numero, String cep, String complemento,
		CidadeDto cidade, BairroDto bairro) {

}
