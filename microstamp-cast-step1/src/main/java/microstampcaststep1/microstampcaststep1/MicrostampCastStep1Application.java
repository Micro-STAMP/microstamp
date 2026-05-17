package microstampcaststep1.microstampcaststep1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; 

@SpringBootApplication
@EnableDiscoveryClient
public class MicrostampCastStep1Application {
    public static void main(String[] args) {
        SpringApplication.run(MicrostampCastStep1Application.class, args);
    }
}