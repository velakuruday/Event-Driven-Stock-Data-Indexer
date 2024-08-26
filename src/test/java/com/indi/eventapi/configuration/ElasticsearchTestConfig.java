package com.indi.eventapi.configuration;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@TestConfiguration
@Slf4j
public class ElasticsearchTestConfig {

    public ElasticsearchTestConfig() {

        DockerImageName DOCKER_IMAGE = DockerImageName.parse("elasticsearch:8.15.0");
        GenericContainer<?> container = new GenericContainer<>(DOCKER_IMAGE);
        container.withExposedPorts(9200);
        container.setPortBindings(List.of("9200:9200"));
        container.withEnv("xpack.security.enabled", "false");
        container.withEnv("discovery.type", "single-node");
        container.withLabel("indi-dev", "velakuruday");
        container.setWaitStrategy((new HttpWaitStrategy())
                .forPort(9200)
                .forStatusCodeMatching(response -> response == 200 || response == 401));
        container.start();
        log.info("Started Test ES container at {}", 9200);
    }

    @Bean
    @Primary
    public ElasticsearchClient testElasticsearchClient() {

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}
