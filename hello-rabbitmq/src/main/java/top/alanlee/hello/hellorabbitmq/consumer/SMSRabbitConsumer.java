package top.alanlee.hello.hellorabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "panda.sms")
public class SMSRabbitConsumer {

    @RabbitHandler
    public void handler(String content){
        System.out.println("接到消息:" + content);
        System.out.println("执行下面的操作");
        System.out.println("发送短信");
        System.out.println("发送成功");
    }

}
