package br.com.ams.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.ams.sys.records.ConfiguracaoPesquisaFiltroDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ConfiguracaoPesquisaFiltro extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	private Boolean requerido;
	private String descricao;
	private String campo;
	private String componente;
	private String tipo;
	private Integer posicao;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "configuracao", nullable = false, insertable = false, updatable = false)
	private ConfiguracaoPesquisa configuracao;

	public ConfiguracaoPesquisaFiltroDto toConfiguracaoPesquisaFiltroDto() {
		return ConfiguracaoPesquisaFiltroDto.builder().campo(campo).codigo(codigo).componente(componente)
				.descricao(descricao).posicao(posicao).requerido(requerido).tipo(tipo).build();
	}
}
