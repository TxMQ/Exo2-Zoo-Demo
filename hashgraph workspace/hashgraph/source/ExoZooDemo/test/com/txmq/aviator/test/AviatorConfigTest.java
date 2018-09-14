package com.txmq.aviator.test;

import org.junit.Test;

import com.txmq.exo.config.AviatorConfig;
import com.txmq.exo.config.MessagingConfig;

public class AviatorConfigTest {

	@Test
	public void test() {
		AviatorConfig.loadConfiguration("../../aviator-config.json");
		MessagingConfig messagingConfig = (MessagingConfig) AviatorConfig.get("rest");
		System.out.println("!");
	}
}
