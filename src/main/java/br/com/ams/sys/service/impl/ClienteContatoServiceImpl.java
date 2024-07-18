package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.records.ClienteContatoDto;
import br.com.ams.sys.repository.ClienteContatoRepository;
import br.com.ams.sys.service.ClienteContatoService;
import br.com.ams.sys.service.ClienteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ClienteContatoServiceImpl implements ClienteContatoService {

	@Autowired
	private ClienteContatoRepository clienteContatoRepository;

	@Autowired
	private ClienteService clienteService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ClienteContatoDto> findByCliente(Integer page, Long codigo) {
		page--;
		var pageable = PageRequest.of(page, 5);
		var pageContent = clienteContatoRepository.findByCliente(codigo, pageable);
		var content = pageContent.getContent().stream().map(mp -> mp.toClienteContatoDto()).toList();
		return new RestPage<ClienteContatoDto>(content, page, 5, pageContent.getTotalElements());

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
