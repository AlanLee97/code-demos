package top.alanlee.hello.hellorabbitmq.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import top.alanlee.hello.hellorabbitmq.entity.Person;

@Service
public class PersonService {

    //@RabbitListener(queues = "alanlee.news")
    public void receiveMessage(Person person){
        System.out.println("收到消息：" + person);
    }
}
