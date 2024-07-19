package br.com.ams.sys.records;

import java.util.List;
import java.util.Set;

import br.com.ams.sys.entity.ConfiguracaoPesquisaFiltro;
import lombok.Builder;

@Builder
public record ConfiguracaoPesquisaDto(Long codigo, String descricao, String componenteCadastro, String caminho, ConfiguracaoPesquisaGradeDto grade,
		List<ConfiguracaoPesquisaFiltroDto> filtros) {

}
