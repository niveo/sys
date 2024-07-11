package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.CEPPesquisaDto;
import br.com.ams.sys.service.CEPService;

@RestController
public class CEPController {

	@Autowired
	private CEPService cepService;

	@GetMapping("/cep/{cep}")
	public ResponseEntity<CEPPesquisaDto> pesquisarCep(@PathVariable(name = "cep", required = true) String cep)
			throws Exception {
		var registro = cepService.pesquisar(cep);
		return new ResponseEntity<CEPPesquisaDto>(registro, HttpStatus.OK);
	}
}
