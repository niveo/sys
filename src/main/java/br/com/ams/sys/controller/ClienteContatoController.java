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

import br.com.ams.sys.records.ClienteContatoDto;
import br.com.ams.sys.service.ClienteContatoService;

@RestController
@RequestMapping(value = { "/clientecontatos" })
public class ClienteContatoController {
	@Autowired
	private ClienteContatoService clienteContatoService;

	@GetMapping("/{codigo}")
	public PagedModel<?> findByCliente(@PathVariable(name = "codigo", required = true) Long codigo,
			@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		var response = clienteContatoService.findByCliente(page, codigo);
		return new PagedModel<>(response);
	}

	@PostMapping
	public ResponseEntity<ClienteContatoDto> save(@RequestBody ClienteContatoDto registro) throws Exception {
		var response = clienteContatoService.save(registro);
		return new ResponseEntity<ClienteContatoDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		clienteContatoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
