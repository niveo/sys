package br.com.ams.sys.records;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.enuns.TipoPessoa;

public record EmpresaDto(Long codigo, String documento, String nome, String razaoSocial, String observacao,
		String telefone, String email, String inscricaoEstadual, TipoPessoa tipoPessoa, EnderecoDto endereco) {
	public Empresa toEmpresa(Empresa empresa, Cidade cidade, Bairro bairro) {
		return empresa.toBuilder().codigo(codigo).tipoPessoa(tipoPessoa).documento(documento).nome(nome)
				.inscricaoEstadual(
						inscricaoEstadual)
				.razaoSocial(razaoSocial).observacao(observacao).email(email).telefone(telefone)
				.endereco(Endereco.builder().cep(endereco.cep()).complemento(endereco.complemento())
						.logradouro(endereco.logradouro()).numero(endereco.numero()).cidade(cidade).bairro(bairro)
						.build())
				.build();

	}
}
