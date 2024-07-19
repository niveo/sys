package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ams.sys.records.ConfiguracaoPesquisaDto;
import br.com.ams.sys.records.ConfiguracaoPesquisaGradeDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class ConfiguracaoPesquisaGrade extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String caminhoInserir;

	@Column(nullable = false)
	private String caminhoEditar;

	@Column(nullable = false)
	private String listaItem;

	public ConfiguracaoPesquisaGradeDto toConfiguracaoPesquisaGradeDto() {
		return ConfiguracaoPesquisaGradeDto.builder().caminhoEditar(caminhoEditar).caminhoInserir(caminhoInserir)
				.codigo(codigo).listaItem(listaItem).build();
	}

}
