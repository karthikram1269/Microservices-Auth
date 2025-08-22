package com.zomato.gk.controller;

import java.util.Map;
 
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zomato.gk.service.ZomatoAgentDeliveryService;

import lombok.RequiredArgsConstructor;
 
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zomato/delivery")
public class ZomatoAgentController {
 
	private final ZomatoAgentDeliveryService zomatoAgentDeliveryService;
	
	
	@PatchMapping("/getLocationOfDeliveryAgent")
	public Map<String,String> getUpdatedLocation() throws InterruptedException
	{	
		int range=100;
		
		while(range>0)
		{
			zomatoAgentDeliveryService.updateDeleiveryAgentLocation(Math.random()+","+Math.random());
			Thread.sleep(1000);
			range--;
		}
		return Map.of("message","Location Updated every Second");
		
	}
	
	
	
}
 
 