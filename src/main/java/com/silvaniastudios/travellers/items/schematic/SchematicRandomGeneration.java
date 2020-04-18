package com.silvaniastudios.travellers.items.schematic;

import java.util.Random;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;

public class SchematicRandomGeneration {

	public static final int MAX = 100;
	public static final int MIN = 5;

	public static SchematicStats generateRandomStats(SchematicTypeEnum type,
			SchematicRarityEnum rarity) {
		float[] stats = generateRandomArrayWithTotal(type.statNo, rarity.getTotal(type.statNo));
		String[] names = SchematicTypeEnum.getStatNames(type);

		SchematicStats combinedLists = new SchematicStats();

		for (int i = 0; i < stats.length; i++) {
			combinedLists.add(new SchematicStatisticSlot(names[i], stats[i]));
		}

		return combinedLists;
	}
	
	/**
	 * Generates array of random numbers. Originally made by Ziwix in MATLAB
	 * adapted to Java
	 * 
	 * @author Ziwix
	 * @param arrayLength
	 * @param arrayTotal
	 * @return float[] of length `arrayLength`, sums to `arrayTotal`
	 */
	public static float[] generateRandomArrayWithTotal(int arrayLength, int arrayTotal) {

		float[] stats = new float[] { MAX + 1, MAX + 1, MAX + 1, MAX + 1, MAX + 1 };
		while (comparisonAny(stats, MAX, MIN)) {
			stats = randomStatArray(arrayLength);

			stats = elementwiseMultiplication(elementwiseDivison(stats, sumArray(stats)), arrayTotal);
		}
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

	public static boolean comparisonAny(float[] arr, int max, int min) {
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
	
	public static int indexOf(String[] arr, String str) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) {
				return i;
			}
		}

		return -1;
	}
}
