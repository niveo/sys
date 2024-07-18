package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.records.ClienteEnderecoDto;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.repository.ProdutoUnidadeRepository;
import br.com.ams.sys.service.ProdutoService;
import br.com.ams.sys.service.ProdutoUnidadeService;
import br.com.ams.sys.service.UnidadeService;

@Service
public class ProdutoUnidadeServiceImpl implements ProdutoUnidadeService {

	@Autowired
	private ProdutoUnidadeRepository produtoUnidadeRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private UnidadeService unidadeService;

	@Override
	public Page<ProdutoUnidadeDto> findByProduto(Integer page, Long codigo) {
		page--;
		var pageable = PageRequest.of(page, 5);
		var pageContent = produtoUnidadeRepository.findByProduto(codigo, pageable);
		var content = pageContent.getContent().stream().map(mp -> mp.toProdutoUnidadeDto()).toList();
		return new RestPage<ProdutoUnidadeDto>(content, page, 5, pageContent.getTotalElements());
	}

	@Override
	public ProdutoUnidade save(ProdutoUnidade registro) throws Exception {
		return produtoUnidadeRepository.save(registro);
	}

	@Override
	public ProdutoUnidadeDto save(ProdutoUnidadeDto registro) throws Exception {
		var produto = produtoService.findByCodigo(registro.produto());
		Unidade unidade = null;
		if (registro.unidade() != null)
			unidade = unidadeService.findByCodigo(registro.unidade().codigo());

		ProdutoUnidade registrar = null;
		if (registro.codigo() != null) {
			var emp = produtoUnidadeRepository.findById(registro.codigo()).get();
			registrar = registro.toProdutoUnidade(emp, unidade, produto);
		} else {
			registrar = registro.toProdutoUnidade(new ProdutoUnidade(), unidade, produto);
		}
		return save(registrar).toProdutoUnidadeDto();
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		produtoUnidadeRepository.deleteById(codigo);
	}

}
