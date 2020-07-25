package top.alanlee.hello.hellorabbitmq.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.alanlee.hello.hellorabbitmq.service.MQService;

@Service
public class MQServiceImpl implements MQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
