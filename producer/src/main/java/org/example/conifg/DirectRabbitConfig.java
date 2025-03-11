package org.example.conifg;

import org.example.RabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

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
        //启动项目即创建交换机和队列
        rabbitAdmin.declareExchange(rabbitmqDemoFanoutExchange());
        rabbitAdmin.declareQueue(fanoutExchangeQueueB());
        rabbitAdmin.declareQueue(fanoutExchangeQueueA());
        return null;
    }

    @Bean
    public Queue fanoutExchangeQueueA() {
        //队列A
        return new Queue(RabbitMQConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_A, true, false, false);
    }

    @Bean
    public Queue fanoutExchangeQueueB() {
        //队列B
        return new Queue(RabbitMQConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_B, true, false, false);
    }

    @Bean
    public FanoutExchange rabbitmqDemoFanoutExchange() {
        //创建FanoutExchange类型交换机
        return new FanoutExchange(RabbitMQConfig.FANOUT_EXCHANGE_DEMO_NAME, true, false);
    }

    @Bean
    public Binding bindFanoutA() {
        //队列A绑定到FanoutExchange交换机
        return BindingBuilder.bind(fanoutExchangeQueueA()).to(rabbitmqDemoFanoutExchange());
    }

    @Bean
    public Binding bindFanoutB() {
        //队列B绑定到FanoutExchange交换机
        return BindingBuilder.bind(fanoutExchangeQueueB()).to(rabbitmqDemoFanoutExchange());
    }



}