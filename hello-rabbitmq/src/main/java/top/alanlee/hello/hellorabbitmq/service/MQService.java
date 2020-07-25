package top.alanlee.hello.hellorabbitmq.service;

public interface MQService {
    void sendMessage(String exchange, String routingKey, String message);
}
