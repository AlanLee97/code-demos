package top.alanlee.hello.hellorabbitmq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.hello.hellorabbitmq.consumer.RabbitConsumer;
import top.alanlee.hello.hellorabbitmq.provider.RabbitProvider;
import top.alanlee.hello.hellorabbitmq.service.impl.MQServiceImpl;

@RestController
@RequestMapping("/mq")
public class TestController {

    @Autowired
    private RabbitProvider rabbitProvider;

    @Autowired
    private MQServiceImpl mqService;


    @RequestMapping("/provider")
    public void provider(){
        for (int i = 0; i < 100; i++) {
            rabbitProvider.send(i);
        }
    }

    @RequestMapping("/sms/send")
    public String sendSMS(){
        String exchange = "exchange.direct";
        String routingKey = "panda.sms";
        mqService.sendMessage(exchange, routingKey, "panda.sms.send");
        return "ok";
    }




}
