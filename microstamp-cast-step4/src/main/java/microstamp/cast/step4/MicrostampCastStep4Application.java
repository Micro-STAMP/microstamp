package microstamp.cast.step4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicrostampCastStep4Application {

    public static void main(String[] args) {
        SpringApplication.run(MicrostampCastStep4Application.class, args);
    }

}
