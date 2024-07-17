package br.com.ams.sys.records;

import java.util.List;
import java.util.Set;

import br.com.ams.sys.entity.ConfiguracaoGradeFiltro;
import lombok.Builder;

@Builder
public record ConfiguracaoGradeDto(Long codigo, String caminho, String caminhoInserir, String caminhoEditar,
		String listaItem, List<ConfiguracaoGradeFiltro> filtros) {

}
