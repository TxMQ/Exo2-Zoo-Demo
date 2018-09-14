package com.txmq.exo.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.reflections.Reflections;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txmq.exo.config.parsers.IConfigurationProcessor;

public class AviatorConfig {

	private static final Map<String, IConfiguration> configs = new HashMap<String, IConfiguration>();
	private static boolean initialized = false;
	
	public static void loadConfiguration(String path) {
		Map<String, IConfigurationProcessor> processors = new HashMap<String, IConfigurationProcessor>();
		Reflections reflections = new Reflections();
		Set<Class<? extends IConfigurationProcessor>> processorClasses = reflections.getSubTypesOf(IConfigurationProcessor.class);
		for (Class<? extends IConfigurationProcessor> processorClass : processorClasses) {
			try {
				IConfigurationProcessor processor = processorClass.newInstance();
				for (String key : processor.keys()) {
					processors.put(key, processor);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JsonNode jsonNode = null;
		try {
			jsonNode = new ObjectMapper().readTree(new File(path));
			Iterator<Entry<String, JsonNode>> i = jsonNode.fields();
			while(i.hasNext()) {
				Entry<String, JsonNode> node = i.next();
				if (processors.containsKey(node.getKey())) {
					configs.put(node.getKey(), processors.get(node.getKey()).readConfiguration(node.getValue()));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialized = true;
	}
	public static IConfiguration get(String key) {
		if (!initialized) {
			loadConfiguration("aviator-config.json");
		}
		return configs.get(key);
	}
}
