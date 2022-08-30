package com.indi.eventapi.configuration.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaAdminUtils {

    private final Map<String, Object> kafkaConfig;

    public KafkaAdminUtils(
        KafkaAdmin kafkaAdmin,
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        kafkaConfig = new HashMap<>();
        kafkaConfig.putAll(kafkaAdmin.getConfigurationProperties());
        kafkaConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaConfig.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000);
    }

    public boolean isConnectionHealthy() {
        try (var adminClient = AdminClient.create(kafkaConfig)) {
            var clusterInfoRequest = new DescribeClusterOptions().timeoutMs(2000);
            var clusterInfo = adminClient.describeCluster(clusterInfoRequest);
            var clusterId = clusterInfo.clusterId();
            var nodes = clusterInfo.nodes();
            log.info(String.format(
                "KafkaProducer.isConnectionHealthy: Yes! Info: cluster %s with %d nodes",
                clusterId.get(),
                nodes.get().size()
            ));
            return true;
        } catch (InterruptedException | ExecutionException e) {
            log.info("KafkaProducer.isConnectionHealthy: No! Error: " + e.getMessage());
            return false;
        }
    }

}
