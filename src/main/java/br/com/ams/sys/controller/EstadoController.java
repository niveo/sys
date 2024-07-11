package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.service.EstadoService;

@RestController
@RequestMapping(value = { "/estados" })
public class EstadoController {
	@Autowired
	private EstadoService estadoService;

	@GetMapping
	public ResponseEntity<List<EstadoDto>> obterTodos() throws Exception {
		var registros = estadoService.obterTodos();
		return new ResponseEntity<List<EstadoDto>>(registros, HttpStatus.OK);
	}

}
