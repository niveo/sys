package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.records.TabelaPrecoLancamentoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoRegisterDto;
import br.com.ams.sys.service.ProdutoUnidadeService;
import br.com.ams.sys.service.TabelaPrecoLancamentoService;
import br.com.ams.sys.service.TabelaPrecoProdutoService;

@RestController
@RequestMapping(value = { "/tabelaprecoprodutos" })
public class TabelaPrecoProdutoController {

	@Autowired
	private TabelaPrecoProdutoService tabelaPrecoProdutoService;

	@GetMapping("/{codigo}")
	public PagedModel<?> findByLancamento(@PathVariable(name = "codigo", required = true) Long codigo,
			@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		var response = tabelaPrecoProdutoService.findByLancamento(page, codigo);
		return new PagedModel<>(response);
	}

	@PostMapping
	public ResponseEntity<TabelaPrecoProdutoDto> save(@RequestBody TabelaPrecoProdutoRegisterDto registro)
			throws Exception {
		var response = tabelaPrecoProdutoService.save(registro);
		return new ResponseEntity<TabelaPrecoProdutoDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		tabelaPrecoProdutoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
