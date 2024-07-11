package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record CidadeDto(Long codigo, String descricao, EstadoDto estado) {

}
