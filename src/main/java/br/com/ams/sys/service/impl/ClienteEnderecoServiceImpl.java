package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.records.ClienteContatoDto;
import br.com.ams.sys.records.ClienteEnderecoDto;
import br.com.ams.sys.repository.ClienteEnderecoRepository;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteEnderecoService;
import br.com.ams.sys.service.ClienteService;

@Service
public class ClienteEnderecoServiceImpl implements ClienteEnderecoService {

	@Autowired
	private ClienteEnderecoRepository clienteEnderecoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Override
	public Page<ClienteEnderecoDto> findByCliente(Integer page, Long codigo) {
		page--;
		var pageable = PageRequest.of(page, 5);
		var pageContent = clienteEnderecoRepository.findByCliente(codigo, pageable);
		var content = pageContent.getContent().stream().map(mp -> mp.toClienteEnderecoDto()).toList();
		return new RestPage<ClienteEnderecoDto>(content, page, 5, pageContent.getTotalElements());
	}

	@Override
	public ClienteEndereco save(ClienteEndereco registro) throws Exception {
		return clienteEnderecoRepository.save(registro);
	}

	@Override
	public ClienteEnderecoDto save(ClienteEnderecoDto registro) throws Exception {
		var cidade = cidadeService.findByCodigo(registro.endereco().cidade().codigo());
		var bairro = bairroService.findByCodigo(registro.endereco().bairro().codigo());
		var cliente = clienteService.findByCodigo(registro.cliente());

		ClienteEndereco registrar = null;
		if (registro.codigo() != null) {
			var emp = clienteEnderecoRepository.findById(registro.codigo()).get();
			registrar = registro.toClienteEndereco(emp, cliente, cidade, bairro);
		} else {
			registrar = registro.toClienteEndereco(new ClienteEndereco(), cliente, cidade, bairro);
		}

		return save(registrar).toClienteEnderecoDto();
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		clienteEnderecoRepository.deleteById(codigo);
	}

}
