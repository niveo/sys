package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.UnidadeDto;
import br.com.ams.sys.service.UnidadeService;

@RestController
@RequestMapping(value = { "/unidades" })
public class UnidadeController {
	@Autowired
	private UnidadeService unidadeService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes", required = false) String condicoes) throws Exception {
		var clientes = unidadeService.obterTodos(empresa, page, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<UnidadeDto> obterCodigo(@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = unidadeService.obterCodigo(empresa, codigo);
		return new ResponseEntity<UnidadeDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<UnidadeDto> salvar(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestBody UnidadeDto request) throws Exception {
		var registro = unidadeService.save(empresa, request);
		return new ResponseEntity<UnidadeDto>(registro, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		unidadeService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
