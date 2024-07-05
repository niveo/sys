package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.service.CidadeService;

@RestController
@RequestMapping(value = { "/cidades" })
public class CidadeController {

	@Autowired
	private CidadeService cidadeService;

	@GetMapping("/pesquisarDescricao")
	public ResponseEntity<List<CidadeDto>> pesquisarDescricao(
			@RequestParam(name = "descricao", required = true) String descricao) {
		var registros = cidadeService.pesquisarDescricao(descricao);
		return new ResponseEntity<List<CidadeDto>>(registros, HttpStatus.OK);
	}

}
