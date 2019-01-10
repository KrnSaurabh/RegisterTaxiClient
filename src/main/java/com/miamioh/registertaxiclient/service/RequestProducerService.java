package com.miamioh.registertaxiclient.service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.miamioh.registertaxiclient.model.request.Taxi;

import lombok.extern.slf4j.Slf4j;
//41.892072635,-87.62887415700001

@Service
@Slf4j
public class RequestProducerService {
	
	@Autowired
    private KafkaTemplate<String, Taxi> kafkaTemplate;
	
	@Value(value = "${kafka.topic}")
    private String topicName;
	
	
	public void createAndSendRequestAsync(String input) {
		log.info("Inside Register Taxi Request Producer Service");
		String[] tokens = input.split(",");
		Taxi taxi = new Taxi();
		String taxiId = UUID.randomUUID().toString();
		taxi.setTaxiId(taxiId);
		taxi.setLatitude(Double.valueOf(tokens[0]));
		taxi.setLongitude(Double.valueOf(tokens[1]));
		taxi.setModel("Honda City");
		taxi.setNoOfPassenger(new AtomicInteger(0));
		taxi.setTaxiNumber("9211");
		log.info("Generated Taxi Object: "+taxi);
		ListenableFuture<SendResult<String, Taxi>> future = kafkaTemplate.send(topicName, taxi);
		StopWatch watch = new StopWatch();
		watch.start();
		future.addCallback(
				new ListenableFutureCallback<SendResult<String, Taxi>>() {

					@Override
					public void onSuccess(
							SendResult<String, Taxi> result) {
						log.info("Successfully published message to the kafka topic with key={} and offset={}",
								taxi, result.getRecordMetadata().offset());

						watch.stop();
						log.info("total time taken to send the message to topic :{}", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}

					@Override
					public void onFailure(Throwable ex) {
						log.error("Unable to publish message to the kafka topic with key={}",
								taxi, ex);

						watch.stop();
						log.info("total time taken to send the message to topic :", taxi.getTaxiId(), 
								watch.getTotalTimeMillis());
					}
				});
	}

}
