package com.indi.eventapi.configuration.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient getElasticsearchClient() {
        RestClientBuilder restClient = RestClient.builder(new HttpHost("elasticsearch", 9200));

        return new RestHighLevelClient(restClient);
    }
}
