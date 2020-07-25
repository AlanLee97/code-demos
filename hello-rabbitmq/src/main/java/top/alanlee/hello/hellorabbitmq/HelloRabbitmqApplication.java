package top.alanlee.hello.hellorabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.alanlee.hello.hellorabbitmq.provider.RabbitProvider;

@EnableRabbit
@SpringBootApplication
public class HelloRabbitmqApplication {


    public static void main(String[] args) {
        SpringApplication.run(HelloRabbitmqApplication.class, args);
    }

}
