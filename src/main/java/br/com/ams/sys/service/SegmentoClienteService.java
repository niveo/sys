package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.SegmentoClienteDto;

public interface SegmentoClienteService {
	SegmentoCliente save(SegmentoCliente entidade) throws Exception;

	SegmentoCliente findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<SegmentoClienteDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	SegmentoClienteDto save(Long empresa, SegmentoClienteDto entidade) throws Exception;

	SegmentoClienteDto obterCodigo(Long empresa, Long codigo);

}
