package br.com.ams.sys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.ams.sys.bd.DadosIniciaiService;

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
		System.out.println(timeZone);
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
		System.out.println(TimeZone.getDefault());
		System.out.println(ZoneId.systemDefault());
		System.out.println(LocalDateTime.now());
		System.out.println(LocalDateTime.now(ZoneId.systemDefault()));
		dadosIniciaiService.iniciar();
	}

}
