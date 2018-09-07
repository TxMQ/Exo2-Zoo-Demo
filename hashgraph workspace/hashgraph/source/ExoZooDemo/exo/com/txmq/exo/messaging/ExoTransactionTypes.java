package com.txmq.exo.messaging;

import java.util.HashMap;
import java.util.Map;

public class ExoTransactionTypes {

	private static Map<Integer, Map<Integer, Boolean>> values;
	private static boolean initialized = false;
	
	public static boolean isValid(ExoTransactionTypeValue transactionType) {
		if (!initialized) {
			throw new IllegalStateException("Attempted to validate a transaction type before initialization");
		}
		return 	values.containsKey(transactionType.getNamespace()) && 
				values.get(transactionType.getNamespace()).containsKey(transactionType.getValue()); 
	}
	
	public static void initialize() {
		values = new HashMap<Integer, Map<Integer, Boolean>>();
		initialized = true;
	}
	
	public static void addTransactionType(String namespace, String value) {
		addTransactionType(namespace.hashCode(), value.hashCode());
	}
	
	public static void addTransactionType(int namespace, int value) {
		if (!initialized) {
			throw new IllegalStateException("Attempted to validate a transaction type before initialization");
		}
		
		if (!values.containsKey(namespace)) {
			values.put(namespace, new HashMap<Integer, Boolean>());
		}
		
		if (values.get(namespace).containsKey(value)) {
			throw new IllegalStateException(
					"A collision occurred while registering transaction type (" + namespace + "," + value + ")"
			);
		}
		
		values.get(namespace).put(value, true);
	}
}
