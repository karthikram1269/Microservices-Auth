package com.user.gk;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationServer {

	@KafkaListener(topics="zomato-delivery-agent",groupId = "user-group")
	public void zomatoAgentLocaion(String updateLocation) {
		
		log.warn(updateLocation);
	}
}
