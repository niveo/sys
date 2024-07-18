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

import br.com.ams.sys.records.ClienteEnderecoDto;
import br.com.ams.sys.service.ClienteEnderecoService;

@RestController
@RequestMapping(value = { "/clienteenderecos" })
public class ClienteEnderecoController {
	@Autowired
	private ClienteEnderecoService clienteEnderecoService;

	@GetMapping("/{codigo}")
	public PagedModel<?>  obterCodigo(@PathVariable(name = "codigo", required = true) Long codigo,
			@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		var response = clienteEnderecoService.findByCliente(page, codigo);
		return new PagedModel<>(response);
	}

	@PostMapping
	public ResponseEntity<ClienteEnderecoDto> save(@RequestBody ClienteEnderecoDto registro) throws Exception {
		var response = clienteEnderecoService.save(registro);
		return new ResponseEntity<ClienteEnderecoDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete(@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		clienteEnderecoService.deleteByCodigo(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
