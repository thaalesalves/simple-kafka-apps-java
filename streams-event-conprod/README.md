# Simple Kafka Streams app
This simple application works with a simple single-node Kafka cluster that runs on Docker. It produces and consumes messages from pre-defined topics with no major data processing using the Kafka Streams API without Spring Cloud Stream.

## Technologies used
* Java 11
* Lombok
* Spring Boot
* Kafka Streams API libraries
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

3. Once the cluster is up, you can deploy the [producer application](https://github.com/thaalesalves/simple-kafka-producer-java) locally or produce `BookSchema` events to the topic supplied in `application.yaml`. You can also use the Kafka CLI to produce events to the topic.