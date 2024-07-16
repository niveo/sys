package br.com.ams.sys.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfiguracaoViewFiltro implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean requerido;
	private String descricao;
	private String campo;
	private String componente;
	private String tipo;
	private Integer posicao;
}
