package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.ams.sys.entity.Estado;
import jakarta.persistence.EntityNotFoundException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EstadoServiceTest {
	@Autowired
	private EstadoService estadoService;

	private static final String descricao = "teste";
	private static final String sigla = "ti";

	@Test
	void deve_criar_registro() throws Exception {
		var estado = estadoService.save(Estado.builder().descricao(descricao).sigla(sigla).build());
		assertThat(estado).isNotNull();
		assertThat(estado.getDescricao()).isNotBlank();
		assertThat(estado.getSigla()).isNotBlank();
		assertThat(estado.getDescricao()).isUpperCase();
		assertThat(estado.getSigla()).isUpperCase();
		assertThat(estado.getDescricao()).isEqualTo("TESTE");
		assertThat(estado.getSigla()).isEqualTo("TI");
	}

	@Test
	void deve_retornar_erro_campos_nulos() {
		Exception thrown = catchException(() -> {
			estadoService.save(new Estado());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates not-null constraint");
	}

	@Test
	void deve_retornar_erro_duplicidade() {
		Exception thrown = catchException(() -> {
			estadoService.save(Estado.builder().descricao(descricao).sigla("MG").build());
			estadoService.save(Estado.builder().descricao(descricao).sigla("MG").build());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates unique constraint");
	}

	@Test
	void deve_retornar_registro() throws Exception {
		var registro = estadoService.findByCodigo(1L);
		assertThat(registro).isNotNull();
	}

	@Test
	void deve_retornar_registro_nulo() {
		Exception thrown = catchException(() -> {
			estadoService.findByCodigo(0L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");
	}

	@Test
	void deve_criar_atualizar_registro() throws Exception {
		var estado = estadoService.save(Estado.builder().descricao(descricao).sigla("LD").build());
		assertThat(estado).isNotNull();
		estado.setSigla("NA");
		estado.setDescricao("NANA");
		estado = estadoService.save(estado);
		assertThat(estado).isNotNull();
		assertThat(estado.getSigla()).isNotBlank();
		assertThat(estado.getSigla()).isEqualTo("NA");
		assertThat(estado.getDescricao()).isNotBlank();
		assertThat(estado.getDescricao()).isEqualTo("NANA");
	}

	@Test
	void should_remover_registro() {
		estadoService.deleteByCodigo(1L);
		Exception thrown = catchException(() -> {
			estadoService.findByCodigo(1L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");

	}

	@Test
	void deve_remover_registro_inexistente() {
		estadoService.deleteByCodigo(0L);
	}

	@Test
	void deve_retornar_um_registro() throws Exception {
		var registros = estadoService.obterTodos();
		assertThat(registros).hasSize(1);
	}
}
