package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record ConfiguracaoPesquisaGradeDto(Long codigo, String caminhoInserir, String caminhoEditar, String listaItem) {

}
