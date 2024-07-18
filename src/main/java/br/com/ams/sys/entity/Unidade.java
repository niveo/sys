package br.com.ams.sys.entity;

import br.com.ams.sys.records.UnidadeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })

public class Unidade extends BaseEntityEmpresa {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Descrição é obrigatório")
	@Column(nullable = false)
	private String descricao;

	@NotBlank(message = "Sigla é obrigatório")
	@Column(nullable = false, length = 4)
	private String sigla;

	public UnidadeDto toUnidadeDto() {
		return UnidadeDto.builder().descricao(descricao).sigla(sigla).build();
	}
}