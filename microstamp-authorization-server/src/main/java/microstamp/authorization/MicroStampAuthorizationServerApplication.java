package microstamp.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroStampAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroStampAuthorizationServerApplication.class, args);
	}

}
