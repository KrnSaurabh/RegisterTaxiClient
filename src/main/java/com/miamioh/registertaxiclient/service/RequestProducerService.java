package com.miamioh.registertaxiclient.service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.miamioh.registertaxiclient.model.request.Taxi;
//41.892072635,-87.62887415700001

@Service
public class RequestProducerService {
	
	@Autowired
    private KafkaTemplate<String, Taxi> kafkaTemplate;
	
	@Value(value = "${kafka.topic}")
    private String topicName;
	
	
	public void createAndSendRequestAsync(String input) {
		
		String[] tokens = input.split(",");
		Taxi taxi = new Taxi();
		String taxiId = UUID.randomUUID().toString();
		taxi.setTaxiId(taxiId);
		taxi.setLatitude(Double.valueOf(tokens[0]));
		taxi.setLongitude(Double.valueOf(tokens[1]));
		taxi.setModel("Honda City");
		taxi.setNoOfPassenger(new AtomicInteger(0));
		taxi.setTaxiNumber("9211");
		kafkaTemplate.send(topicName, taxi);
		
	}

}
