package org.example.service;

import java.util.Map;

public interface RabbitMQService {
    String sendMsgByHeadersExchange(String msg, Map<String, Object> map) throws Exception;

}
