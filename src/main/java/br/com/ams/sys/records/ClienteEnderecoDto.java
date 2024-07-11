package br.com.ams.sys.records;

import lombok.Builder;

@Builder
public record ClienteEnderecoDto(Long codigo, String observacao, EnderecoDto endereco) {

}
