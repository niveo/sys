package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record BairroCriarDto(@JsonProperty("descricao") String descricao) {
}
