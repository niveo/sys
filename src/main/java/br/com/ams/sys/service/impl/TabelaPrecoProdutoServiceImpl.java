package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.entity.TabelaPrecoProduto;
import br.com.ams.sys.records.TabelaPrecoLancamentoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoRegisterDto;
import br.com.ams.sys.repository.TabelaPrecoProdutoRepository;
import br.com.ams.sys.service.ProdutoService;
import br.com.ams.sys.service.TabelaPrecoLancamentoService;
import br.com.ams.sys.service.TabelaPrecoProdutoService;

@Service
@Transactional(readOnly = true)
public class TabelaPrecoProdutoServiceImpl implements TabelaPrecoProdutoService {

	@Autowired
	private TabelaPrecoProdutoRepository tabelaPrecoProdutoRepository;

	@Autowired
	private TabelaPrecoLancamentoService tabelaPrecoLancamentoService;

	@Autowired
	private ProdutoService produtoService;

	@Override
	public Page<TabelaPrecoProdutoDto> findByLancamento(Integer page, Long codigo) {
		if (page > 0)
			page--;
		var pageable = PageRequest.of(page, 5);
		var pageContent = tabelaPrecoProdutoRepository.findByLancamento(codigo, pageable);
		var content = pageContent.getContent().stream().map(mp -> mp.toTabelaPrecoProdutoDto()).toList();
		return new RestPage<TabelaPrecoProdutoDto>(content, page, 5, pageContent.getTotalElements());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPrecoProduto save(TabelaPrecoProduto registro) throws Exception {
		return tabelaPrecoProdutoRepository.save(registro);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPrecoProdutoDto save(TabelaPrecoProdutoRegisterDto registro) throws Exception {
		TabelaPrecoProduto registrar = null;
		var lancamento = tabelaPrecoLancamentoService.findByCodigo(registro.lancamento());
		var produto = produtoService.findByCodigo(registro.produto());

		if (registro.codigo() != null) {
			var emp = tabelaPrecoProdutoRepository.findById(registro.codigo()).get();
			registrar = registro.toTabelaPrecoProduto(emp, lancamento, produto);
		} else {
			registrar = registro.toTabelaPrecoProduto(new TabelaPrecoProduto(), lancamento, produto);
		}
		return save(registrar).toTabelaPrecoProdutoDto();
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		tabelaPrecoProdutoRepository.deleteById(codigo);
	}

}
