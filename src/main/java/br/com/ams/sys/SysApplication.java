package br.com.ams.sys;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.ams.sys.bd.DadosIniciaiService;
import br.com.ams.sys.service.UsuarioService;

@SpringBootApplication
public class SysApplication implements CommandLineRunner {

	@Value("${sistema.time-zone}")
	private String timeZone;

	@Autowired
	DadosIniciaiService dadosIniciaiService;

	public static void main(String[] args) {
		SpringApplication.run(SysApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
		dadosIniciaiService.iniciar();
	}

}
