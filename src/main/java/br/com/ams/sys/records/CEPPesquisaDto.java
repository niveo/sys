package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record CEPPesquisaDto(String logradouro, CidadeDto cidade, BairroDto bairro) {

}
