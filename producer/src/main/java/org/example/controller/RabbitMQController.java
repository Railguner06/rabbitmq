package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.RabbitMQService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/mall/rabbitmq")
public class RabbitMQController {
    @Resource
    private RabbitMQService rabbitMQService;

    @PostMapping("/headersSend")
    @SuppressWarnings("unchecked")
    public String headersSend(@RequestParam(name = "msg") String msg,
                              @RequestParam(name = "json") String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, Map.class);
        return rabbitMQService.sendMsgByHeadersExchange(msg, map);
    }
}
