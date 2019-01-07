package com.miamioh.registertaxiclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.miamioh.registertaxiclient.config.params.KafkaProducerConfigParams;
import com.miamioh.registertaxiclient.model.request.Taxi;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
	
	@Autowired
	private KafkaProducerConfigParams kafkaProducerConfigParams;
	
	@Bean
	@DependsOn("kafkaProducerConfigParams")
    public ProducerFactory<String, Taxi> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigParams.getProducerConfig());
    }
 
    @Bean
    public KafkaTemplate<String, Taxi> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
