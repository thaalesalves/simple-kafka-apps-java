# Simple Kafka Applications in Java
This repo contains a few simple apps that worked as proofs of concept for an architectural model we used at work. These applications depend on Schema Registry to work with Avros in the topics used by the applications, and does simple processing that consumes and produces the Avro schemas `BookSchema` and `MagazineSchema`. 

* [Simple Kafka consumer](simple-event-consumer/): Simple application with Spring Boot that consumes data from a topic and processes it
* [Simple Kafka producer](simple-event-producer/): Simple application with Spring Boot that produces data to a topic so a consumer may process it
* [Simple Kafka Streams application](streams-event-conprod): Simple application with Spring Boot that streams data from a topic to another