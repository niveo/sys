package br.com.ams.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ams.sys.records.ConfiguracaoGradeDto;
import br.com.ams.sys.service.ConfiguracaoGradeService;

@RestController
@RequestMapping(value = { "/configuracaograde" })
public class ConfiguracaoViewController {
	@Autowired
	private ConfiguracaoGradeService configuracaoViewService;

	@GetMapping("/{codigo}")
	public ResponseEntity<ConfiguracaoGradeDto> obterCodigo(
			@RequestHeader(name = "empresa", required = true) Long empresa,
			@PathVariable(name = "codigo", required = true) Long codigo) throws Exception {
		var response = configuracaoViewService.obterCodigo(empresa, codigo);
		return new ResponseEntity<ConfiguracaoGradeDto>(response, HttpStatus.OK);
	}
}
