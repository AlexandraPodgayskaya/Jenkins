package com.epam.esm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Class contains method to run Spring Boot application
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@SpringBootApplication
public class ApplicationRunner extends SpringBootServletInitializer {

	/**
	 * The entry point of Spring Boot application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}

}
