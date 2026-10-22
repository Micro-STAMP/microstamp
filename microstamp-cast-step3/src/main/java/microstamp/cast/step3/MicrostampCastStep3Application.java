package microstamp.cast.step3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MicrostampCastStep3Application {

    public static void main(String[] args) {
        SpringApplication.run(MicrostampCastStep3Application.class, args);
    }

}
