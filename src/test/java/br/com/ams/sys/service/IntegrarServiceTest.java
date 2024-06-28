package br.com.ams.sys.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.Contato;
import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Endereco;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.enuns.TipoPessoa;

public class IntegrarServiceTest {

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
			empresa = empresaService.salvar(empresa);

			var empresa2 = new Empresa();
			empresa2.setEndereco(endereco);
			empresa2.setTipoPessoa(TipoPessoa.JURIDICA);
			empresa2.setNome("Teste2");
			empresa2.setDocumento("335921198772");
			empresa2.setRazaoSocial("Teste2");
			empresa2 = empresaService.salvar(empresa2);

			var cliente = new Cliente();
			cliente.setDocumento("33592119877");
			cliente.setEmpresa(empresa);
			cliente.setNome("Teste");
			cliente.setRazaoSocial("Teste");
			cliente.setTipoPessoa(TipoPessoa.JURIDICA);
			cliente.setEndereco(endereco);

			var contato = new Contato();
			contato.setNome("TESTE");
			contato.setCargo("CARGO1");
			contato.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
			contato.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));

			var contato2 = new Contato();
			contato2.setNome("TESTE");
			contato2.setCargo("CARGO1");
			contato2.setTelefones(new HashSet<String>(List.of("11950514363", "11950514363")));
			contato2.setEmails(new HashSet<String>(List.of("sandnine@gmail.com", "sandnine@gmail.com")));

			cliente.setContatos(new HashSet<Contato>(List.of(contato, contato2)));

			cliente = clienteService.salvar(cliente);

			var cliente2 = new Cliente();
			cliente2.setDocumento("335921198772");
			cliente2.setEmpresa(empresa);
			cliente2.setNome("Teste2");
			cliente2.setRazaoSocial("Teste2");
			cliente2.setTipoPessoa(TipoPessoa.JURIDICA);
			cliente2.setEndereco(endereco);
			cliente2 = clienteService.salvar(cliente2);

			var usuario = new Usuario();
			usuario.setEmail("sandnine@gmail.com");
			usuario.setNome("ALDINEY");
			usuario.setSenha("123456");
			usuario.setClientes(List.of(cliente, cliente2));
			usuario.setEmpresas(List.of(empresa, empresa2));
			usuario = usuarioService.salvar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
