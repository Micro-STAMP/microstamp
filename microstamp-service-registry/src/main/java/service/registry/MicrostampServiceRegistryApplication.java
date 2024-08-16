package service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The @EnableEurekaServer annotation transforms our SpringBoot application
 * into a Eureka Server-based Service Registry.
 */
@SpringBootApplication
@EnableEurekaServer
public class MicrostampServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrostampServiceRegistryApplication.class, args);
	}

}
