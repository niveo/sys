package br.com.ams.sys.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractTimesTampEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Basic
	@CreationTimestamp()
	@Column
	private ZonedDateTime dataCadastrado;

	@Basic
	@UpdateTimestamp
	@Column
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