package br.com.ams.sys.records;

import br.com.ams.sys.enuns.TipoPessoa;
import lombok.Builder;

@Builder
public record ClienteDto(Long codigo, String documento, String nome, String razaoSocial, String observacao,
		String telefone, String email, String inscricaoEstadual, TipoPessoa tipoPessoa, EnderecoDto endereco,
		SegmentoClienteDto segmento, RedeClienteDto rede, TabelaPrecoBaseDto tabela) {

}
