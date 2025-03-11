package org.example.conifg;

import org.example.RabbitMQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现BeanPostProcessor类，使用Bean的生命周期函数
 */
@Configuration
public class DirectRabbitConfig implements BeanPostProcessor {


    /**
     * 创建交换机和队列用的rabbitAdmin对象
     */
    @Resource
    private RabbitAdmin rabbitAdmin;


    /**
     * 初始化rabbitAdmin对象
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 实例化bean后，也就是Bean的后置处理器
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        rabbitAdmin.declareExchange(rabbitmqDemoHeadersExchange());
        rabbitAdmin.declareQueue(headersQueueA());
        rabbitAdmin.declareQueue(headersQueueB());
        return null;
    }
    @Bean
    public Queue headersQueueA() {
        return new Queue(RabbitMQConfig.HEADERS_EXCHANGE_QUEUE_A, true, false, false);
    }

    @Bean
    public Queue headersQueueB() {
        return new Queue(RabbitMQConfig.HEADERS_EXCHANGE_QUEUE_B, true, false, false);
    }

    @Bean
    public HeadersExchange rabbitmqDemoHeadersExchange() {
        return new HeadersExchange(RabbitMQConfig.HEADERS_EXCHANGE_DEMO_NAME, true, false);
    }

    @Bean
    public Binding bindHeadersA() {
        Map<String, Object> map = new HashMap<>();
        map.put("key_one", "java");
        map.put("key_two", "rabbit");
        //全匹配
        return BindingBuilder.bind(headersQueueA())
                .to(rabbitmqDemoHeadersExchange())
                .whereAll(map).match();
    }

    @Bean
    public Binding bindHeadersB() {
        Map<String, Object> map = new HashMap<>();
        map.put("headers_A", "coke");
        map.put("headers_B", "sky");
        //部分匹配
        return BindingBuilder.bind(headersQueueB())
                .to(rabbitmqDemoHeadersExchange())
                .whereAny(map).match();
    }

}