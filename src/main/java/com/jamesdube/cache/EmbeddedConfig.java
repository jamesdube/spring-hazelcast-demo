package com.jamesdube.cache;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedConfig {

    @Bean
    public TransactionService transactionService(TransactionRepository transactionRepository, CacheManager cacheManager){
        return new TransactionServiceImpl( transactionRepository, hazelcastInstance(), cacheManager);
    }

    @Bean
    public HazelcastInstance hazelcastInstance(){
        return Hazelcast.newHazelcastInstance(cacheConfig());
    }


    @Bean
    public Config cacheConfig(){

        Config config = new Config();
        config.getManagementCenterConfig().setEnabled(true).setUrl("http://localhost:8080/hazelcast-mancenter/");
        config.setInstanceName("cache-demo");
        config.getGroupConfig().setName("cache-demo");


        config.addMapConfig(new MapConfig().setName("transactions")
                .setMaxIdleSeconds(12000)
                .setTimeToLiveSeconds(24000)
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE)));

        return config;
    }
}
