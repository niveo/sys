package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.service.CidadeService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CidadeServiceImpl implements CidadeService {
	@Autowired
	private CidadeRepository cidadeRepository;

	@Override
	public Cidade salvar(Cidade entidade) throws Exception {
		return cidadeRepository.save(entidade);
	}

	@Override
	public Cidade obterCodigo(Long codigo) throws Exception {
		return this.cidadeRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.cidadeRepository.deleteById(codigo);
	}
}
