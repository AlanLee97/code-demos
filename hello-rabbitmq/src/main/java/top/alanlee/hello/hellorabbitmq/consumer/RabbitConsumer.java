package top.alanlee.hello.hellorabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "helloRabbitMQ")
public class RabbitConsumer {

    @RabbitHandler
    public void handler(String content){
        System.out.println("Consumer:" + content);
    }

}
