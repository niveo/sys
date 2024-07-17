package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.service.ProdutoUnidadeService;

@RestController
@RequestMapping(value = { "/produtounidades" })
public class ProdutoUnidadeController {

	@Autowired
	private ProdutoUnidadeService produtoUnidadeService;

	@GetMapping("/{codigo}")
	public ResponseEntity<List<ProdutoUnidadeDto>> findByProduto(
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = produtoUnidadeService.findByProduto(codigo);
		return new ResponseEntity<List<ProdutoUnidadeDto>>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProdutoUnidadeDto> save(@RequestBody ProdutoUnidadeDto registro) throws Exception {
		var response = produtoUnidadeService.save(registro);
		return new ResponseEntity<ProdutoUnidadeDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		produtoUnidadeService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
