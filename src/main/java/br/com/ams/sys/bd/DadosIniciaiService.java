package br.com.ams.sys.bd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
import br.com.ams.sys.service.CEPService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.EstadoService;
import br.com.ams.sys.service.UsuarioService;

@Service
public class DadosIniciaiService {
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

	@Autowired
	CEPService cepService;

	HashMap<Integer, String> docs = new HashMap<Integer, String>();

	public DadosIniciaiService() {
		docs.put(0, "27716634000100");
		docs.put(1, "21979945000177");
		docs.put(2, "22009814000120");
		docs.put(3, "21952489000171");
		docs.put(4, "19773094000160");
		docs.put(5, "21694883000157");
		docs.put(6, "21788790000191");
		docs.put(7, "21788769000196");
		docs.put(8, "13343626000142");
		docs.put(9, "04220791000116");
		docs.put(10, "09009472000180");
		docs.put(11, "10301427000183");
		docs.put(12, "06145784000122");
		docs.put(13, "01449976000109");
		docs.put(14, "05300183000184");
		docs.put(15, "56757388000124");
		docs.put(16, "45063195000157");
		docs.put(17, "73064438000107");
		docs.put(18, "71101489000136");
		docs.put(19, "01174133000147");
		docs.put(20, "46787008000140");
		docs.put(21, "00767130000154");
		docs.put(22, "59238220000109");
		docs.put(23, "49765266000114");
		docs.put(24, "55289672000150");
		docs.put(25, "56192958000186");
		docs.put(26, "05124209000180");
		docs.put(27, "02815001000110");
		docs.put(28, "04747348000106");
		docs.put(29, "49772825000113");
		docs.put(30, "69280865000155");
		docs.put(31, "61755005000119");
		docs.put(32, "61459772000180");
		docs.put(33, "50518794000158");
		docs.put(34, "67120139000140");
		docs.put(35, "04186968000105");
		docs.put(36, "71098990000190");
		docs.put(37, "66599226000169");
		docs.put(38, "58163700000187");
		docs.put(39, "68218759000189");
		docs.put(40, "02852571000180");
		docs.put(41, "03064022000103");
		docs.put(42, "96421151000192");
		docs.put(43, "04824770000100");
		docs.put(44, "04582293000113");
		docs.put(45, "52339132000146");
		docs.put(46, "02240615000110");
		docs.put(47, "04608247000146");
		docs.put(48, "03916009000135");
		docs.put(49, "49040892000143");
		docs.put(50, "72985872000167");
		docs.put(51, "49871734000135");
		docs.put(52, "02087873000109");
		docs.put(53, "00514221000188");
		docs.put(54, "02865415000153");
		docs.put(55, "46079372000318");
		docs.put(56, "48595219000107");
		docs.put(57, "44328482000189");
		docs.put(58, "03488189000100");
		docs.put(59, "67506865000103");
		docs.put(60, "48250278000143");
		docs.put(61, "46150256000186");
		docs.put(62, "00941850000194");
		docs.put(63, "46582938000168");
		docs.put(64, "51982189000104");
		docs.put(65, "58290602000100");
		docs.put(66, "51961688000107");
		docs.put(67, "02102710000158");
		docs.put(68, "39005491000107");
		docs.put(69, "68474311000126");
		docs.put(70, "64738198000198");
		docs.put(71, "02522265000185");
		docs.put(72, "45072063000191");
		docs.put(73, "03033326000103");
		docs.put(74, "00988777000106");
		docs.put(75, "03060222000198");
		docs.put(76, "05103533000112");
		docs.put(77, "74696246000186");
		docs.put(78, "03238192000167");
		docs.put(79, "02607572000169");
		docs.put(80, "00020002000142");
		docs.put(81, "52551926000179");
		docs.put(82, "02312674000157");
		docs.put(83, "04795839000114");
		docs.put(84, "04606532000128");
		docs.put(85, "04437089000109");
		docs.put(86, "43010842000137");
		docs.put(87, "54313374000196");
		docs.put(88, "57333973000160");
		docs.put(89, "03146478000112");
		docs.put(90, "52216629000177");
		docs.put(91, "64807498000181");
		docs.put(92, "02809049000115");
		docs.put(93, "67425447000183");
		docs.put(94, "52539582000182");
		docs.put(95, "66976960000108");
		docs.put(96, "63922827000172");
		docs.put(97, "03448377000104");
		docs.put(98, "01638609000153");
		docs.put(99, "62255401000140");
		docs.put(100, "44592194000137");
	}

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
			cidadeRepository.saveAll(listaCidade(estado).stream().limit(5).toList());
			return estado;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void registrarBairros() throws Exception {
		try {
			bairroRepository.saveAll(listaBairro().stream().limit(5).toList());
		} catch (Exception e) {
			throw e;
		}
	}

	public void registrarEstadoBairroCidade() throws Exception {
		registrarEstados();
		registrarBairros();
		registrarCidades();
	}

	public void iniciar() {
		try {

			registrarEstados();
			registrarBairros();
			registrarCidades();

			var cidade = cidadeRepository.getOne(1L);
			var bairro = bairroRepository.getOne(1L);

			var endereco = new Endereco("Rua Teste", "1", "09980200", null, cidade, bairro);

			var empresa = empresaService.save(Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
					.nome("Teste").documento("17689329000104").razaoSocial("Teste").build());

			var empresa2 = empresaService.save(Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
					.nome("Teste2").documento("28824938000145").razaoSocial("Teste2").build());

			for (int i = 0; i < 100; i++) {

				var cliente = new Cliente();
				cliente.setDocumento(docs.get(i));
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

				cliente = clienteService.save(cliente);
			}

			var cliente2 = new Cliente();
			cliente2.setDocumento("45827425003041");
			cliente2.setEmpresa(empresa);
			cliente2.setNome("Teste2");
			cliente2.setRazaoSocial("Teste2");
			cliente2.setTipoPessoa(TipoPessoa.JURIDICA);
			cliente2.setEndereco(endereco);
			cliente2 = clienteService.save(cliente2);

			var codigo = usuarioService
					.criar(new UsuarioCriarDto("teste@gmail.com", "123456", RoleName.ROLE_ADMINISTRATOR));
			var usuario = usuarioService.findByCodigo(codigo);
			usuario.setClientes(List.of(cliente2));
			usuario.setEmpresas(List.of(empresa, empresa2));
			usuarioService.save(usuario);

			empresaService.obterTodos(PageRequest.of(0, 10), "{\"codigo\": \"1\"}");

			// cepService.pesquisar("09980200");
			// cepService.pesquisar("09185410");

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
