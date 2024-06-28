package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.service.EstadoService;
import jakarta.persistence.EntityNotFoundException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class EstadoServiceTest {

	@Autowired
	private EstadoService estadoService;

	private static final String descricao = "SAO PAULO";
	private static final String sigla = "SP";

	@Test
	void should_criar_estado() throws Exception {
		var estado = estadoService.salvar(new Estado(descricao, sigla));
		assertThat(estado).isNotNull();
		assertThat(estado.getDescricao()).isNotBlank();
		assertThat(estado.getSigla()).isNotBlank();
		assertThat(estado.getDescricao()).isEqualTo(descricao);
		assertThat(estado.getSigla()).isEqualTo(sigla);
	}

	@Test
	void should_criar_estado_campos_nulos() {
		Exception thrown = catchException(() -> {
			estadoService.salvar(new Estado());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates not-null constraint");
	}

	@Test
	void should_criar_estado_duplicado() {
		Exception thrown = catchException(() -> {
			estadoService.salvar(new Estado(descricao, sigla));
			estadoService.salvar(new Estado(descricao, sigla));
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates unique constraint");
	}

	@Test
	void should_obter_estado_codigo() throws Exception {
		var estado = estadoService.salvar(new Estado(descricao, sigla));
		assertThat(estado).isNotNull();
		estado = estadoService.obterCodigo(estado.getCodigo());
		assertThat(estado).isNotNull();
	}

	@Test
	void should_obter_estado_nulo() {
		Exception thrown = catchException(() -> {
			estadoService.obterCodigo(0L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");
	}

	@Test
	void should_criar_atualizar_estado() throws Exception {
		var estado = estadoService.salvar(new Estado(descricao, sigla));
		assertThat(estado).isNotNull();
		estado = estadoService.obterCodigo(estado.getCodigo());
		assertThat(estado).isNotNull();
		estado.setSigla("MG");
		estado = estadoService.salvar(estado);
		assertThat(estado.getSigla()).isNotBlank();
		assertThat(estado.getSigla()).isEqualTo("MG");
	}

	@Test
	void should_criar_remover_estado() throws Exception {
		var estado = estadoService.salvar(new Estado(descricao, sigla));
		assertThat(estado).isNotNull();
		estadoService.remover(estado.getCodigo());
	}

	@Test
	void should_criar_remover_estado_inexistente() throws Exception {
		estadoService.remover(0L);
	}
}
