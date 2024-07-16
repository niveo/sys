package br.com.ams.sys.records;

import java.util.Set;

import br.com.ams.sys.entity.ConfiguracaoViewFiltro;
import lombok.Builder;

@Builder
public record ConfiguracaoViewDto(Long codigo, String caminho, String caminhoInserir, String caminhoEditar,
		String listaItem, Set<ConfiguracaoViewFiltro> filtros) {

}
