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
import br.com.ams.sys.service.ProdutoUnidadeService;
import br.com.ams.sys.service.TabelaPrecoLancamentoService;

@RestController
@RequestMapping(value = { "/tabelaprecolancamentos" })
public class TabelaPrecoLancamentoController {

	@Autowired
	private TabelaPrecoLancamentoService tabelaPrecoLancamentoService;

	@GetMapping("/{codigo}")
	public PagedModel<?> findByProduto(@PathVariable(name = "codigo", required = true) Long codigo,
			@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		var response = tabelaPrecoLancamentoService.findByProduto(page, codigo);
		return new PagedModel<>(response);
	}

	@PostMapping
	public ResponseEntity<TabelaPrecoLancamentoDto> save(@RequestBody TabelaPrecoLancamentoDto registro)
			throws Exception {
		var response = tabelaPrecoLancamentoService.save(registro);
		return new ResponseEntity<TabelaPrecoLancamentoDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		tabelaPrecoLancamentoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
