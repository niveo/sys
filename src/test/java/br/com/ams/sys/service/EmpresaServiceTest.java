package br.com.ams.sys.service;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.ams.sys.bd.DadosIniciaiService;
import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.EmpresaDto;
import br.com.ams.sys.records.EnderecoDto;
import jakarta.persistence.EntityNotFoundException;

@Disabled
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpresaServiceTest {
	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private DadosIniciaiService dadosIniciaiService;

	@Order(1)
	@Test
	void inicial() throws Exception {
		dadosIniciaiService.registrarEstadoBairroCidade();
	}

	Empresa getEmpresa() throws Exception {
		var bairro = bairroService.findByCodigo(1L);
		var cidade = cidadeService.findByCodigo(1L);
		var endereco = new Endereco("Rua Teste", "1", "09980200", "COMPLEMENTO", cidade, bairro);
		return Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).nome("Teste")
				.documento("33592119877").razaoSocial("Teste").build();
	}

	@Order(2)
	@Test
	void deve_criar_registro() throws Exception {
		var empresa = getEmpresa();
		var registro = empresaService.save(empresa);
		validarEmpresa(registro, empresa.getDocumento());
	}

	void validarEmpresa(Empresa registro, String documento) {
		assertAll("Empresa", () -> assertThat(registro).isNotNull(), () -> assertThat(registro.getNome()).isNotBlank(),
				() -> assertThat(registro.getNome()).isEqualTo("Teste"),
				() -> assertThat(registro.getRazaoSocial()).isEqualTo("Teste"),
				() -> assertThat(registro.getDocumento()).isEqualTo(documento),
				() -> assertThat(registro.getTipoPessoa()).isEqualTo(TipoPessoa.JURIDICA),

				() -> assertThat(registro.getEndereco()).isNotNull(),
				() -> assertThat(registro.getEndereco().getBairro()).isNotNull(),
				() -> assertThat(registro.getEndereco().getCidade()).isNotNull(),

				() -> assertThat(registro.getEndereco().getCep()).isEqualTo("09980200"),
				() -> assertThat(registro.getEndereco().getNumero()).isEqualTo("1"),
				() -> assertThat(registro.getEndereco().getLogradouro()).isEqualTo("Rua Teste"),
				() -> assertThat(registro.getEndereco().getComplemento()).isEqualTo("COMPLEMENTO"));
	}

	@Order(2)
	@Test
	void deve_retornar_erro_campos_nulos() {
		Exception thrown = catchException(() -> {
			empresaService.save(new Empresa());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates not-null constraint");
	}

	@Order(2)
	@Test
	void deve_retornar_erro_duplicidade() {
		Exception thrown = catchException(() -> {
			empresaService.save(getEmpresa().toBuilder().documento("335921198771").build());
			empresaService.save(getEmpresa().toBuilder().documento("335921198771").build());
		});
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageContaining("violates unique constraint");
	}

	@Order(2)
	@Test
	void deve_criar_retornar_registro() throws Exception {
		var empresa = getEmpresa().toBuilder().documento("335921198773").build();
		empresa = empresaService.save(empresa);
		var registro = empresaService.findByCodigo(empresa.getCodigo());
		assertThat(registro).isNotNull();
	}

	@Order(2)
	@Test
	void deve_retornar_registro_nulo() {
		Exception thrown = catchException(() -> {
			empresaService.findByCodigo(0L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");
	}

	@Order(2)
	@Test
	void deve_criar_atualizar_registro() throws Exception {
		var empresa = getEmpresa().toBuilder().documento("335921198772").build();
		var registro = empresaService.save(empresa);
		assertThat(registro).isNotNull();
		registro.setNome("TESTE2");
		registro = empresaService.save(registro);
		assertThat(registro).isNotNull();
		assertThat(registro.getNome()).isEqualTo("TESTE2");
	}

	@Order(3)
	@Test
	void deve_remover_registro() {
		empresaService.deleteByCodigo(1L);
		Exception thrown = catchException(() -> {
			empresaService.findByCodigo(1L);
		});
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessageContaining("Not entity found");

	}

	@Order(3)
	@Test
	void deve_remover_registro_inexistente() {
		empresaService.deleteByCodigo(0L);
	}

	@Order(4)
	@Test
	void deve_criar_registro_dto() throws Exception {

	}

}
