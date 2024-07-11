package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroCriarDto;
import jakarta.persistence.EntityNotFoundException;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BairroServiceTest {
	@Autowired
	private BairroService bairroService;

	@Test
	void deve_criar_registro() throws Exception {
		var registro = bairroService.save(Bairro.builder().descricao("teste").build());
		assertThat(registro).isNotNull();
		assertThat(registro.getDescricao()).isNotBlank();
		assertThat(registro.getDescricao()).isUpperCase();
		assertThat(registro.getDescricao()).isEqualTo("TESTE");
	}

	@Test
	void deve_retornar_erro_campos_nulos() {
		Exception thrown = catchException(() -> {
			bairroService.save(new Bairro());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates not-null constraint");
	}

	@Test
	void deve_retornar_erro_duplicidade() {
		Exception thrown = catchException(() -> {
			bairroService.save(Bairro.builder().descricao("C").build());
			bairroService.save(Bairro.builder().descricao("C").build());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates unique constraint");
	}

	@Test
	void deve_etornar_registro() throws Exception {
		var registro = bairroService.findByCodigo(1L);
		assertThat(registro).isNotNull();
	}

	@Test
	void deve_retornar_registro_nulo() {
		Exception thrown = catchException(() -> {
			bairroService.findByCodigo(0L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");
	}

	@Test
	void deve_criar_atualizar_registro() throws Exception {
		var registro = bairroService.save(Bairro.builder().descricao("E").build());
		assertThat(registro).isNotNull();
		registro.setDescricao("F");
		registro = bairroService.save(registro);
		assertThat(registro).isNotNull();
		assertThat(registro.getDescricao()).isNotBlank();
		assertThat(registro.getDescricao()).isEqualTo("F");
	}

	@Test
	void should_remover_registro() {
		bairroService.deleteByCodigo(1L);
		Exception thrown = catchException(() -> {
			bairroService.findByCodigo(1L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");

	}

	@Test
	void deve_remover_registro_inexistente() {
		bairroService.deleteByCodigo(0L);
	}

	@Test
	void deve_criar_registro_dto() throws Exception {
		var registro = bairroService.save(BairroCriarDto.builder().descricao("testedto").build());
		assertThat(registro).isNotNull();
		assertThat(registro.descricao()).isNotBlank();
		assertThat(registro.descricao()).isUpperCase();
		assertThat(registro.descricao()).isEqualTo("TESTEDTO");
	}

	@Test
	void deve_retornar_um_registro() throws Exception {
		bairroService.save(BairroCriarDto.builder().descricao("pesquisa1").build());
		var registros = bairroService.pesquisarDescricao("pesquisa1");
		assertThat(registros).hasSize(1);
	}

}
