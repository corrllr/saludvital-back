package edu.eci.cvds.citasmedicas;

import edu.eci.cvds.citasmedicas.service.IEspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CitasmedicasApplication {
	@Autowired
	private IEspecialidadService especialidadService;	

	public static void main(String[] args) {
		SpringApplication.run(CitasmedicasApplication.class, args);
	}
	
}
