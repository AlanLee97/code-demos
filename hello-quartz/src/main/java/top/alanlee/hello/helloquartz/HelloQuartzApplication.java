package top.alanlee.hello.helloquartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HelloQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloQuartzApplication.class, args);
    }

}
