package javabasicapi.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BelajarJavaSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BelajarJavaSpringBootApplication.class, args);
	}

}