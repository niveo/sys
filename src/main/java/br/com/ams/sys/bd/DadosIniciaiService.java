package br.com.ams.sys.bd;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import br.com.ams.sys.entity.ConfiguracaoPesquisa;
import br.com.ams.sys.entity.ConfiguracaoPesquisaFiltro;
import br.com.ams.sys.entity.ConfiguracaoPesquisaGrade;
import br.com.ams.sys.entity.ConfiguracaoPesquisaGrade;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.TabelaPreco;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.entity.TabelaPrecoProduto;
import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.enuns.RoleName;
import br.com.ams.sys.enuns.TipoPessoa;
import br.com.ams.sys.records.SegmentoClienteDto;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.repository.EstadoRepository;
import br.com.ams.sys.repository.ProdutoRepository;
import br.com.ams.sys.repository.UnidadeRepository;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CEPService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.ConfiguracaoPesquisaService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.EstadoService;
import br.com.ams.sys.service.ProdutoService;
import br.com.ams.sys.service.SegmentoClienteService;
import br.com.ams.sys.service.TabelaPrecoLancamentoService;
import br.com.ams.sys.service.TabelaPrecoProdutoService;
import br.com.ams.sys.service.TabelaPrecoService;
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
	private TabelaPrecoService tabelaPrecoService;

	@Autowired
	private TabelaPrecoLancamentoService tabelaPrecoLancamentoService;

	@Autowired
	private TabelaPrecoProdutoService tabelaPrecoProdutoService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	CEPService cepService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ConfiguracaoPesquisaService configuracaoViewService;

	@Autowired
	private UnidadeRepository unidadeRepository;

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

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/produtos").descricao("Produtos")
					.grade(ConfiguracaoPesquisaGrade.builder().caminhoEditar("produtos_detalhe").listaItem("LT_01")
							.caminhoInserir("produtos_cadastrar").build())
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição").posicao(1)
									.campo("descricao").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Referência").posicao(2)
									.campo("referencia").requerido(false).build()))
					.build());

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/empresas").descricao("Empresas")
					.grade(ConfiguracaoPesquisaGrade.builder().caminhoEditar("empresas_detalhe").listaItem("LT_02")
							.caminhoInserir("empresas_cadastrar").build())
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Nome").posicao(1)
									.campo("nome").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Razão Social")
									.posicao(2).campo("razaoSocial").requerido(false).build(),

							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("CNPJ / CPF").posicao(3)
									.campo("documento").requerido(false).build()))
					.build());

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/clientes").descricao("Clientes")
					.grade(ConfiguracaoPesquisaGrade.builder().caminhoEditar("clientes_detalhe").listaItem("LT_03")
							.caminhoInserir("clientes_cadastrar").build())
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Nome").posicao(1)
									.campo("nome").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Razão Social")
									.posicao(2).campo("razaoSocial").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("CNPJ / CPF").posicao(3)
									.campo("documento").requerido(false).build()))
					.build());

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/tabelaprecos").descricao("Tabelas")
					.grade(ConfiguracaoPesquisaGrade.builder().caminhoEditar("tabelaprecos_detalhe").listaItem("LT_04")
							.caminhoInserir("tabelaprecos_cadastrar").build())
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição").posicao(1)
									.campo("descricao").requerido(false).build()))
					.build());

			configuracaoViewService.save(
					ConfiguracaoPesquisa.builder().caminho("/bairros").descricao("Bairro").componenteCadastro("CA_01")
							.filtros(List.of(
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number")
											.descricao("Código").posicao(0).campo("codigo").requerido(false).build(),
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição")
											.posicao(1).campo("descricao").requerido(false).build()))
							.build());

			configuracaoViewService.save(
					ConfiguracaoPesquisa.builder().caminho("/cidades").descricao("Cidade").componenteCadastro("CA_02")
							.filtros(List.of(
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number")
											.descricao("Código").posicao(0).campo("codigo").requerido(false).build(),
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição")
											.posicao(1).campo("descricao").requerido(false).build(),
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Estado")
											.posicao(1).campo("estado").requerido(false).build()))
							.build());

			configuracaoViewService.save(
					ConfiguracaoPesquisa.builder().caminho("/unidades").descricao("Unidade").componenteCadastro("CA_03")
							.filtros(List.of(
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number")
											.descricao("Código").posicao(0).campo("codigo").requerido(false).build(),
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição")
											.posicao(1).campo("descricao").requerido(false).build(),
									ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Sigla")
											.posicao(1).campo("sigla").requerido(false).build()))
							.build());

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/redesclientes").descricao("Rede")
					.componenteCadastro("CA_04")
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição").posicao(1)
									.campo("descricao").requerido(false).build()))
					.build());

			configuracaoViewService.save(ConfiguracaoPesquisa.builder().caminho("/segmentosclientes")
					.descricao("Segmento").componenteCadastro("CA_05")
					.filtros(List.of(
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").tipo("number").descricao("Código")
									.posicao(0).campo("codigo").requerido(false).build(),
							ConfiguracaoPesquisaFiltro.builder().componente("CP_01").descricao("Descrição").posicao(1)
									.campo("descricao").requerido(false).build()))
					.build());

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

			unidadeRepository
					.saveAll(List.of(Unidade.builder().empresa(empresa).descricao("UNIDADE").sigla("UN").build(),
							Unidade.builder().empresa(empresa).descricao("CAIXA").sigla("CX").build())

					);

			tabelaPrecoService.save(TabelaPreco.builder().descricao("REDE 10%").ativo(true).empresa(empresa).build());

			var tabelaPreco = tabelaPrecoService
					.save(TabelaPreco.builder().descricao("PRINCIPAL").ativo(true).empresa(empresa).build());

			clienteRepository.saveAll(listaCliente(endereco, empresa, tabelaPreco));

			var produtos = produtoRepository.saveAll(listaProduto(empresa));

			var lancamento = tabelaPrecoLancamentoService.save(TabelaPrecoLancamento.builder().tabela(tabelaPreco)
					.vigor(LocalDateTime.now(ZoneId.systemDefault())).build());

			tabelaPrecoProdutoService.save(TabelaPrecoProduto.builder().lancamento(lancamento).produto(produtos.get(0))
					.valor(new BigDecimal(5.55)).build());

			var segmento = segmentoClienteService
					.save(SegmentoCliente.builder().descricao("ALIMENTAR").empresa(empresa).build());
			segmentoClienteService.save(SegmentoCliente.builder().descricao("PERFUMARIA").empresa(empresa).build());
			segmentoClienteService.save(SegmentoCliente.builder().descricao("FARMACO").empresa(empresa).build());

			var cliente2 = new Cliente();
			cliente2.setDocumento("45827425003041");
			cliente2.setEmpresa(empresa);
			cliente2.setNome("Teste2");
			cliente2.setSegmento(segmento);
			cliente2.setRazaoSocial("Teste2");
			cliente2.setTabela(tabelaPreco);
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

	private List<Cliente> listaCliente(Endereco endereco, Empresa empresa, TabelaPreco tabela) throws Exception {
		var tree = objectMapper.readTree(new File("src/main/resources/parceiros.json"));
		var it = tree.elements();
		var lista = new ArrayList<Cliente>();
		while (it.hasNext()) {
			var n = it.next();
			String nome = n.get("nome").asText();
			String razaoSocial = n.get("razaoSocial").asText();
			String documento = n.get("documento").asText();
			String telefone = n.get("telefone").asText();
			String email = n.get("email").asText();
			lista.add(Cliente.builder().nome(nome).tipoPessoa(TipoPessoa.JURIDICA).razaoSocial(razaoSocial)
					.documento(documento).telefone(telefone).email(email).endereco(endereco).empresa(empresa)
					.tabela(tabela).build());
		}
		return lista;
	}

	private List<Produto> listaProduto(Empresa empresa) throws Exception {
		var tree = objectMapper.readTree(new File("src/main/resources/produtos.json"));
		var it = tree.elements();
		var lista = new ArrayList<Produto>();
		while (it.hasNext()) {
			var n = it.next();
			String codigoExterno = n.get("codigoExterno").asText();
			String descricao = n.get("descricao").asText();
			String referencia = n.get("referencia").asText();
			Boolean ativo = n.get("ativo").asBoolean();
			String complemento = n.get("complemento").asText();
			lista.add(Produto.builder().empresa(empresa).codigoExterno(codigoExterno).descricao(descricao)
					.referencia(referencia).ativo(ativo).complemento(complemento).build());
		}
		return lista;
	}
}
