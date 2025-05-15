package org.example.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventConsumer {

    @KafkaListener(topics = "transaction-events", groupId = "transaction-logger")
    public void consume(String message) {
        System.out.println("🔔 Transaction Event Received: " + message);
    }
}