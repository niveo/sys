package br.com.ams.sys.records;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Endereco;
import lombok.Builder;

@Builder
public record EnderecoDto(String logradouro, String numero, String cep, String complemento, CidadeDto cidade,
		BairroDto bairro) {

	public Endereco toEndereco(Endereco endereco, Bairro bairro, Cidade cidade) {
		return endereco.toBuilder().cep(cep.replace("-", "")).complemento(complemento).logradouro(logradouro)
				.numero(numero).bairro(bairro).cidade(cidade).build();
	}
}
