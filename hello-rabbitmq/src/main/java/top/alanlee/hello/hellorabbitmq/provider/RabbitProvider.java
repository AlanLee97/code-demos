package top.alanlee.hello.hellorabbitmq.provider;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitProvider {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(int i){
        String content = "hello" + i;
        System.out.println("Provider:" + content);
        amqpTemplate.convertAndSend("helloRabbitMQ", content);
    }

}
