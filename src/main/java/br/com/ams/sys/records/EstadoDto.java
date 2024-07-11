package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record EstadoDto(Long codigo, String descricao, String sigla) {

}
