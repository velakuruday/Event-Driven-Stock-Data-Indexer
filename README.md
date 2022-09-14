# Event driven indxer service

This application provides an indexing service by consuming user data from Kafka
and indexing into elasticsearch.

### Framework

[Spring Kafka Reference Document](https://docs.spring.io/spring-kafka/reference/html/)

Spring kafka is used to consume messages and elasticsearch high level
rest client to index documents.

### Kafka topics

- user.Updates

### Elastic search indices

- user_updates_*

### Consumer group

- user-update-*

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

The dependencies mentioned above can be run in a docker environment. Firstly,
create and image of the application using:

>`docker build -t eventapi .`

Next run the application and dependencies using the command:

>`docker compose up`
>
> Note: The docker compose file is set to download images for M1 chip
>  (linux/arm64). For linux/amd64 architecture, specify them in the "platform"
> field in the docker compose file.

Once the containers are running, bring up akhq using the url
http://localhost:8081/ in the browser. Navigate to _Topics > Create a topic_
and create topic the "user.Updates".

In akhq, navigate to _Topics > user.Updates > Produce to topic_ and publish the message:

```
{
  "name" : "Rick Sanchez",
  "type" : "user",
  "id": "5LJT59HYR2",
  "email" : "rickc137@rickmail.com",
  "phone": "+1 157 556 8560",
  "membership" : {
    "status": "premium",
    "category": "family"
  },
  "address": "Smith Residence, Washington, USA"
}
```

If indexing is successful, the application log should publish:

_Indexed update of user Rick Sanchez_

Search the newly indexed document using:

> http://localhost:9200/user_updates_1/_search

The documents can also be explored using Kibana.








