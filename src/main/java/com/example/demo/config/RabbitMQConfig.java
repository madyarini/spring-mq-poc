package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Declarables queue(){
        return new Declarables(
                new DirectExchange("ecommerce_engine"),
                new Queue("ns.events.update"),
                new Binding(
                        "ns.events.update",
                        Binding.DestinationType.QUEUE,
                        "ns.events.update--retry-requeue",
                        "#",
                        null
                )
        );
    }

    @Bean
    public DirectExchange retryRequeueExchange() {
        return new DirectExchange("ns.events.update--retry-requeue");
    }

    @Bean
    public Declarables retryQueue(){
        Queue q = QueueBuilder.durable("ns.events.update--retry")
                .withArgument("x-dead-letter-exchange","ns.events.update--retry-requeue")
                .withArgument("x-dead-letter-routing-key","#")
                .withArgument("x-message-ttl", 60000)
                .build();

        return new Declarables(
                new DirectExchange("ns.events.update--retry"),
                q,
                new Binding(
                        "ns.events.update--retry",
                        Binding.DestinationType.QUEUE,
                        "ns.events.update--retry",
                        "#",
                        null
                )
        );
    }

    @Bean
    public Declarables errorQueue(){
        return new Declarables(
                new DirectExchange("ns.events.update--error"),
                new Queue("ns.events.update--error"),
                new Binding(
                        "ns.events.update--error",
                        Binding.DestinationType.QUEUE,
                        "ns.events.update--error",
                        "#",
                        null
                )
        );
    }

}
