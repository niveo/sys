package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record BairroDto(Long codigo, String descricao) {

}
