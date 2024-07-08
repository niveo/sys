package br.com.ams.sys.entity;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class AbstractTimesTampEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic
	@CreationTimestamp()
	@Column(name = "cadastrado")
	private ZonedDateTime dataCadastrado;

	@Basic
	@UpdateTimestamp
	@Column(name = "alterado")
	private ZonedDateTime dataAlterado;

	@PrePersist
	public void onInsert() {
		dataCadastrado = ZonedDateTime.now(ZoneId.systemDefault());
		dataAlterado = dataCadastrado;
	}

	@PreUpdate
	public void onUpdate() {
		dataAlterado = ZonedDateTime.now(ZoneId.systemDefault());
	}
}