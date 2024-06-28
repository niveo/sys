package br.com.ams.sys.entity;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@Entity
@Table
public class Empresa extends AbstractClient {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "empresas", targetEntity = Usuario.class)
	private List<Usuario> usuarios;

}
