package com.silvaniastudios.travellers.schematic;

import java.util.Random;

public class RandomSchematicGen {
	
	public static final int MAX = 100;
	public static final int MIN = 5;
	
	public static float[] generateRandomStats (SchematicTypeEnum type, SchematicRarityEnum rarity) {
		
		float[] stats = new float[] {MAX + 1, MAX + 1, MAX + 1, MAX + 1, MAX + 1};
		while (comparisonAny(stats, MAX, MIN)) {
			stats = randomStatArray(type.statNo);
			
			stats = elementwiseMultiplication(
						elementwiseDivison(stats, sumArray(stats)),
						rarity.getTotal(type.statNo)
					);
		}
		//System.out.println(stats);
		return stats;
	}
	
	public static float[] randomStatArray(int statNo) {
		Random random = new Random();
		float[] stats = new float[statNo];
		
		for (int i = 0; i < statNo; i++) {
			stats[i] = (1 + random.nextFloat() * (5 - 1)) + 0.185F;
		}
		
		return stats;
	}
	
	public static boolean comparisonAny(float [] arr, int max, int min) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= max) {
				return true;
			}
			
			if (arr[i] <= min) {
				return true;
			}
		}
		
		return false;
	}
	
	public static float[] elementwiseDivison(float[] arr, float num) {
		for (int i = 0; i < arr.length; i++) {
        arr[i] = arr[i] / num;
      }
      return arr;
	}
	
	public static float[] elementwiseMultiplication(float[] arr, float num) {
		for (int i = 0; i < arr.length; i++) {
        arr[i] = arr[i] * num;
      }
      return arr;
	}
	
	public static float sumArray(float[] arr) {
		float output = 0;
		
		for (int i = 0; i < arr.length; i++) {
			output += arr[i];
		}
		
		return output;
	}

}
