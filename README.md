# Event driven indxer service

This application provides an indexing service by consuming user data from Kafka
and indexing into elasticsearch. 

<p align="center">
<img src="https://user-images.githubusercontent.com/31853241/218024512-b5e11029-e07c-4a04-a474-c4de454fba39.png" alt="Custom image"/>
</p>

### Framework

[Spring Kafka Reference Document](https://docs.spring.io/spring-kafka/reference/html/)

Spring kafka is used to consume messages and elasticsearch high level
rest client to index documents.

### Kafka topics

- user-updates

### Elastic search indices

- user_updates_*

### Consumer group

- user-update-*

### Dependencies 

- Kafka
- Elasticsearch
- Akhq

## Instructions

### Tests
Run the tests using the command:

```
gradle test
```
>
> Note: The integration tests automatically starts and
> stops the docker test containers.

### Application

The dependencies mentioned above can be run in a docker environment. Firstly,
create and image of the application using:

```
docker build -t eventapi .
```

Next, run the application and dependencies using the command:
```
docker compose up -d
```
>
> Note: The docker compose file is set to download images for M1 chip
>  (linux/arm64). For linux/amd64 architecture, specify them in the "platform"
> field in the docker compose file.

Open the command window and create a Kafka topic using the command:

```
docker-compose exec kafka kafka-topics.sh --create --topic user-updates --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
```

Run the following command to produce to the topic:

```
docker-compose exec kafka kafka-console-producer.sh --topic user-updates --broker-list kafka:9092
```

In the CLI enter the following message:

```
{"name":"Rick Sanchez","type":"user","id":"5LJT59HYR2","email":"rickc137@rickmail.com","phone":"+1 157 556 8560","membership":{"status":"premium","category":"family"},"address":"Smith Residence, Washington, USA"}
```
Search the newly indexed document using:

> http://localhost:9200/user_updates_1/_search








