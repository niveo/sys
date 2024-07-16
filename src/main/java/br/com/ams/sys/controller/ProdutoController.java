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

import br.com.ams.sys.records.ProdutoDto;
import br.com.ams.sys.service.ProdutoService;

@RestController
@RequestMapping(value = { "/produtos" })
public class ProdutoController {
	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes", required = false) String condicoes) throws Exception {
		var clientes = produtoService.obterTodos(empresa, page, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ProdutoDto> obterCodigo(@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = produtoService.obterCodigo(empresa, codigo);
		return new ResponseEntity<ProdutoDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProdutoDto> salvar(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestBody ProdutoDto request) throws Exception {
		var registro = produtoService.save(empresa, request);
		return new ResponseEntity<ProdutoDto>(registro, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		produtoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
