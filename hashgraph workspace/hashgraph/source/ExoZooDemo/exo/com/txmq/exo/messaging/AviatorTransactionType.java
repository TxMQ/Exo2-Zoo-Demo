package com.txmq.exo.messaging;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.reflections.Reflections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.txmq.exo.messaging.annotations.TransactionType;
import com.txmq.exo.messaging.annotations.TransactionTypes;

import util.hash.MurmurHash3;

public class AviatorTransactionType {

	/**
	 * Seed value for the hashing algorithm used internally. This value 
	 * was derived using "Aviator".hashCode() and fixed to guard against 
	 * the implementation of Java's String.hashCode() changing. 
	 */
	private static final int hashSeed = 1036411402;

	/**
	 * Convenience method wrapping the Murmur3 implementation used internally.
	 * @return a 32-bit integer hash of the supplied string
	 */
	private static int hash(String message) {
		return MurmurHash3.murmurhash3_x86_32(message, 0, message.length(), hashSeed);
	}
	
	private static BidiMap<String, Integer> namespaceHashes = new DualHashBidiMap<String, Integer>();
	
	private static BidiMap<String, Integer> transactionTypeHashes = new DualHashBidiMap<String, Integer>();
	
	private static HashSet<AviatorTransactionType> transactionTypes= new HashSet<AviatorTransactionType>();
	
	//TODO:  Add a parameter to exo-config.json to turn duplicate detection on and off
	private static void registerTransactionType(String namespace, String value) {
		
		if (!namespaceHashes.containsKey(namespace)) {
			namespaceHashes.put(namespace, hash(namespace));
		}
		
		if (!transactionTypeHashes.containsKey(value)) {
			transactionTypeHashes.put(value, hash(value));
		}
		
		AviatorTransactionType transactionType = new AviatorTransactionType(namespace, value);
		transactionTypes.add(transactionType);
	}
	
	/**
	 * Inspects the classpath for classes annotated with @TransactionTypes and 
	 * registers public static strings as transaction types.  This behavior can 
	 * be modified to include only those items annotated with @TransactionType 
	 * if the class is annotated with @TransactionTypes(onlyAnnotatedValues=true)
	 * 
	 *  This method is called by the framework during bootstrapping - developers 
	 *  should not call this method themselves.
	 */
	//TODO:  Offer optimization through a whitelist of transaction type packages in exo-config.json
	public static void initialize() throws ReflectiveOperationException {
		Reflections reflections = new Reflections();
		Set<Class<?>> transactionTypeClasses = reflections.getTypesAnnotatedWith(TransactionTypes.class);
		for (Class<?> ttc : transactionTypeClasses) {
			TransactionTypes tta = ttc.getAnnotation(TransactionTypes.class);
System.out.println(ttc.getName());
			String namespace;
			if (tta.namespace().equals("")) {
				namespace = tta.namespace();
			} else {
				namespace = ttc.getName();
			}
			int classHash = hash(namespace);
			namespaceHashes.putIfAbsent(namespace, classHash);
				
			for (Field field : ttc.getFields()) {
				if (	field.getType().equals(String.class) && 
						Modifier.isStatic(field.getModifiers()) && 
						(!tta.onlyAnnotatedValues() || field.isAnnotationPresent(TransactionType.class))
					) 
				{
					String typeValue = (String) field.get(null);
					int typeHash = hash(typeValue);
					transactionTypeHashes.putIfAbsent(typeValue, typeHash);
					transactionTypes.add(new AviatorTransactionType(ttc.getName(), typeValue));
				}
			}
		}
	}
	
	/*
	 * Instance stuff
	 */
	
	/**
	 * Holds the hashed value of the namespace string for this transaction type
	 */
	private int ns;
	
	/**
	 * Holds the hashed value of the transaction type value
	 */
	private int value;
	
	//TODO:  Test for validity
	public void setNamespace(int namespace) {
		this.ns = namespace;
	}
	
	//TODO: test for validity
	public void setNamespace(String namespace) {
		this.ns = hash(namespace);
	}
	
	@JsonProperty("ns")
	public int getNamespaceHash() {
		return this.ns;
	}
	
	public String getNamespace() {
		return namespaceHashes.getKey(this.ns);		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = hash(value);
	}
	
	@JsonProperty("value")
	public int getValueHash() {
		return this.value;
	}
	
	public String getValue() {
		return namespaceHashes.getKey(this.value);
	}
	
	public AviatorTransactionType(String namespace, String value) {
		this.setNamespace(namespace);
		this.setValue(value);
	}
	
	public AviatorTransactionType(int namespace, int value) {
		this.setNamespace(namespace);
		this.setValue(value);
	}
	
	public boolean isValid() {
		return transactionTypes.contains(this);
	}

	@Override
	public boolean equals(Object obj) {
		AviatorTransactionType that = (AviatorTransactionType) obj;
		return this.ns == that.ns && this.value == that.value;
	}
}
