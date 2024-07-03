package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.service.ClienteService;

@RestController
@RequestMapping(value = { "/clientes" })
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	PagedModel<?> obterTodos(@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		var pageable = PageRequest.of(page, Constante.PAGINA_REGISTROS_CLIENTES);
		var clientes = clienteService.obterTodos(pageable);
		return new PagedModel<>(clientes);
	}

}
