package br.com.ams.sys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.records.EmpresaDto;
import br.com.ams.sys.records.EmpresaListaDto;
import br.com.ams.sys.records.EmpresaRegistrarDto;

public interface EmpresaService {
	Empresa save(Empresa entidade) throws Exception;

	EmpresaDto save(EmpresaRegistrarDto entidade) throws Exception;

	Empresa findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;

	EmpresaDto obterCodigo(Long codigo) throws Exception;

	Page<EmpresaListaDto> obterTodos(PageRequest pageable, String conditions) throws Exception;
}
