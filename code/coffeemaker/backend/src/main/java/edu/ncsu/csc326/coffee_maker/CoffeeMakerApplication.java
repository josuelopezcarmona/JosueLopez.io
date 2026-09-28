package edu.ncsu.csc326.coffee_maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the CoffeeMaker backend.  Running main() starts the Spring
 * application context and the embedded web server.
 *
 * @author CSC 326 Course Staff
 */
@SpringBootApplication
public class CoffeeMakerApplication {

	/**
	 * Starts the CoffeeMaker application.
	 * @param args command line arguments passed through to Spring Boot
	 */
	public static void main(String[] args) {
		SpringApplication.run(CoffeeMakerApplication.class, args);
	}

}
