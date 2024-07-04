package br.com.ams.sys.records;

import br.com.ams.sys.enuns.TipoPessoa;

public record ClienteListaDto(Long codigo, String documento, String nome, String razaoSocial, String telefone,
		String email, String inscricaoEstadual, TipoPessoa tipoPessoa) {

}
