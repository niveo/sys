package br.com.ams.sys.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.Contato;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.enuns.TipoPessoa;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ClienteServiceTest {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Test
	void should_criar_cliente() throws Exception {

		var estado = estadoService.salvar(new Estado("SAO PAULO", "SP"));

		assertThat(estado).isNotNull();

		var bairro = new Bairro();
		bairro.setDescricao("TESTE");
		bairro = bairroService.salvar(bairro);

		assertThat(bairro).isNotNull();

		var cidade = new Cidade();
		cidade.setDescricao("TESTE");
		cidade.setEstado(estado);
		cidade = cidadeService.salvar(cidade);

		assertThat(cidade).isNotNull();

		var endereco = new Endereco("Rua Teste", "1", "09980200", null, cidade, bairro);

		assertThat(endereco).isNotNull();

		var empresa = new Empresa();
		empresa.setEndereco(endereco);
		empresa.setTipoPessoa(TipoPessoa.JURIDICA);
		empresa.setNome("Teste");
		empresa.setDocumento("33592119877");
		empresa.setRazaoSocial("Teste");
		empresa = empresaService.salvar(empresa);

		assertThat(empresa).isNotNull();

		var cliente = new Cliente();
		cliente.setDocumento("33592119877");
		cliente.setEmpresa(empresa);
		cliente.setNome("Teste");
		cliente.setRazaoSocial("Teste");
		cliente.setTipoPessoa(TipoPessoa.JURIDICA);
		cliente.setEndereco(endereco);

		var contato = new Contato();
		contato.setNome("TESTE1");
		contato.setCargo("CARGO1");
		contato.setTelefones(new HashSet<String>(List.of("11950514363", "12950514363")));
		contato.setEmails(new HashSet<String>(List.of("sandnine1@gmail.com", "sandnine2@gmail.com")));

		var contato2 = new Contato();
		contato2.setNome("TESTE2");
		contato2.setCargo("CARGO2");
		contato2.setTelefones(new HashSet<String>(List.of("11950514363", "12950514363")));
		contato2.setEmails(new HashSet<String>(List.of("sandnine1@gmail.com", "sandnine2@gmail.com")));

		cliente.setContatos(new HashSet<Contato>(List.of(contato, contato2)));

		cliente = clienteService.salvar(cliente);

		assertThat(cliente).isNotNull();

		assertThat(cliente.getContatos()).isNotNull();

		assertThat(cliente.getContatos()).hasSize(2);

	}
}
