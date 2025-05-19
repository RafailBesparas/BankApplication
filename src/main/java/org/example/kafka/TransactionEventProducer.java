package org.example.kafka;

// Import necessary Spring and Kafka components
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for producing (publishing) transaction events to a Kafka topic.
 * It uses Spring's KafkaTemplate to send messages to the "transaction-events" topic.
 */
@Service // Registers this class as a Spring-managed service bean
public class TransactionEventProducer {

    // Name of the Kafka topic to which transaction events will be published
    private static final String TOPIC = "transaction-events";

    // KafkaTemplate is used to send messages to Kafka
    @Autowired
    //Automatically injects the KafkaTemplate bean configured in the application
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends a transaction event message to the Kafka topic.
     *
     * @param message the message content to be sent to the topic
     */
    public void sendTransactionEvent(String message)
    {
        kafkaTemplate.send(TOPIC, message); // Sends the message to the "transaction-events" topic
    }
}