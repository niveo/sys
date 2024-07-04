package br.com.ams.sys.records;

import br.com.ams.sys.enuns.TipoPessoa;

public record EmpresaDto(Long codigo, String documento, String nome, String razaoSocial, String observacao,
		String telefone, String email, String inscricaoEstadual, TipoPessoa tipoPessoa, EnderecoDto endereco) {

}
