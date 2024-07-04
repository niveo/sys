package br.com.ams.sys;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.enuns.RoleName;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.EstadoService;
import br.com.ams.sys.service.UsuarioService;

@SpringBootApplication
public class SysApplication implements CommandLineRunner {

	@Value("${sistema.time-zone}")
	private String timeZone;

	public static void main(String[] args) {
		SpringApplication.run(SysApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
		this.name();
	}

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

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	PasswordEncoder passwordEncoder;

	private void name() {
		try {
			var estado = estadoService.salvar(new Estado("SAO PAULO", "SP"));

			var bairro = new Bairro();
			bairro.setDescricao("TESTE");
			bairro = bairroService.salvar(bairro);

			var cidade = new Cidade();
			cidade.setDescricao("TESTE");
			cidade.setEstado(estado);
			cidade = cidadeService.salvar(cidade);

			var endereco = new Endereco("Rua Teste", "1", "09980200", null, cidade, bairro);

			var empresa = new Empresa();
			empresa.setEndereco(endereco);
			empresa.setTipoPessoa(TipoPessoa.JURIDICA);
			empresa.setNome("Teste");
			empresa.setDocumento("33592119877");
			empresa.setRazaoSocial("Teste");
			empresa = empresaService.save(empresa);

			var empresa2 = new Empresa();
			empresa2.setEndereco(endereco);
			empresa2.setTipoPessoa(TipoPessoa.JURIDICA);
			empresa2.setNome("Teste2");
			empresa2.setEmail("sandnine@gmail.com");
			empresa2.setDocumento("335921198772");
			empresa2.setRazaoSocial("Teste2");
			empresa2 = empresaService.save(empresa2);

			var empresa3 = new Empresa();
			empresa3.setEndereco(endereco);
			empresa3.setTipoPessoa(TipoPessoa.JURIDICA);
			empresa3.setNome("Teste2");
			empresa3.setDocumento("335921198772X");
			empresa3.setEmail("sandnine@gmail.com");
			empresa3.setTelefone("11950514363");
			empresa3.setRazaoSocial("Teste2");
			empresa3 = empresaService.save(empresa3);

			for (int i = 0; i < 100; i++) {

				var cliente = new Cliente();
				cliente.setDocumento("111111" + i);
				cliente.setEmpresa(empresa);
				cliente.setNome("Teste");
				cliente.setRazaoSocial("Teste");
				cliente.setTipoPessoa(TipoPessoa.JURIDICA);
				cliente.setEndereco(endereco);

				cliente.setEnderecos(
						List.of(new ClienteEndereco(cliente, endereco), new ClienteEndereco(cliente, endereco)));

				var contato = new ClienteContato();
				contato.setNome("TESTE");
				contato.setCargo("CARGO1");
				contato.setCliente(cliente);
				contato.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
				contato.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));

				var contato2 = new ClienteContato();
				contato2.setNome("TESTE");
				contato2.setCargo("CARGO1");
				contato2.setCliente(cliente);
				contato2.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
				contato2.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));
				cliente.setContatos(List.of(contato, contato2));

				cliente = clienteService.save(cliente);
			}

			var cliente2 = new Cliente();
			cliente2.setDocumento("335921198772");
			cliente2.setEmpresa(empresa);
			cliente2.setNome("Teste2");
			cliente2.setRazaoSocial("Teste2");
			cliente2.setTipoPessoa(TipoPessoa.JURIDICA);
			cliente2.setEndereco(endereco);
			cliente2 = clienteService.save(cliente2);

			usuarioService.criar(new UsuarioCriarDto("teste@gmail.com", "123456", RoleName.ROLE_ADMINISTRATOR));

			var usuario = usuarioService.findByCodigo(1L);
			usuario.setClientes(List.of(cliente2));
			usuario.setEmpresas(List.of(empresa, empresa2));

			usuarioService.save(usuario);

			empresaService.obterTodos(PageRequest.of(0, 10), "{\"codigo\": \"1\"}");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
