package br.com.ams.sys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.enuns.RoleName;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.repository.EstadoRepository;
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
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional(propagation = Propagation.REQUIRED)
	private void registrarEstados() throws Exception {
		try {
			estadoRepository.saveAll(listarEstados());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private Estado registrarCidades() throws Exception {
		try {
			var estado = estadoRepository.findAll().stream().filter(f -> f.getSigla().equals("SP")).findFirst().get();
			cidadeRepository.saveAll(listaCidade(estado));
			return estado;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void registrarBairros() throws Exception {
		try {
			bairroRepository.saveAll(listaBairro());
		} catch (Exception e) {
			throw e;
		}
	}

	private void name() {
		try {

			registrarEstados();
			registrarBairros();
			registrarCidades();

			var cidade = cidadeRepository.getOne(1L);
			var bairro = bairroRepository.getOne(1L);

			var endereco = new Endereco("Rua Teste", "1", "09980200", null, cidade, bairro);

			var empresa = empresaService.save(Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
					.nome("Teste").documento("33592119877").razaoSocial("Teste").build());

			var empresa2 = empresaService.save(Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
					.nome("Teste2").documento("335921198772").razaoSocial("Teste2").build());

			for (int i = 0; i < 100; i++) {

				var cliente = new Cliente();
				cliente.setDocumento("111111" + i);
				cliente.setEmpresa(empresa);
				cliente.setNome("Teste");
				cliente.setRazaoSocial("Teste");
				cliente.setTipoPessoa(TipoPessoa.JURIDICA);
				cliente.setEndereco(endereco);

				cliente.setEnderecos(List.of(ClienteEndereco.builder().cliente(cliente).endereco(endereco).build(),
						ClienteEndereco.builder().cliente(cliente).endereco(endereco).build()));

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

				// cliente = clienteService.save(cliente);
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

	private List<Estado> listarEstados() throws Exception {
		var tree = objectMapper.readTree(new File("src/main/resources/estados.json"));
		var it = tree.elements();
		var lista = new ArrayList<Estado>();
		while (it.hasNext()) {
			var ob = it.next();
			String nome = ob.get("nome").asText();
			String sigla = ob.get("sigla").asText();
			lista.add(Estado.builder().descricao(nome).sigla(sigla).build());
		}
		return lista;
	}

	private List<Bairro> listaBairro() throws Exception {
		var tree = objectMapper.readTree(new File("src/main/resources/bairros.json"));
		var it = tree.elements();
		var lista = new ArrayList<Bairro>();
		while (it.hasNext()) {
			String nome = it.next().get("nome").asText();
			lista.add(Bairro.builder().descricao(nome).build());
		}
		return lista;
	}

	private List<Cidade> listaCidade(Estado estado) throws Exception {
		var tree = objectMapper.readTree(new File("src/main/resources/cidades.json"));
		var it = tree.elements();
		var lista = new ArrayList<Cidade>();
		while (it.hasNext()) {
			String nome = it.next().get("nome").asText();
			lista.add(Cidade.builder().descricao(nome).estado(estado).build());
		}
		return lista;
	}
}
