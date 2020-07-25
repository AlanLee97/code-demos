package top.alanlee.hello.hellorabbitmq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.alanlee.hello.hellorabbitmq.entity.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRabbitMQ {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;


    //测试发送消息
    @Test
    public void testSendMessage(){
        String exchange = "exchange.direct";
        String routingKey = "alanlee.news";
        String object = "spring boot msg";
        Map<String, Object> map = new HashMap();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("data", new Person("AlanLee", 23));
        rabbitTemplate.convertAndSend(exchange, routingKey, map);
    }

    //测试接收消息
    @Test
    public void testReceiveMessage(){
        Object o = rabbitTemplate.receiveAndConvert("alanlee.news");
        System.out.println("接收的消息=========================");
        System.out.println(o);
    }

    //创建交换机
    @Test
    public void createExchange(){
        amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
        System.out.println("交换机创建完成");
    }

    //创建队列
    @Test
    public void createQueue(){
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue"));
        System.out.println("队列创建完成");
    }

    //创建绑定规则，将交换机与队列绑定起来
    @Test
    public void createBinding(){
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.exchange", "amqpadmin.key", null));
        System.out.println("绑定完成");
    }

    //测试模拟下单
    @Test
    public void testStimulateAddOrder(){
        addOrder();

        String exchange = "exchange.direct";
        String routingKey = "alanlee.news";

        //发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, "add.order.ok");
        System.out.println("消息已发送");
    }

    void addOrder(){
        System.out.println("下单成功");
    }

    @Test
    public void testStimulateSendEmail(){
        Object o = rabbitTemplate.receiveAndConvert("alanlee.news");
        System.out.println("接收的内容：" + o);
    }

}
