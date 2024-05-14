package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventsUpdateListener {
    @RabbitListener(queues = "ns.events.update")
    public void listenForEventUpdates(String update) throws Exception {
        System.out.println("The message received: " + update);
        throw new Exception("test error");
    }
}