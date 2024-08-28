# Event driven indxer service

This application provides an indexing service by consuming stock data and indexing into elasticsearch. 


![](indexed.png)

### Framework

[Spring Kafka Reference Document](https://docs.spring.io/spring-kafka/reference/html/)

Spring kafka is used to consume messages and elasticsearch high level
rest client to index documents.

### Kafka topics

- stock-updates

### Elastic search indices

- stock_updates_*

### Consumer group

- user-update-*

### Dependencies 

- Kafka
- Elasticsearch
- Kafdrop

### Python script Dependencies
- python 3.9
- kafka-python 2.0.2
- yfinance 0.2.41

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

Run the stockfeed script using the command:

```
python stockfeed.py
```

The stock data will be consumed by the application and indexed into elasticsearch in the index "stock_updates_*".






