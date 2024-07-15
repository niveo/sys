package br.com.ams.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.EmpresaBasicoDto;
import br.com.ams.sys.records.LoginRequest;
import br.com.ams.sys.records.LoginResponse;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> autenticar(@RequestBody LoginRequest request) throws Exception {
		var response = usuarioService.autenticar(request);
		return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Void> criar(@RequestBody UsuarioCriarDto request) {
		usuarioService.criar(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/empresas")
	public ResponseEntity<List<EmpresaBasicoDto>> empresas() throws Exception {
		var response = usuarioService.empresas();
		return new ResponseEntity<List<EmpresaBasicoDto>>(response, HttpStatus.OK);
	}
}
