package br.com.ams.sys.records;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.entity.Endereco;
import lombok.Builder;

@Builder
public record ClienteEnderecoDto(Long codigo, String observacao, Long cliente, EnderecoDto endereco) {
	public ClienteEndereco toClienteEndereco(ClienteEndereco clienteEndereco, Cliente cliente, Cidade cidade,
			Bairro bairro) {
		return clienteEndereco.toBuilder().cliente(cliente).observacao(observacao)
				.endereco(endereco.toEndereco(new Endereco(), bairro, cidade)).build();
	}
}
