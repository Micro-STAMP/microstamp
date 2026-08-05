package microstamp.cast.step5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicrostampCastStep5Application {

    public static void main(String[] args) {
        SpringApplication.run(MicrostampCastStep5Application.class, args);
    }

}
