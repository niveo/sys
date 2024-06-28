package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.repository.EmpresaRepository;
import br.com.ams.sys.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public Empresa salvar(Empresa entidade) throws Exception {
		return empresaRepository.save(entidade);
	}

	@Override
	public Empresa obterCodigo(Long codigo) throws Exception {
		return this.empresaRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.empresaRepository.deleteById(codigo);
	}

}
