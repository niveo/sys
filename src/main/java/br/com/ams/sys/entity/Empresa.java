package br.com.ams.sys.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table
public class Empresa extends AbstractClient {

	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "empresas", targetEntity = Usuario.class)
	private List<Usuario> usuarios;

}
