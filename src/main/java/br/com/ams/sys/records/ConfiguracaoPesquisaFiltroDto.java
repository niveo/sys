package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record ConfiguracaoPesquisaFiltroDto(Long codigo, Boolean requerido, String descricao, String campo,
		String componente, String tipo, Integer posicao) {

}
