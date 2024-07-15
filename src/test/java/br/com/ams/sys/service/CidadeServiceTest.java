package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.records.CidadeCriarDto;
import jakarta.persistence.EntityNotFoundException;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CidadeServiceTest {
	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private EstadoService estadoService;

	@Test
	void deve_criar_registro() throws Exception {
		var estado = estadoService.save(Estado.builder().descricao("TESTE").sigla("TE").build());
		var registro = cidadeService.save(Cidade.builder().descricao("teste").estado(estado).build());
		assertThat(registro).isNotNull();
		assertThat(registro.getDescricao()).isNotBlank();
		assertThat(registro.getDescricao()).isUpperCase();
		assertThat(registro.getDescricao()).isEqualTo("TESTE");
	}

	@Test
	void deve_retornar_erro_campos_nulos() {
		Exception thrown = catchException(() -> {
			cidadeService.save(new Cidade());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates not-null constraint");
	}

	@Test
	void deve_retornar_erro_duplicidade() {
		Exception thrown = catchException(() -> {
			var estado = estadoService.save(Estado.builder().descricao("TESTE").sigla("T1").build());
			cidadeService.save(Cidade.builder().descricao("C").estado(estado).build());
			cidadeService.save(Cidade.builder().descricao("C").estado(estado).build());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates unique constraint");
	}

	@Test
	void deve_retornar_registro() throws Exception {
		var registro = cidadeService.findByCodigo(1L);
		assertThat(registro).isNotNull();
	}

	@Test
	void deve_retornar_registro_nulo() {
		Exception thrown = catchException(() -> {
			cidadeService.findByCodigo(0L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");
	}

	@Test
	void deve_criar_atualizar_registro() throws Exception {
		var estado = estadoService.save(Estado.builder().descricao("TESTE").sigla("T3").build());
		var registro = cidadeService.save(Cidade.builder().descricao("E").estado(estado).build());
		assertThat(registro).isNotNull();
		registro.setDescricao("F");
		registro = cidadeService.save(registro);
		assertThat(registro).isNotNull();
		assertThat(registro.getDescricao()).isNotBlank();
		assertThat(registro.getDescricao()).isEqualTo("F");
	}

	@Test
	void should_remover_registro() {
		cidadeService.deleteByCodigo(1L);
		Exception thrown = catchException(() -> {
			cidadeService.findByCodigo(1L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");

	}

	@Test
	void deve_remover_registro_inexistente() {
		cidadeService.deleteByCodigo(0L);
	}

	@Test
	void deve_criar_registro_dto() throws Exception {
		var estado = estadoService.save(Estado.builder().descricao("TESTE").sigla("T4").build());
		var registro = cidadeService
				.save(CidadeCriarDto.builder().descricao("testedto").estado(estado.getCodigo()).build());
		assertThat(registro).isNotNull();
		assertThat(registro.descricao()).isNotBlank();
		assertThat(registro.descricao()).isUpperCase();
		assertThat(registro.descricao()).isEqualTo("TESTEDTO");
	}

}
