package br.com.ams.sys.service;

import br.com.ams.sys.records.CEPPesquisaDto;

public interface CEPService {

	CEPPesquisaDto pesquisar(String cep) throws Exception;

}
