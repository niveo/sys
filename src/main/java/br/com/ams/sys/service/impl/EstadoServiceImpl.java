package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.repository.EstadoRepository;
import br.com.ams.sys.service.EstadoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadoServiceImpl implements EstadoService {
	@Autowired
	private EstadoRepository estadoRepository;

	@Override
	public Estado salvar(Estado entidade) throws Exception {
		return estadoRepository.save(entidade);
	}

	@Override
	public Estado obterCodigo(Long codigo) throws Exception {
		return this.estadoRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.estadoRepository.deleteById(codigo);
	}

}
