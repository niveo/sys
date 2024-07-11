package br.com.ams.sys.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
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
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataCadastrado;

	@Basic
	@UpdateTimestamp
	@Column(name = "alterado")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataAlterado;

	@PrePersist
	public void onInsert() {
		dataCadastrado = LocalDate.now(ZoneId.systemDefault());
		dataAlterado = dataCadastrado;
	}

	@PreUpdate
	public void onUpdate() {
		dataAlterado = LocalDate.now(ZoneId.systemDefault());
	}
}