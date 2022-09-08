package com.indi.eventapi.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ElasticsearchTestConfig {

    @Bean
    @Primary
    public RestHighLevelClient testElasticsearchClient() {
        RestClientBuilder restClient = RestClient.builder(new HttpHost("localhost", 9200));

        return new RestHighLevelClient(restClient);
    }
}
