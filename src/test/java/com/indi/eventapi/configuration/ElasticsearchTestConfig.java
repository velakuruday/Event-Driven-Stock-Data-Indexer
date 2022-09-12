package com.indi.eventapi.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
@Slf4j
public class ElasticsearchTestConfig {

    private final Integer PORT = 9200;

    private final GenericContainer<?> container;

    public ElasticsearchTestConfig() {

        DockerImageName DOCKER_IMAGE = DockerImageName.parse("elasticsearch:7.13.1");
        container = new GenericContainer<>(DOCKER_IMAGE);
        container.withExposedPorts(PORT);
        container.withEnv("xpack.security.enabled", "false");
        container.withEnv("discovery.type", "single-node");
        container.withLabel("indi-dev", "velakuruday");
        container.withReuse(true);
        container.setWaitStrategy((new HttpWaitStrategy())
                .forPort(PORT)
                .forStatusCodeMatching(response -> response == 200 || response == 401));
        container.start();
        log.info("Started Test ES container at {}", PORT);
    }

    @Bean
    @Primary
    public RestHighLevelClient testElasticsearchClient() {

        RestClientBuilder restClient = RestClient.builder(
                new HttpHost(container.getHost(), container.getMappedPort(PORT)));

        return new RestHighLevelClient(restClient);
    }
}
