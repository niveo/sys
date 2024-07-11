package br.com.ams.sys.records;

import java.util.Set;

import br.com.ams.sys.enuns.TipoPessoa;
import lombok.Builder;

@Builder
public record EnderecoDto(String logradouro, String numero, String cep, String complemento,
		CidadeDto cidade, BairroDto bairro) {

}
