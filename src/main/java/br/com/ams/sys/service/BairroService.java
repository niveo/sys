package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;

public interface BairroService {
	Bairro save(Bairro entidade) throws Exception;

	Bairro findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<BairroDto> obterTodos(Integer page, String descricao) throws Exception;

	BairroDto pesquisarDescricaoSingle(String descricao);

	BairroDto save(BairroCriarDto entity) throws Exception;

	BairroDto obterCodigo(Long codigo);
}
