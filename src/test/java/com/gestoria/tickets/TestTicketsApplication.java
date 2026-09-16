package com.gestoria.tickets;

import org.springframework.boot.SpringApplication;

public class TestTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
