package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.records.ClienteEnderecoDto;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.records.TabelaPrecoLancamentoDto;
import br.com.ams.sys.repository.ProdutoUnidadeRepository;
import br.com.ams.sys.repository.TabelaPrecoLancamentoRepository;
import br.com.ams.sys.service.ProdutoService;
import br.com.ams.sys.service.ProdutoUnidadeService;
import br.com.ams.sys.service.TabelaPrecoLancamentoService;
import br.com.ams.sys.service.TabelaPrecoService;
import br.com.ams.sys.service.UnidadeService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class TabelaPrecoLancamentoServiceImpl implements TabelaPrecoLancamentoService {

	@Autowired
	private TabelaPrecoLancamentoRepository tabelaPrecoLancamentoRepository;

	@Autowired
	private TabelaPrecoService tabelaPrecoService;

	@Override
	public Page<TabelaPrecoLancamentoDto> findByProduto(Integer page, Long codigo) {
		if (page > 0)
			page--;
		var pageable = PageRequest.of(page, 5);
		var pageContent = tabelaPrecoLancamentoRepository.findByProduto(codigo, pageable);
		var content = pageContent.getContent().stream().map(mp -> mp.toTabelaPrecoLancamentoDto()).toList();
		return new RestPage<TabelaPrecoLancamentoDto>(content, page, 5, pageContent.getTotalElements());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPrecoLancamento save(TabelaPrecoLancamento registro) throws Exception {
		return tabelaPrecoLancamentoRepository.save(registro);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPrecoLancamentoDto save(TabelaPrecoLancamentoDto registro) throws Exception {
		var tabela = tabelaPrecoService.findByCodigo(registro.tabela());

		TabelaPrecoLancamento registrar = null;
		if (registro.codigo() != null) {
			var emp = tabelaPrecoLancamentoRepository.findById(registro.codigo()).get();
			registrar = registro.toTabelaPrecoLancamento(emp, tabela);
		} else {
			registrar = registro.toTabelaPrecoLancamento(new TabelaPrecoLancamento(), tabela);
		}
		return save(registrar).toTabelaPrecoLancamentoDto();
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		tabelaPrecoLancamentoRepository.deleteById(codigo);
	}

	@Override
	public TabelaPrecoLancamento findByCodigo(Long codigo) throws Exception {
		return tabelaPrecoLancamentoRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));

	}

}
