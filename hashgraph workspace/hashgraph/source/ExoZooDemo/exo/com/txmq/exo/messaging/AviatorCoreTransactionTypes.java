package com.txmq.exo.messaging;

import com.txmq.exo.messaging.annotations.TransactionTypes;

@TransactionTypes(namespace="AviatorCoreTransactionTypes")
public class AviatorCoreTransactionTypes {
	public static final String ACKNOWLEDGE = "ACKNOWLEDGE"; 
	public static final String ANNOUNCE_NODE = "ANNOUNCE_NODE";
	public static final String RECOVER_STATE = "RECOVER_STATE";
	public static final String SHUTDOWN = "SHUTDOWN";
}
