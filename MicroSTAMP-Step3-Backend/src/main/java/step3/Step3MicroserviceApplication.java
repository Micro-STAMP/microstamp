package step3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Step3MicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Step3MicroserviceApplication.class, args);
	}

}
