

# 📘 BesparasBank – Developer Guide & Setup Documentation

This guide explains how to set up and run the **BesparasBank** project, including the Spring Boot backend, Kafka for event messaging, and Docker containers for development services.

---

## 🔧 Prerequisites

Make sure you have the following tools installed:

- [Java 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Git](https://git-scm.com/)
- Optional: [Postman](https://www.postman.com/) or `curl` for testing APIs

---

## 🚀 Run the Spring Boot Application

```bash
git clone https://github.com/yourusername/besparasbank.git
cd besparasbank
./mvnw spring-boot:run
```

Or with Maven installed globally:

```bash
mvn clean install
mvn spring-boot:run
```

📍 Access the app: [http://localhost:8080](http://localhost:8080)

---

## 🐳 Start Docker for Kafka + Zookeeper

You must have Docker running in the background.

Create a file named `docker-compose.yml` in the root of the project with:

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

Then run:

```bash
docker-compose up -d
```

✅ Kafka will be available at `localhost:9092`

---

## 📬 Verify Kafka is Running

From terminal:

```bash
docker ps
```

Look for `kafka` and `zookeeper` containers.

You can also use:

```bash
docker logs <kafka-container-id> --follow
```

To see real-time broker logs.

---

## 🛠️ Kafka Configuration in Spring Boot

Make sure this is in `application.properties`:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=transaction-logger
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

---

## 📄 Common Endpoints

| URL               | Description                       |
|-------------------|-----------------------------------|
| `/login`          | User login                        |
| `/register`       | Create new user account           |
| `/dashboard`      | View balance, last transfer       |
| `/transactions`   | Full transaction history          |
| `/profile`        | View or edit profile              |
| `/account-summary`| Account + transaction overview    |

---

## 🧠 Troubleshooting Tips

- 🛑 If Kafka shows "node -1 disconnected": Ensure Docker is running and Kafka container is using `localhost:9092`
- ⚠️ If ports are already in use, stop conflicting services or change ports
- 🐞 Spring Boot crash? Check for missing DB/Kafka config or invalid bean definitions

---

## 🧪 Sample Data (Optional)

To test features quickly:
1. Register a user
2. Login and deposit money
3. Create transactions
4. View filters and account summary

---

## 👨‍💻 Maintainer

Developed by **Rafael Besparas**  
rafaelbesparas@outlook.com

MIT Licensed – Safe to clone, learn, and extend.
