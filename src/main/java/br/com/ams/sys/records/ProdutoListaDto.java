package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Produto;
import lombok.Builder;

@Builder
public record ProdutoListaDto(Long codigo, String descricao, String referencia, Boolean ativo) {

}
