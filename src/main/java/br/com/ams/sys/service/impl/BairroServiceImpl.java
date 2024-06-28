package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.service.BairroService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class BairroServiceImpl implements BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	@Override
	public Bairro salvar(Bairro entidade) throws Exception {
		return bairroRepository.save(entidade);
	}

	@Override
	public Bairro obterCodigo(Long codigo) throws Exception {
		return this.bairroRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.bairroRepository.deleteById(codigo);
	}
}
