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

import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteRegistrarDto;
import br.com.ams.sys.service.ClienteService;

@RestController
@RequestMapping(value = { "/clientes" })
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes", required = false) String condicoes) throws Exception {
		var clientes = clienteService.obterTodos(empresa, page, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ClienteDto> obterCodigo(@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = clienteService.obterCodigo(empresa, codigo);
		return new ResponseEntity<ClienteDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ClienteDto> salvar(@RequestHeader(name = "empresa", required = true) Long empresa,
			@RequestBody ClienteRegistrarDto request) throws Exception {
		var registro = clienteService.save(empresa, request);
		return new ResponseEntity<ClienteDto>(registro, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		clienteService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
