package br.com.ams.sys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SysApplication implements CommandLineRunner {

    @Value("${sistema.time-zone}")
    private String timeZone;

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}
