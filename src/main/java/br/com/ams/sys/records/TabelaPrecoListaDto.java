package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.TabelaPreco;
import lombok.Builder;

@Builder
public record TabelaPrecoListaDto(Long codigo, String descricao, Boolean ativo) {

}
