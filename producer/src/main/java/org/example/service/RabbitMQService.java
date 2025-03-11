package org.example.service;

public interface RabbitMQService {
    String sendMsgByTopicExchange(String msg, String routingKey) throws Exception;

}
