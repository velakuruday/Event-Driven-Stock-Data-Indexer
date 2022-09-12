# Event driven indxer service

This application provides and indexing service by consuming user data from Kafka
and indexing into elasticsearch.

### Kafka topics

- user.Updates

### Elastic search indices

- user_updates_*

### Consumer group

- user-update-*

### Framework

[Spring Kafka Reference Document](https://docs.spring.io/spring-kafka/reference/html/)

Spring kafka is used to consume messages and elasticsearch high level
rest client to index documents.

### Dependencies 

- Kafka
- Zookeeper
- Elasticsearch
- Akhq
- Kibana

## Instructions

### Tests
Run the tests using the command:

> `gradle test`
>
> Note: The integration tests automatically starts and
> stops the docker test containers.

### Application

The dependencies mentioned above can be run in a docker environment by using
the command:

>`docker compose up`
>
> Note: The docker compose file is set to download images for M1 chip
>  (linux/arm64). For linux/amd64 architecture, specify them in the "platform"
> field in the docker compose files.

Once the containers are running, bring up akhq using the url
http://localhost:8081/ in the browser. Navigate to _Topics > Create a topic_
and create topic called "user.Updates".

Run the application using the command:

> `gradle bootrun`
> 
> Note: The application will successfully start only if all dependent
> containers are running and healthy. 









