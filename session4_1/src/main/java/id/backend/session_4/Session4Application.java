package id.backend.session_4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties") // Force properties to load
public class Session4Application {

	public static void main(String[] args) {
		SpringApplication.run(Session4Application.class, args);
	}

}
