package br.com.ams.sys.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.ams.sys.entity.TabelaPreco;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import lombok.Builder;

@Builder
public record TabelaPrecoLancamentoDto(Long codigo, Long tabela, BigDecimal percentual,
		@JsonProperty("vigor") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime vigor) {

	public TabelaPrecoLancamento toTabelaPrecoLancamento(TabelaPrecoLancamento emp, TabelaPreco tabela) {
		return emp.toBuilder().codigo(codigo).tabela(tabela).percentual(percentual).vigor(vigor).build();
	}

}
