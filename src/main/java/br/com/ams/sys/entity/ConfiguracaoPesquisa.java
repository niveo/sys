package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ams.sys.records.ConfiguracaoPesquisaDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class ConfiguracaoPesquisa extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false, unique = true)
	private String caminho;

	@Column
	private String componenteCadastro;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "grade")
	private ConfiguracaoPesquisaGrade grade;

	@JsonManagedReference
	@OrderBy("posicao")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(targetEntity = ConfiguracaoPesquisaFiltro.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "configuracao", nullable = false)
	private List<ConfiguracaoPesquisaFiltro> filtros;

	public ConfiguracaoPesquisaDto toConfiguracaoPesquisaDto() {
		return ConfiguracaoPesquisaDto.builder().caminho(caminho).descricao(descricao)
				.componenteCadastro(componenteCadastro).codigo(codigo)
				.filtros(filtros.stream().map(m -> m.toConfiguracaoPesquisaFiltroDto()).toList())
				.grade(grade != null ? grade.toConfiguracaoPesquisaGradeDto() : null).build();
	}

}
