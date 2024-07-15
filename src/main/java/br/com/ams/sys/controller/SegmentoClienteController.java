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

import br.com.ams.sys.records.SegmentoClienteDto;
import br.com.ams.sys.service.SegmentoClienteService;

@RestController
@RequestMapping(value = { "/segmentosclientes" })
public class SegmentoClienteController {
	@Autowired
	private SegmentoClienteService segmentoService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes", required = false) String condicoes) throws Exception {
		var clientes = segmentoService.obterTodos(empresa, page, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<SegmentoClienteDto> obterCodigo(@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = segmentoService.obterCodigo(empresa, codigo);
		return new ResponseEntity<SegmentoClienteDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<SegmentoClienteDto> salvar(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestBody SegmentoClienteDto request) throws Exception {
		var registro = segmentoService.save(empresa, request);
		return new ResponseEntity<SegmentoClienteDto>(registro, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		segmentoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
