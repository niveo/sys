package br.com.ams.sys.records;

import br.com.ams.sys.enuns.TipoPessoa;
import lombok.Builder;

@Builder
public record SegmentoDto(Long codigo, String descricao) {

}
