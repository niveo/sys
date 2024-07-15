package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.records.EmpresaDto;
import br.com.ams.sys.records.EmpresaListaDto;

public interface EmpresaService {
	Empresa save(Empresa entidade) throws Exception;

	EmpresaDto save(EmpresaDto entidade) throws Exception;

	Empresa findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	EmpresaDto obterCodigo(Long codigo) throws Exception;

	Page<EmpresaListaDto> obterTodos(Integer page, String conditions) throws Exception;
}
