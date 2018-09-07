package com.txmq.exo.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Core component used by the framework to route messages to destinations.  "Exo classic" used
 * an "extensible enum" kind of construct which has proven to be less flexible than we'd like, 
 * and heavier over the wire.  The new implementation separates the transaction type values from
 * the "validator" code that maintains the master list of transaction types/values.
 * 
 * Additionally, we've added a namespace parameter which can be used to ensure that there are no
 * collisions in type values.  The best way to use the namespace is to supply it as a string,
 * using the base package for the application, e.g. "com.txmq.exo.ExoZooDemo".  Internally, that
 * package will get hashed to a (most likely) unique integer value representing the namespace.
 * Similarly, best practice for values is to use string transaction type values - they too will
 * be hashed.  If you're concerned about the amount of data going over the as JSON, you can continue
 * to use integer values and maintain them manually.
 * 
 * @author craigdrabik
 *
 */
public class ExoTransactionTypeValue {
	/**
	 * integer value of the transaction type's namespace.  
	 * This will be generated via hash if a string is supplied to the constructor.
	 */
	@JsonProperty("ns")
	private int namespace;
	
	/**
	 * Public getter for namespace value
	 * @return
	 */
	public int getNamespace() {
		return namespace;
	}
	
	/**
	 * integer value of the transaction type.  
	 * This will be generated via hash if a string is supplied to the constructor.
	 */
	private int value;
	
	/**
	 * Public getter for transaction type value
	 * @return
	 */
	public int getValue() {
		return value;
	}

	/**
	 * int/int constructor, useful in cases where you manually manage 
	 * the integer values of your transaction types and namespaces.
	 * @param namespace
	 * @param value
	 */
	public ExoTransactionTypeValue(int namespace, int value) {
		this.namespace = namespace;
		this.value = value;
		
		if (!ExoTransactionTypes.isValid(this)) {
			throw new IllegalStateException(
				"Transaction type (" + namespace + "," + value + ") is invalid"
			);
		}
	}
	
	/**
	 * String/int constructor, useful in cases where you manually manage the
	 * integer values of your transaction types, but not your namespaces
	 * @param namespace
	 * @param value
	 */
	public ExoTransactionTypeValue(String namespace, int value) {
		this(namespace.hashCode(), value);		
	}
	
	/**
	 * String/String constructor.  This is the default constructor.  Integer
	 * values for namespace and value will be derived using hashing.
	 * @param namespace
	 * @param value
	 */
	public ExoTransactionTypeValue(String namespace, String value) {
		this(namespace.hashCode(),value.hashCode());
		
	}

	@Override
	public boolean equals(Object obj) {
		return this.equals((ExoTransactionTypeValue) obj);
	}
	
	/**
	 * Method for Java to use to see if two ExoTransactionType oinstances have the same value.
	 */
	public boolean equals(ExoTransactionTypeValue obj) {
		return this.namespace == obj.namespace && this.value == obj.value;
	}
	
}
