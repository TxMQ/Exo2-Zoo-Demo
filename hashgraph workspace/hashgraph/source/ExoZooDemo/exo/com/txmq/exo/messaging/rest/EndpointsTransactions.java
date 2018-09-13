package com.txmq.exo.messaging.rest;

import com.txmq.exo.messaging.AviatorCoreTransactionTypes;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.pipeline.PlatformEvents;
import com.txmq.exo.pipeline.metadata.ExoHandler;
import com.txmq.exo.core.ExoState;

/**
 * Implements the Endpoints API announcement transaction.
 */
public class EndpointsTransactions {
	@ExoHandler(namespace="AviatorCoreTransactionTypes", 
				transactionType=AviatorCoreTransactionTypes.ANNOUNCE_NODE, 
				events= {PlatformEvents.executeConsensus})
	public void announceNode(ExoMessage message, ExoState state, boolean consensus) {
		state.addEndpoint((String) message.payload);
	}
}
