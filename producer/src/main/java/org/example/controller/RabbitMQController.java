package org.example.controller;

import org.example.service.RabbitMQService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mall/rabbitmq")
public class RabbitMQController {
    @Resource
    private RabbitMQService rabbitMQService;
    /**
     * 发送消息
     * @author java技术爱好者
     */
    @PostMapping("/publish")
    public String publish(@RequestParam(name = "msg") String msg) throws Exception {
        return rabbitMQService.sendMsgByFanoutExchange(msg);
    }
}
