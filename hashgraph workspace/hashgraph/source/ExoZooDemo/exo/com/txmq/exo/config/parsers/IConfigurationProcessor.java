package com.txmq.exo.config.parsers;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.txmq.exo.config.IConfiguration;

public interface IConfigurationProcessor {
	public List<String> keys();
	public IConfiguration readConfiguration(JsonNode jsonFragment);
}
