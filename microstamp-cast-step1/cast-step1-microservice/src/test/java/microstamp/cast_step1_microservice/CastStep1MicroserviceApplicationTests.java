package microstamp.cast_step1_microservice;

import microstamp.cast.step1.CastStep1Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CastStep1Application.class, properties = "eureka.client.enabled=false")
class CastStep1MicroserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
