package br.com.ams.sys.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BairroCriarDto(@JsonProperty("descricao") String descricao) {
}
