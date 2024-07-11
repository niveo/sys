package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.ams.sys.bd.DadosIniciaiService;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Role;
import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.enuns.RoleName;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.UsuarioCriarDto;

@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpresaUsuarioTest {

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private UsuarioService usuarioService;

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
	void deve_manter_usuario_apos_remover_empresa() throws Exception {
		var empresa = getEmpresa();
		empresa = empresaService.save(empresa);
		assertThat(empresa).isNotNull();
		var usuario = Usuario.builder().senha("123456").email("teste@gmail.com").nome("teste")
				.empresas(List.of(empresa)).roles(List.of(Role.builder().name(RoleName.ROLE_ADMINISTRATOR).build()))
				.build();
		usuario = usuarioService.save(usuario);
		assertThat(usuario).isNotNull();

		empresaService.deleteByCodigo(empresa.getCodigo());

		usuario = usuarioService.findByCodigo(usuario.getCodigo());
		assertThat(usuario).isNotNull();
	}

	@Order(3)
	@Test
	void deve_manter_empresa_apos_remover_usuario() throws Exception {
		var empresa = getEmpresa().toBuilder().documento("123").build();
		empresa = empresaService.save(empresa);
		assertThat(empresa).isNotNull();

		var usuario = Usuario.builder().senha("123456").email("teste2@gmail.com").nome("teste")
				.empresas(List.of(empresa)).roles(List.of(Role.builder().name(RoleName.ROLE_ADMINISTRATOR).build()))
				.build();
		usuario = usuarioService.save(usuario);
		assertThat(usuario).isNotNull();

		usuarioService.deleteByCodigo(usuario.getCodigo());

		empresa = empresaService.findByCodigo(empresa.getCodigo());
		assertThat(empresa).isNotNull();
	}

}
