package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public ResponseEntity<Page<ClienteDto>> obterTodos(@RequestParam(name = "page", defaultValue = "0") Integer page)
			throws Exception {

		var pageable = PageRequest.of(page, Constante.PAGINA_REGISTROS_CLIENTES);

		var clientes = clienteService.obterTodos(pageable);

		return new ResponseEntity<Page<ClienteDto>>(clientes, HttpStatus.OK);
	}
}
