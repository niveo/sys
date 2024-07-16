package br.com.ams.sys.entity;

import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.ams.sys.records.ConfiguracaoViewDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table
public class ConfiguracaoView extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false, unique = true)
	private String caminho;

	@Column(nullable = false)
	private String caminhoInserir;

	@Column(nullable = false)
	private String caminhoEditar;

	@Column(nullable = false)
	private String listaItem;

	@Column
	@JdbcTypeCode(SqlTypes.JSON)
	private Set<ConfiguracaoViewFiltro> filtros;

	public ConfiguracaoViewDto toConfiguracaoViewDto() {
		var filtrosst = filtros.stream().sorted((a, b) -> b.getPosicao().compareTo(a.getPosicao()))
				.collect(Collectors.toSet());
		return ConfiguracaoViewDto.builder().caminho(caminho).caminhoEditar(caminhoEditar).listaItem(listaItem)
				.caminhoInserir(caminhoInserir).codigo(codigo).filtros(filtrosst).build();
	}
}
