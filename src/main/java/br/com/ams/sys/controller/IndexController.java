package br.com.ams.sys.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping(path = "/")
	public ResponseEntity<String> obterCodigo() throws Exception {

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

}
