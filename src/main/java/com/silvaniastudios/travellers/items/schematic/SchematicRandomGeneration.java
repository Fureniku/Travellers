package com.silvaniastudios.travellers.items.schematic;

import java.util.ArrayList;
import java.util.Random;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;

public class SchematicRandomGeneration {

	public static final int MAX = 100;
	public static final int MIN = 5;

	public static ArrayList<SchematicStatisticSlot> generateRandomStats(SchematicTypeEnum type,
			SchematicRarityEnum rarity) {
		float[] stats = generateRandomArrayWithTotal(type.statNo, rarity.getTotal(type.statNo));
		String[] names = SchematicTypeEnum.getStatNames(type);

		ArrayList<SchematicStatisticSlot> combinedLists = new ArrayList<SchematicStatisticSlot>();

		for (int i = 0; i < stats.length; i++) {
			combinedLists.add(new SchematicStatisticSlot(names[i], stats[i]));
		}

		return combinedLists;
	}

	
	public static ArrayList<SchematicCraftingSlot> generateEngineCosts (SchematicTypeEnum type, ArrayList<SchematicStatisticSlot> stats) {
	
		SchematicStatisticSlot statResilience = SchematicStatisticSlot.findStat(stats, "travellers.stat.resilience.name");
		SchematicStatisticSlot statPower = SchematicStatisticSlot.findStat(stats, "travellers.stat.power.name");
		SchematicStatisticSlot statBoost = SchematicStatisticSlot.findStat(stats, "travellers.stat.boost.name");
		SchematicStatisticSlot statFuelEff = SchematicStatisticSlot.findStat(stats, "travellers.stat.fueleff.name");
		SchematicStatisticSlot statOhl = SchematicStatisticSlot.findStat(stats, "travellers.stat.ohl.name");
		
		SchematicCraftingSlot slotCasing = new SchematicCraftingSlot("travellers.slot.casing.name");
		slotCasing.amount = 2 * (int)(statResilience.amount + statPower.amount + statBoost.amount); //2 x (Resilience + Power + Spinup)
		slotCasing.type = ""; // Dependent on tier / resilience value
		
		SchematicCraftingSlot slotCombus = new SchematicCraftingSlot("travellers.slot.combus.name"); 
		slotCombus.amount = 2 * (int)(statPower.amount + statFuelEff.amount + statOhl.amount); //2 x (Power + Fuel efficiency + Overheat)
		slotCombus.type = "travellers.material.metal"; // Always metal
		
		SchematicCraftingSlot slotMech = new SchematicCraftingSlot("travellers.slot.mech.name");
		slotMech.amount = 2 * (int)(statPower.amount + statFuelEff.amount); //2 x (Power + Fuel efficiency)
		slotCombus.type = "travellers.material.metal"; // Always metal
		
		SchematicCraftingSlot slotProp = new SchematicCraftingSlot("travellers.slot.prop.name");
		slotProp.amount = 2 * (int)(statBoost.amount + statOhl.amount); //2 x (Spinup + Overheat)
		slotProp.type = ""; // Is Prop Vowel?
		
		ArrayList<SchematicCraftingSlot> slots = new ArrayList<SchematicCraftingSlot>();
		slots.add(slotCasing);
		slots.add(slotCombus);
		slots.add(slotMech);
		slots.add(slotProp);
		return slots;

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
