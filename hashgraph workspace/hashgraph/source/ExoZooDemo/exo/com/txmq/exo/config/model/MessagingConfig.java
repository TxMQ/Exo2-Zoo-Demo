package com.txmq.exo.config;

public class MessagingConfig implements IConfiguration {
	public Integer port;
	public Integer derivedPort;
	public boolean secured;
	public String[] handlers;
	public KeystoreConfig clientKeystore;
	public KeystoreConfig clientTruststore;
	public KeystoreConfig serverKeystore;
	public KeystoreConfig serverTruststore;
}
