package org.example.kafka;

// Import the KafkaListener annotation to listen to Kafka topics
import org.springframework.kafka.annotation.KafkaListener;

// Import the Service annotation to register this class as a Spring service
import org.springframework.stereotype.Service;

/**
 * Service class responsible for consuming messages from the Kafka topic "transaction-events".
 * This is part of the event-driven architecture where transaction events are logged or processed.
 */
// Marks this class as a Spring-managed service component
@Service
public class TransactionEventConsumer {

    /**
     * Consumes messages from the "transaction-events" Kafka topic.
     * The consumer belongs to the "transaction-logger" consumer group.
     *
     * @param message the message payload received from the Kafka topic
     */
    @KafkaListener(
            topics = "transaction-events", // Topic to listen to
            groupId = "transaction-logger" // Consumer group ID for parallel consumption control
    )
    public void consume(String message) {
        // Output the received transaction event to the console (for logging/demo purposes)
        System.out.println("🔔 Transaction Event Received: " + message);
    }
}