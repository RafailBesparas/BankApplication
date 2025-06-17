package org.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


// This class sends the message to a Kafka topic called transaction events
    // It is used in order to publish a message to new transaction events
@Service // Register this class as a Spring Managed service component
public class TransactionEventProducer {

    // The name of the Kafka topic
    private static final String TOPIC = "transaction-events";

    // This object is used to send messages to Kafka
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Injects a kafka template for messaging publishing

    // This method sends a message to the Kafka topic and the message I want to send
    public void sendTransactionEvent(String message) {
        // Send the message to the topic
        kafkaTemplate.send(TOPIC, message);
    }
}
