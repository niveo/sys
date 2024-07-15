package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.service.BairroService;

@RestController
@RequestMapping(value = { "/bairros" })
public class BairroController {

	@Autowired
	private BairroService bairroService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes") String condicoes) throws Exception {
		System.out.println(condicoes); 
		var registros = bairroService.obterTodos(page, condicoes);
		return new PagedModel<>(registros);
	}

	@PostMapping
	public ResponseEntity<BairroDto> save(@RequestBody BairroCriarDto request) throws Exception {
		var cidade = bairroService.save(request);
		return new ResponseEntity<BairroDto>(cidade, HttpStatus.CREATED);
	}

}
