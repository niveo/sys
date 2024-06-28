package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Cliente salvar(Cliente entidade) throws Exception {
		return clienteRepository.save(entidade);
	}

	@Override
	public Cliente obterCodigo(Long codigo) throws Exception {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.clienteRepository.deleteById(codigo);
	}
}
