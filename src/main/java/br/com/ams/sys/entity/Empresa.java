package br.com.ams.sys.entity;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Empresa extends AbstractClient {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "empresas", targetEntity = Usuario.class)
	private List<Usuario> usuarios;

}
