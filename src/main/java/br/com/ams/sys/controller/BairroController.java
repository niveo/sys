package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CidadeService;

@RestController
@RequestMapping(value = { "/bairros" })
public class BairroController {

	@Autowired
	private BairroService bairroService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes") String condicoes) throws Exception {
		var pageable = PageRequest.of(page, Constante.PAGINA_REGISTROS);
		var registros = bairroService.obterTodos(pageable, condicoes);
		return new PagedModel<>(registros);
	}

	@PostMapping
	public ResponseEntity<BairroDto> criar(@RequestBody BairroCriarDto request) throws Exception {
		var cidade = bairroService.save(request);
		return new ResponseEntity<BairroDto>(cidade, HttpStatus.CREATED);
	}

}
