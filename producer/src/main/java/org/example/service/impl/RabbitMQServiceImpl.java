package org.example.service.impl;

import org.example.RabbitMQConfig;
import org.example.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布消息
     * @param msg
     * @return
     * @throws Exception
     */
    @Override
    public String sendMsgByTopicExchange(String msg, String routingKey) throws Exception {
        Map<String, Object> message = getMessage(msg);
        try {
            //发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_DEMO_NAME, routingKey, message);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 组装消息体
     * @param msg
     * @return
     */
    private Map<String, Object> getMessage(String msg) {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", msg);
        return map;
    }
}