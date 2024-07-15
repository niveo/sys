package br.com.ams.sys.bd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aspectj.weaver.ast.Literal;
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
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.enuns.RoleName;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.SegmentoClienteDto;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.repository.EstadoRepository;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CEPService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.EstadoService;
import br.com.ams.sys.service.SegmentoClienteService;
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
	private ClienteRepository clienteRepository;

	@Autowired
	private SegmentoClienteService segmentoClienteService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	CEPService cepService;

	@Transactional(propagation = Propagation.REQUIRED)
	private void registrarEstados() throws Exception {
		try {
			estadoRepository.saveAll(listarEstados());
			System.out.println("Estados importados...");
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private Estado registrarCidades() throws Exception {
		try {
			var estado = estadoRepository.findAll().stream().filter(f -> f.getSigla().equals("SP")).findFirst().get();
			cidadeRepository.saveAll(listaCidade(estado).stream().limit(5).toList());
			System.out.println("Cidades importados...");
			return estado;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void registrarBairros() throws Exception {
		try {
			bairroRepository.saveAll(listaBairro().stream().limit(5).toList());
			System.out.println("Bairro importados...");
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
					.nome("RAIMAR COMERCIAL E DISTRIBUIDORA LTDA").documento("61154720000104")
					.razaoSocial("RAIMAR COMERCIAL E DISTRIBUIDORA LTDA").build());

			var empresa2 = empresaService.save(Empresa.builder().endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
					.nome("MARIAH PRODUTOS DE BELEZA LTDA").documento("04220791000116")
					.razaoSocial("MARIAH PRODUTOS DE BELEZA LTDA").build());

			clienteRepository.saveAll(listaCliente(endereco, empresa));

			var segmento = segmentoClienteService.save(SegmentoCliente.builder().descricao("ALIMENTAR").empresa(empresa).build());
			segmentoClienteService.save(SegmentoCliente.builder().descricao("PERFUMARIA").empresa(empresa).build());
			segmentoClienteService.save(SegmentoCliente.builder().descricao("FARMACO").empresa(empresa).build());

			var cliente2 = new Cliente();
			cliente2.setDocumento("45827425003041");
			cliente2.setEmpresa(empresa);
			cliente2.setNome("Teste2");
			cliente2.setSegmento(segmento);
			cliente2.setRazaoSocial("Teste2");
			cliente2.setTipoPessoa(TipoPessoa.JURIDICA);
			cliente2.setEndereco(endereco);
			cliente2.setEnderecos(List.of(ClienteEndereco.builder().cliente(cliente2).endereco(endereco).build(),
					ClienteEndereco.builder().cliente(cliente2).endereco(endereco).build()));

			var contato = new ClienteContato();
			contato.setNome("TESTE");
			contato.setCargo("CARGO1");
			contato.setCliente(cliente2);
			contato.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
			contato.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));

			var contato2 = new ClienteContato();
			contato2.setNome("TESTE");
			contato2.setCargo("CARGO1");
			contato2.setCliente(cliente2);
			contato2.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
			contato2.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));
			cliente2.setContatos(List.of(contato, contato2));

			cliente2 = clienteService.save(cliente2);

			var codigo = usuarioService
					.criar(new UsuarioCriarDto("teste@gmail.com", "123456", RoleName.ROLE_ADMINISTRATOR));
			var usuario = usuarioService.findByCodigo(codigo);
			usuario.setClientes(List.of(cliente2));
			usuario.setEmpresas(List.of(empresa, empresa2));
			usuarioService.save(usuario);

			// empresaService.obterTodos(1, "{\"codigo\": \"1\"}");

			// cepService.pesquisar("09980200");
			// cepService.pesquisar("09185410");

			// cidadeService.obterTodos(0, "");

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

	private List<Cliente> listaCliente(Endereco endereco, Empresa empresa) {
		return List.of(
				Cliente.builder().nome("PERFUMARIA FLORIBELLA LTDA ME").razaoSocial("PERFUMARIA FLORIBELLA LTDA ME")
						.telefone("01139032870").email("").documento("56757388000124").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ULISSES A DOMINGUES E CIA LTDA").razaoSocial("ULISSES A DOMINGUES E CIA LTDA")
						.telefone("01334681781").email("notasfiscais@fielbarateiro.com.br").documento("71101489000136")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DISTRIBUIDORA KIREI COSMETICO E BAZAR LT")
						.razaoSocial("DISTRIBUIDORA KIREI COSMETICO E BAZAR LT").telefone("01136817699").email("")
						.documento("04747348000106").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("CAMPEAO FARMA LJ 04 - MUTINGA").razaoSocial("DROGARIA KM DEZOITO LTDA ME")
						.telefone("01136030241").email("").documento("61459772000180").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("AYAKO TAKARA COSMETICOS ME").razaoSocial("AYAKO TAKARA COSMETICOS ME")
						.telefone("01141892701").email("").documento("04186968000105").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("VALDOMIRO PRESTES ME").razaoSocial("VALDOMIRO PRESTES ME")
						.telefone("01136051356").email("").documento("66599226000169").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA JARDIM SANTISTA LTDA").razaoSocial("DROGARIA JARDIM SANTISTA LTDA")
						.telefone("01332998270").email("zuleide_reis@terra.com.br").documento("58163700000187")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA CIFARMA LTDA").razaoSocial("DROGARIA CIFARMA LTDA")
						.telefone("01146206784").email("").documento("52339132000146").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("COMERCIAL SOCHEUK LTDA").razaoSocial("COMERCIAL SOCHEUK LTDA")
						.telefone("01334671285").email("").documento("02865415000153").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA TURIBIO LTDA ME").razaoSocial("DROGARIA TURIBIO LTDA ME")
						.telefone("01136092586").email("").documento("46150256000186").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA REVAN LTDA ME").razaoSocial("DROGARIA REVAN LTDA ME")
						.telefone("01136081691").email("").documento("51982189000104").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA AZEVEDO MINHOTO LTDA").razaoSocial("DROGARIA AZEVEDO MINHOTO LTDA")
						.telefone("01136059383").email("").documento("02102710000158").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("CIDA SUPERMERCADO LTDA ME").razaoSocial("CIDA SUPERMERCADO LTDA ME")
						.telefone("01136080622").email("").documento("02522265000185").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ANTONIO MIRAMOTO E FILHO LTDA").razaoSocial("ANTONIO MIRAMOTO E FILHO LTDA")
						.telefone("01334649566").email("anasoares7@bol.com.br").documento("45072063000191")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ESDRAS HENRIQUE DA COSTA BARUERI EPP")
						.razaoSocial("ESDRAS HENRIQUE DA COSTA BARUERI EPP").telefone("01141941996")
						.email("gilzapinheiro@uau.com.br").documento("74696246000186").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPERMERCADO SAN LTDA").razaoSocial("SUPERMERCADO SAN LTDA")
						.telefone("1148004010").email("compras3@redesa.com.br").documento("52216629000177")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("LUIZ FERNANDO DA SILVA SUPERMERCADO EPP")
						.razaoSocial("LUIZ FERNANDO DA SILVA SUPERMERCADO EPP").telefone("01141981717")
						.email("lfs.silva@uol.com.br").documento("64807498000181").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PEDRO DA SILVA GUARUJA EPP").razaoSocial("PEDRO DA SILVA GUARUJA EPP")
						.telefone("01333524067").email("").documento("62255401000140").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("COMERCIAL LAM SHUI LING LTDA").razaoSocial("COMERCIAL LAM SHUI LING LTDA")
						.telefone("01332345526").email("").documento("66683327000113").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("FARMADROGA JABAQUARA LTDA").razaoSocial("FARMADROGA JABAQUARA LTDA")
						.telefone("01332332249").email("").documento("58139742000182").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("COMERCIAL HILLWEGG LTDA").razaoSocial("COMERCIAL HILLWEGG LTDA")
						.telefone("01333611212").email("").documento("74214206000150").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("COMERCIAL FRAGA SOCHEUK LTDA").razaoSocial("COMERCIAL FRAGA SOCHEUK LTDA")
						.telefone("01332846815").email("perfumariaflorence@gmail.com").documento("03276518000140")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("COMERCIAL CHAU KAN LTDA").razaoSocial("COMERCIAL CHAU KAN LTDA")
						.telefone("01332191722").email("").documento("00003022000105").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA E PERFUMARIA MIRO DOIS BARUERI")
						.razaoSocial("DROGARIA E PERFUMARIA MIRO DOIS BARUERI").telefone("01141942675")
						.email("miro.dois@gmail.com").documento("66036898000166").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGAJAN LTDA EPP").razaoSocial("DROGAJAN LTDA EPP").telefone("01147075389")
						.email("").documento("67868877000170").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MERCADO SUPORTE LTDA").razaoSocial("MERCADO SUPORTE LTDA")
						.telefone("01136213080").email("mercsuporte@hotmail.com").documento("05203706000174")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PASCALE E PETRELLI LTDA ME").razaoSocial("PASCALE E PETRELLI LTDA ME")
						.telefone("01333838325").email("nfeloja1@smsuperx.com.br").documento("74648197000106")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGAVINA DROGARIA LTDA").razaoSocial("DROGAVINA DROGARIA LTDA")
						.telefone("01155234308").email("cpd_farmalogos@uol.com.br").documento("04211813000181")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA NOVA DELHI LTDA ME").razaoSocial("DROGARIA NOVA DELHI LTDA ME")
						.telefone("01156611556").email("").documento("74426180000104").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA E PERFUMARIA NOVA CHAMA LJ 04")
						.razaoSocial("DROGARIA E PERFUMARIA NOVA CHAMA LT L 04").telefone("01156662954")
						.email("nfe@drogaviva.com.br").documento("04116437000146").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA NOBREGA LTDA ME").razaoSocial("DROGARIA NOBREGA LTDA ME")
						.telefone("01167414786").email("").documento("53014353000107").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ACOUGUE E MERCEARIA AVENIDA LTDA ME")
						.razaoSocial("ACOUGUE E MERCEARIA AVENIDA LTDA ME").telefone("01139363085").email("")
						.documento("58851734000164").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("DROGARIA DROGAGATTI LTDA ME").razaoSocial("DROGARIA DROGAGATTI LTDA ME")
						.telefone("01155128053").email("").documento("65570921000135").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGA CINTHIA REGINAI LTDA ME").razaoSocial("DROGA CINTHIA REGINAI LTDA ME")
						.telefone("01158510895").email("droga_cinthia@hotmail.com").documento("52843679000184")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA SANDY LTDA ME").razaoSocial("DROGARIA SANDY LTDA ME")
						.telefone("01155151103").email("").documento("02493890000146").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MARIA IVONE MARTINS ME").razaoSocial("MARIA IVONE MARTINS ME")
						.telefone("01155129778").email("").documento("00381524000170").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA ARARIBA LTDA ME").razaoSocial("DROGARIA ARARIBA LTDA ME")
						.telefone("01155120735").email("").documento("56227614000165").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA FARMA LOPES LTDA ME").razaoSocial("DROGARIA FARMA LOPES LTDA ME")
						.telefone("01155170379").email("").documento("04736629000155").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA TINGUARA LTDA ME").razaoSocial("DROGARIA TINGUARA LTDA ME")
						.telefone("01125570616").email("tinguara@terra.com.br").documento("64853237000106")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("FARMA FALL LTDA ME").razaoSocial("FARMA FALL LTDA ME").telefone("1120352200")
						.email("").documento("58834409000193").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA LUCAS LTDA ME").razaoSocial("DROGARIA LUCAS LTDA ME")
						.telefone("01169446570").email("").documento("72683287000102").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("FARMA LESTE LAGEADO LTDA ME").razaoSocial("FARMA LESTE LAGEADO LTDA ME")
						.telefone("01149307000").email("").documento("03380313000100").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA DROGABAY JARDIM DAS OLIVEIRAS L")
						.razaoSocial("DROGARIA DROGABAY JARDIM DAS OLIVEIRAS L").telefone("11951079592")
						.email("drogabay03@gmail.com").documento("66587288000150").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA FERREIRA SANTOS LTDA").razaoSocial("DROGARIA FERREIRA SANTOS LTDA")
						.telefone("1125579098").email("fiscal.ferreira@gmail.com").documento("57816167000142")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA UETI LTDA ME").razaoSocial("PERFUMARIA UETI LTDA ME")
						.telefone("01129619574").email("").documento("64516917000126").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGA NANCY EMILIA LTDA ME").razaoSocial("DROGA NANCY EMILIA LTDA ME")
						.telefone("01125617154").email("drognanciemilia@terra.com.br").documento("56918675000179")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA SITIO LTDA ME").razaoSocial("DROGARIA SITIO LTDA ME")
						.telefone("01125559982").email("").documento("00487385000163").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGA RENATO DE ITAQUERA LTDA").razaoSocial("DROGA RENATO DE ITAQUERA LTDA")
						.telefone("1155557462").email("ultraromero2@gmail.com").documento("49484991000114")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPERMERCADOS LOURENCINI LJ 01")
						.razaoSocial("LOURENCINI COMERCIO ALIMENTOS LTDA LJ 01").telefone("01145144300")
						.email("nfe01@lourencini.com.br").documento("56907868000124").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA NOVA CARIJOS LTDA ME").razaoSocial("DROGARIA NOVA CARIJOS LTDA ME")
						.telefone("01144539279").email("").documento("00005616000155").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA RANGEL LTDA ME").razaoSocial("DROGARIA RANGEL EIRELI ME")
						.telefone("01144255190").email("").documento("48206015000137").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA NOVA BARAO DE MAUA LTDA")
						.razaoSocial("DROGARIA NOVA BARAO DE MAUA LTDA").telefone("01145141150")
						.email("baraofarma@uol.com.br").documento("58852971000140").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("LUIZ PAULO CORTEZ ME").razaoSocial("LUIZ PAULO CORTEZ ME")
						.telefone("01145762891").email("").documento("53472882000154").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA CAZO VAREA EIRELI").razaoSocial("DROGARIA CAZO VAREA EIRELI")
						.telefone("01149785416").email("").documento("03665303000111").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPERMERCADO NEVADA LTDA").razaoSocial("SUPERMERCADO NEVADA LTDA")
						.telefone("01145162005").email("sup.nevada@hotmail.com").documento("45561537000169")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MERCADINHO IRMAOS DOMINGOS LTDA").razaoSocial("MERCADINHO IRMAOS DOMINGOS LTDA")
						.telefone("01145761577").email("sm.esperanca@uol.com.br").documento("52601549000135")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MERCADO E PADARIA J J A LTDA EPP")
						.razaoSocial("MERCADO E PADARIA J J A LTDA EPP").telefone("01145132678")
						.email("jjasupermercado@hotmail.com").documento("43073477000100").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ROSELI MARIA MORAIS ME").razaoSocial("ROSELI MARIA MORAIS ME")
						.telefone("01145184445").email("cardosoxml@bol.com.br").documento("01895328000186")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MINIMERCADO ITAPEVA LTDA ME").razaoSocial("MINIMERCADO ITAPEVA LTDA ME")
						.telefone("01145763347").email("").documento("01057467000130").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA PINHEIRO E COLARES LTDA ME")
						.razaoSocial("DROGARIA PINHEIRO E COLARES LTDA ME").telefone("1123029385").email("")
						.documento("74558776000168").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("DROGARIA SAO MIGUEL LTDA ME").razaoSocial("DROGARIA SAO MIGUEL LTDA ME")
						.telefone("01169171075").email("contato@novafarmacostabarros.com.br")
						.documento("61349700000180").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("DROGARIA SAO RAFAEL").razaoSocial("DROGARIA SAO RAFAEL LTDA EPP")
						.telefone("01129195469").email("srdrogaria@uol.com.br").documento("52070810000118")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("AIKO PERFUMARIA LTDA").razaoSocial("AIKO PERFUMARIA LTDA")
						.telefone("01127316142").email("aiko.massa@yahoo.com.br").documento("61788659000149")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA YOIYOI LTDA ME").razaoSocial("PERFUMARIA YOIYOI LTDA ME")
						.telefone("01127270394").email("tyoiyoi@gmail.com").documento("66566423000181").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGA MARISA LTDA ME").razaoSocial("DROGA MARISA LTDA ME")
						.telefone("01139219020").email("drogamarisa@gmail.com").documento("47389358000111")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA FLOR DO PERI LTDA ME").razaoSocial("PERFUMARIA FLOR DO PERI LTDA ME")
						.telefone("01122083120").email("flordoperi@hotmail.com").documento("00723598000147")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MARS UTILIDADES DOMESTICAS LTDA ME")
						.razaoSocial("MARS UTILIDADES DOMESTICAS LTDA ME").telefone("01132662381").email("")
						.documento("52337532000112").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("MERCADINHO LUVIZOTTO LTDA ME").razaoSocial("MERCADINHO LUVIZOTTO LTDA ME")
						.telefone("01139156946").email("").documento("49661473000129").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA CAVALHERI LTDA").razaoSocial("DROGARIA CAVALHERI LTDA")
						.telefone("01169546445").email("").documento("49515943000146").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGA TRAMANDAY LTDA").razaoSocial("DROGA TRAMANDAY LTDA ME")
						.telefone("01129172977").email("").documento("61524823000100").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MERCADINHO CARINHOSO LTDA").razaoSocial("MERCADINHO CARINHOSO LTDA")
						.telefone("01129541208").email("smcarinhoso.nfe@terra.com.br").documento("46411625000147")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA NEWFACE LTDA ME").razaoSocial("PERFUMARIA NEWFACE LTDA ME")
						.telefone("01129510982").email("").documento("55173934000117").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA ITAMONTE LT").razaoSocial("DROGARIA ITAMONTE LTDA  ME")
						.telefone("01162449300").email("").documento("54518220000130").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA HIKARI LTDA").razaoSocial("DROGARIA HIKARI LTDA.")
						.telefone("01136020804").email("").documento("68933787000188").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA AKALIN LTDA ME").razaoSocial("PERFUMARIA AKALIN LTDA ME")
						.telefone("01136867080").email("").documento("67818179000160").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPER MERCADO CASTANHA LT").razaoSocial("SUPER MERCADO CASTANHA LT")
						.telefone("01136221447").email("financeiro@fcastanha.com.br").documento("63082721000108")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPER MERCADO ECONOMICO LTDA").razaoSocial("SUPER MERCADO ECONOMICO LTDA")
						.telefone("01136860411").email("sup.economico@uol.com.br").documento("48890875000132")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPERMERCADO FEDERZONI LTDA LJ 02")
						.razaoSocial("SUPERMERCADO FEDERZONI LTDA LJ 02").telefone("01121416888").email("")
						.documento("56251283000107").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("SUPERMERCADO RODRIGUES LTDA").razaoSocial("SUPERMERCADO RODRIGUES LTDA")
						.telefone("01136863331").email("superrodri@bol.com.br").documento("48892582000194")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA MELISSA LTDA ME").razaoSocial("PERFUMARIA MELISSA LTDA ME")
						.telefone("1129575609").email("").documento("04152673000118").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA ADMA LTDA ME").razaoSocial("DROGARIA ADMA LTDA ME")
						.telefone("01138577300").email("").documento("65465288000115").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("GOYA TIVI COSMETICOS LTDA").razaoSocial("GOYA TIVI COSMETICOS LTDA")
						.telefone("01127345070").email("goyaperfumariaelaine@gmail.com").documento("03145183000121")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("HELENA ARTIGOS PARA PRESENTE LTDA ME")
						.razaoSocial("HELENA ARTIGOS PARA PRESENTE LTDA ME").telefone("01129496649").email("")
						.documento("54177787000190").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("DROGARIA CALUX LTDA").razaoSocial("DROGARIA CALUX LTDA").telefone("01143991234")
						.email("drogariacalux@hotmail.com").documento("49253123000123").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA SABIN LTDA EPP").razaoSocial("DROGARIA SABIN LTDA EPP")
						.telefone("01166515722").email("farmaisvilamatilde@yahoo.com.br").documento("01711296000111")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("SUPERMERCADO COSTA AZUL DE JUQUEY LTDA E")
						.razaoSocial("SUPERMERCADO COSTA AZUL DE JUQUEY LTDA E").telefone("12974099783")
						.email("compras.fmcostaazul@gmail.com").documento("66752692000132").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("NELSON YOSHIZIMI E CIA LTDA ME").razaoSocial("NELSON YOSHIZIMI E CIA LTDA ME")
						.telefone("01162011592").email("drogarialoreto@hotmail.com").documento("61231858000151")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("ALAIDE AMERICO GOMES ME").razaoSocial("ALAIDE AMERICO GOMES ME")
						.telefone("1122804703").email("nfe.drogaleste15@gmail.com").documento("03918043000149")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("DROGARIA PIRAMIDE DO PAN LTDA EPP")
						.razaoSocial("DROGARIA PIRAMIDE DO PAN LTDA EPP").telefone("01139283344").email("")
						.documento("04675521000108").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("HEXAFARMA DROGARIA LTDA ME").razaoSocial("HEXAFARMA DROGARIA LTDA ME")
						.telefone("01144537066").email("hexafarmadrogarias@hotmail.com").documento("49381767000105")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("RECANTO STA JULIA COM CONVENIENCIA LTDA")
						.razaoSocial("RECANTO STA JULIA COM CONVENIENCIA LTDA").telefone("01146693381")
						.email("cabralsupermecado@uol.com.br").documento("04812819000104").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("VENCESLAU DE AGUIAR NOVAIS JUNIOR ME")
						.razaoSocial("VENCESLAU DE AGUIAR NOVAIS JUNIOR ME").telefone("01145161925").email("")
						.documento("55910657000188").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("PADRON E PAULA LTDA").razaoSocial("PADRON E PAULA LTDA").telefone("01129172782")
						.email("").documento("43556158000155").empresa(empresa).endereco(endereco)
						.tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("FARMACIA MORAES LTDA EPP").razaoSocial("FARMACIA MORAES LTDA EPP")
						.telefone("01122030849").email("sandra@farmaistremembe.com.br").documento("61293296000170")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("MATSUTANIS COSMETICOS LTDA EPP").razaoSocial("MATSUTANIS COSMETICOS LTDA EPP")
						.telefone("01333174362").email("").documento("00805656000181").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("VERA LUCIA DE LIMA BRAZ - COSMETICOS")
						.razaoSocial("VERA LUCIA DE LIMA BRAZ - COSMETICOS").telefone("01333426159").email("")
						.documento("02478117000100").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("M A B COMERCIO DE COSMETICOS LTDA")
						.razaoSocial("M A B COMERCIO DE COSMETICOS LTDA").telefone("01333526252").email("")
						.documento("03782368000147").empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA)
						.build(),
				Cliente.builder().nome("SILVIO YEIZO TAKARA PERFUMARIA").razaoSocial("SILVIO YEIZO TAKARA PERFUMARIA")
						.telefone("01145236325").email("silviotakara@uol.com.br").documento("47118708000105")
						.empresa(empresa).endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("BOUTIQUE JALU LTDA ME").razaoSocial("BOUTIQUE JALU LTDA ME")
						.telefone("01122935229").email("").documento("52715919000165").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("WALQUIRIA BIAGIOLI CHILOTTI ME").razaoSocial("WALQUIRIA BIAGIOLI CHILOTTI ME")
						.telefone("01169676540").email("").documento("04951483000160").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build(),
				Cliente.builder().nome("PERFUMARIA KOHATSU E YOGI LTDA ME")
						.razaoSocial("PERFUMARIA KOHATSU E YOGI LTDA ME").telefone("01127213224")
						.email("wilsonkohatsu@hotmail.com").documento("00891773000105").empresa(empresa)
						.endereco(endereco).tipoPessoa(TipoPessoa.JURIDICA).build());
	}
}
