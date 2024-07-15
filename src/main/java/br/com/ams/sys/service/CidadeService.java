package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;

public interface CidadeService {
	Cidade save(Cidade entidade) throws Exception;

	Cidade findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<CidadeDto> obterTodos(PageRequest pageable, String descricao) throws Exception;

	CidadeDto pesquisarDescricaoSingle(String descricao, String estadoSigla);

	CidadeDto save(CidadeCriarDto entidade) throws Exception;

	CidadeDto obterCodigo(Long codigo);
}
