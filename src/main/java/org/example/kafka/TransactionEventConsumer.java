package org.example.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * This class listens to Kafka messages about transactions.
 * When a new transaction happens, a message is sent to the "transaction-events" topic.
 * This class receives and prints that message.
 */
// This class in the Consumer
    // It listed to the Kafka messages about transactions
    // When a new transaction happends, a message is sent to the transaction events topic
    // This class subcribes to the events and prints the message
@Service // Register this class as a service which is used for Business logic
public class TransactionEventConsumer {

    // Method that runs automatically when a new message is received from Kafka
    // Listens to the transactions events topic
    @KafkaListener(
            topics = "transaction-events",       // Name of the Kafka topic
            groupId = "transaction-logger"       // Consumer group ID to coordinate parallel listeners
    )

    public void consume(String message) {
        // Print the message to the console
        System.out.println("🔔 New Transaction Event: " + message);
    }
}
