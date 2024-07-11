package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteRegistrarDto;
import br.com.ams.sys.service.ClienteService;

@RestController
@RequestMapping(value = { "/clientes" })
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestHeader("empresa") Long empresa,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "condicoes") String condicoes) throws Exception {
		var pageable = PageRequest.of(page, Constante.PAGINA_REGISTROS_EMPRESAS);
		var clientes = clienteService.obterTodos(empresa, pageable, condicoes);
		return new PagedModel<>(clientes);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ClienteDto> obterCodigo(@PathVariable(name = "codigo", required = true) Long codigo)
			throws Exception {
		var response = clienteService.obterCodigo(codigo);
		return new ResponseEntity<ClienteDto>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ClienteDto> salvar(@RequestBody ClienteRegistrarDto request) throws Exception {
		var empresa = clienteService.save(request);
		return new ResponseEntity<ClienteDto>(empresa, HttpStatus.CREATED);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> remover(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		clienteService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
