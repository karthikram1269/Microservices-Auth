package com.zomato.gk.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZomatoAgentDeliveryService {

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void updateDeleiveryAgentLocation(String string) {
		
		kafkaTemplate.send("zomato-delivery-agent", string);
	}

}
