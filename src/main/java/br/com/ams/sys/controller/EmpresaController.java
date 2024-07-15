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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.EmpresaDto;
import br.com.ams.sys.service.EmpresaService;

@RestController
@RequestMapping(value = { "/empresas" })
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes") String condicoes) throws Exception {
		var clientes = empresaService.obterTodos(page, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<EmpresaDto> obterCodigo(@PathVariable(name = "codigo", required = true) Long codigo)
			throws Exception {
		var response = empresaService.obterCodigo(codigo);
		return new ResponseEntity<EmpresaDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EmpresaDto> salvar(@RequestBody EmpresaDto request) throws Exception {
		var empresa = empresaService.save(request);
		return new ResponseEntity<EmpresaDto>(empresa, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		empresaService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
