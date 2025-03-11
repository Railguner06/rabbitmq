package org.example.service;

public interface RabbitMQService {
    String sendMsgByFanoutExchange(String msg) throws Exception;

}
