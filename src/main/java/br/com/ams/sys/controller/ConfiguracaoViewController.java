package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.ConfiguracaoViewDto;
import br.com.ams.sys.service.ConfiguracaoViewService;

@RestController
@RequestMapping(value = { "/configuracaoviews" })
public class ConfiguracaoViewController {
	@Autowired
	private ConfiguracaoViewService configuracaoViewService;

	@GetMapping("/{codigo}")
	public ResponseEntity<ConfiguracaoViewDto> obterCodigo(
			@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = configuracaoViewService.obterCodigo(empresa, codigo);
		return new ResponseEntity<ConfiguracaoViewDto>(response, HttpStatus.OK);
	}
}
