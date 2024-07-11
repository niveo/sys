package br.com.ams.sys.entity;

import java.io.Serializable;

import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.EnderecoDto;
import br.com.ams.sys.records.EstadoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String logradouro;

	@Column(nullable = false)
	private String numero;

	@Column(nullable = false, length = 8)
	private String cep;

	@Column
	private String complemento;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Cidade.class, optional = false)
	@JoinColumn(nullable = false, name = "cidade")
	private Cidade cidade;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Bairro.class, optional = false)
	@JoinColumn(nullable = false, name = "bairro")
	private Bairro bairro;

	public EnderecoDto toEnderecoDto() {
		var estado = cidade.getEstado();
		var buildEstado = EstadoDto.builder().codigo(estado.getCodigo()).sigla(estado.getSigla()).build();
		var buildCidade = CidadeDto.builder().codigo(cidade.getCodigo()).descricao(cidade.getDescricao())
				.estado(buildEstado).build();
		var buildBairro = BairroDto.builder().codigo(bairro.getCodigo()).descricao(bairro.getDescricao()).build();
		return EnderecoDto.builder().cep(cep).complemento(complemento).logradouro(logradouro).numero(numero)
				.bairro(buildBairro).cidade(buildCidade).build();

	}
}
