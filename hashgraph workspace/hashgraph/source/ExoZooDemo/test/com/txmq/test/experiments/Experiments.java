package com.txmq.test.experiments;

import java.lang.reflect.Field;

import org.junit.Test;

import util.hash.MurmurHash3;

public class Experiments {

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException {
		int seed = "Aviator".hashCode();
		System.out.println("Seed: " + seed);
		
		String ic1name = InnerClass1.class.getName();
		String ic2name = InnerClass2.class.getName();
		int ic1hash = MurmurHash3.murmurhash3_x86_32(ic1name, 0, ic1name.length(), seed);
		int ic2hash = MurmurHash3.murmurhash3_x86_32(ic2name, 0, ic1name.length(), seed);
		
		Field[] fields = InnerClass1.class.getFields();
		for (Field field : fields) {
			String fieldValue = (String) field.get(null);
			System.out.println(ic1name + "." + field.getName() + ": (" + ic1hash + "," +
					MurmurHash3.murmurhash3_x86_32(fieldValue, 0, fieldValue.length(), seed) + ")");
		}
		
		fields = InnerClass2.class.getFields();
		for (Field field : fields) {
			String fieldValue = (String) field.get(null);
			System.out.println(ic1name + "." + field.getName() + ": (" + ic2hash + "," +
					MurmurHash3.murmurhash3_x86_32(fieldValue, 0, fieldValue.length(), seed) + ")");
		}
		System.out.println("Done");
	}
	
	private class InnerClass1 {
		public static final String ADD = "add";
		public static final String EDIT = "edit";
		public static final String DELETE = "delete";		
	}
	
	private class InnerClass2 {
		public static final String ADD = "add";
		public static final String EDIT = "edit";
		public static final String DELETE = "delete";
	}
}
