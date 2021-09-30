# Simple Kafka producer app
This simple application works with a simple single-node Kafka cluster that runs on Docker. It produces messages from JSON payloads sent through rest and posted to topics with no major data processing.

## Technologies used
* Java 11
* Lombok
* Spring Boot
* Spring WebFlux
* Spring Kafka
* Apache Avro
* Apache Kafka
* Docker

## Usage
1. Once the code is cloned, you need to compile the application. Since we have Avro schemas that should be compiled into Java classes, we need to generate these files.
```
$ mvn clean install generate-sources
```

2. Now that the application is built, we need to deploy the Kafka cluster
```
$ docker-compose up
```

3. After the cluster has been deployed, we can boot up our app and start producing messages. Send a POST request to `http://localhost:9090/api/producer/events` with the sample payload.
```json
{
    "event_type": "APPROVED_BOOK",
    "message": {
        "title": "Caim",
        "author": "José Saramago",
        "releaseYear": 2009,
        "genre": "Romance",
        "publisher": "Companhia das Letras",
        "isbn": 152151
    }
}
```

**INFO:** to modify or register other event types, check `es.thalesalv.eventproducer.domain.enums.EventTypes`. You'll need an Avro schema 