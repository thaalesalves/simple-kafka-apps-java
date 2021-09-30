# Simple Kafka app
This simple application works with a simple single-node Kafka cluster that runs on Docker. It produces and consumes messages from pre-defined topics with no major data processing.

## Technologies used
* Java 11
* Lombok
* Spring Boot
* Spring WebFlux
* Spring Cloud Stream for Kafka
* Apache Avro
* Apache Kafka
* Docker

## Usage
1. Once the code is cloned, you need to compile the application. Since we have Avro schemas that should be compiled into Java classes, we need to generate these files.
```
mvn clean install generate-sources
```

2. Now that the application is built, we need to deploy the Kafka cluster
```docker-compose up```

3. After the cluster has been deployed, we can boot up our app and start producing messages. The test class `KafkaListenerTest` is a simple JUnit class that builds and produces a simple event that is then consumed by `BookConsumer`.