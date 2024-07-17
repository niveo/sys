package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.records.ClienteContatoDto;
import br.com.ams.sys.repository.ClienteContatoRepository;
import br.com.ams.sys.service.ClienteContatoService;
import br.com.ams.sys.service.ClienteService;

@Service
public class ClienteContatoServiceImpl implements ClienteContatoService {

	@Autowired
	private ClienteContatoRepository clienteContatoRepository;

	@Autowired
	private ClienteService clienteService;

	@Override
	public List<ClienteContatoDto> findByCliente(Long codigo) {
		var registros = clienteContatoRepository.findByCliente(codigo);
		return registros.stream().map(mp -> mp.toClienteContatoDto()).toList();
	}

	@Override
	public ClienteContato save(ClienteContato registro) throws Exception {
		return clienteContatoRepository.save(registro);
	}

	@Override
	public ClienteContatoDto save(ClienteContatoDto registro) throws Exception {
		var cliente = clienteService.findByCodigo(registro.cliente());
		ClienteContato registrar = null;
		if (registro.codigo() != null) {
			var emp = clienteContatoRepository.findById(registro.codigo()).get();
			registrar = registro.toClienteContato(emp, cliente);
		} else {
			registrar = registro.toClienteContato(new ClienteContato(), cliente);
		}
		return save(registrar).toClienteContatoDto();
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		clienteContatoRepository.deleteById(codigo);
	}

}
