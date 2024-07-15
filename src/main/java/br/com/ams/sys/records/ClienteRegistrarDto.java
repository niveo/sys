package br.com.ams.sys.records;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.enuns.TipoPessoa;
import lombok.Builder;

@Builder
public record ClienteRegistrarDto(Long codigo, String documento, String nome, String razaoSocial, String observacao,
		String telefone, String email, String inscricaoEstadual, TipoPessoa tipoPessoa, EnderecoDto endereco,
		SegmentoClienteDto segmento, RedeClienteDto rede) {

	public Cliente toCliente(Cliente empresa) {
		return empresa.toBuilder().codigo(codigo).tipoPessoa(tipoPessoa).documento(documento).nome(nome)
				.inscricaoEstadual(inscricaoEstadual).razaoSocial(razaoSocial).observacao(observacao).email(email)
				.telefone(telefone).endereco(Endereco.builder().cep(endereco.cep()).complemento(endereco.complemento())
						.logradouro(endereco.logradouro()).numero(endereco.numero()).build())
				.build();

	}

}
