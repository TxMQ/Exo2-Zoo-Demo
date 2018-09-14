package com.txmq.exo.config.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txmq.exo.config.IConfiguration;
import com.txmq.exo.config.MessagingConfig;

public class MessagingConfigProcessor implements IConfigurationProcessor {

	@Override
	public List<String> keys() {
		return Arrays.asList("rest", "socket");
	}

	@Override
	public IConfiguration readConfiguration(JsonNode jsonFragment) {
		try {
			return new ObjectMapper().treeToValue(jsonFragment, MessagingConfig.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
