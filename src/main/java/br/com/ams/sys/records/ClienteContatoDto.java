package br.com.ams.sys.records;

import java.util.Set;

import br.com.ams.sys.enuns.TipoPessoa;
import lombok.Builder;

@Builder
public record ClienteContatoDto(Long codigo, String nome, String cargo, String observacao,
		Set<String> telefones, Set<String> emails) {

}
