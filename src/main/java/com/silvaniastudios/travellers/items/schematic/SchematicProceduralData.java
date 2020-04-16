package com.silvaniastudios.travellers.items.schematic;

import java.util.ArrayList;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;

/**
 * 
 * @author jamesm2w
 */
public class SchematicProceduralData {

	/**
	 * Credit to Node, Ziwix, Machine Maker, Kruft, Docfreeman, Vloshko,
	 * jamesm2w all from the community and Matt Foster from Bossa Studios (Bossa
	 * Moster) For compiling the many engine schematics to make sure all
	 * possible part names were covered, and thanks to Moster for confirming
	 * which were actually bugs :)
	 * 
	 * @param statSlots
	 * @param rarity
	 * @return String representing the name of the engine
	 */
	public static String getEngineName(ArrayList<SchematicStatisticSlot> statSlots, SchematicRarityEnum rarity) {
		// Method was originally designed to work with a float array. So this
		// just converts it to that.
		float[] stats = new float[statSlots.size()];
		for (SchematicStatisticSlot slot : statSlots) {
			int indexOfStat = indexOf(ENGINE_STAT_NAMES, slot.name);
			stats[indexOfStat] = slot.amount;
		}

		String casingName = "", mountType = "m", propMountName = "", powerNum = "", propType = "p", propName = "";

		for (int i = 0; i < EngineCasingList.length; i++) {
			EngineCasing currentCasing = EngineCasingList[i];
			if (currentCasing.tier <= rarity.tier
					&& indexOfMax(stats) == indexOf(ENGINE_STAT_NAMES, currentCasing.stat)) {
				casingName = currentCasing.name;
				mountType = currentCasing.mountType;
			}
		}

		float power = stats[indexOf(ENGINE_STAT_NAMES, "travellers.stat.power.name")];
		for (int i = 0; i < EnginePropMountList.length; i++) {
			if (power > EnginePropMountList[i].minPower && EnginePropMountList[i].mountType.equals(mountType)) {

				propMountName = EnginePropMountList[i].name;
				powerNum = String.valueOf(Math.round(power - EnginePropMountList[i].minPower));
				propType = EnginePropMountList[i].propType;

				break;
			}
		}

		float boost = stats[indexOf(ENGINE_STAT_NAMES, "travellers.stat.boost.name")];
		for (int i = 0; i < EnginePropList.length; i++) {
			if (boost >= EnginePropList[i].minBoost && EnginePropList[i].propType.equals(propType)) {

				propName = EnginePropList[i].letter;
				propType = EnginePropList[i].propType;

				break;
			}
		}

		return casingName + " " + propMountName + " " + propName + powerNum;
	}

	public static int indexOfMax(float[] stats) {
		int index = 0;
		float maxValue = stats[0];
		for (int i = 0; i < stats.length; i++) {
			if (stats[i] > maxValue) {
				maxValue = stats[i];
				index = i;
			}
		}

		return index;
	}

	public static int indexOf(String[] arr, String str) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) {
				return i;
			}
		}

		return -1;
	}

	public static final String[] ENGINE_SLOT_NAMES = new String[] { "travellers.slot.casing.name",
			"travellers.slot.combus.name", "travellers.slot.mech.name", "travellers.slot.prop.name" };

	public static final String[] ENGINE_STAT_NAMES = new String[] { "travellers.stat.resilience.name",
			"travellers.stat.power.name", "travellers.stat.boost.name", "travellers.stat.fueleff.name",
			"travellers.stat.ohl.name" };

	public static final String[] WING_STAT_NAMES = new String[] { "travellers.stat.resilience.name",
			"travellers.stat.airbrake.name", "travellers.stat.wingpower.name" };

	public static final String[] CANNON_STAT_NAMES = new String[] { "travellers.stat.resilience.name",
			"travellers.stat.range.name", "travellers.stat.frag.name", "travellers.stat.cannonohl.name",
			"travellers.stat.rof.name" };

	public static final String[] SWIVELCANNON_STAT_NAMES = new String[] { "travellers.stat.resilience.name",
			"travellers.stat.range.name", "travellers.stat.frag.name", "travellers.stat.cannonohl.name",
			"travellers.stat.rof.name" };

	public static final String[] FIXED_STAT_NAMES = new String[] { "travellers.stat.resilience.name" };

	public static final EngineCasing[] EngineCasingList = new EngineCasing[] {
			new EngineCasing(1, "Piped", "travellers.stat.boost.name", "w"),
			new EngineCasing(1, "Squareframe", "travellers.stat.resilience.name", "w"),
			new EngineCasing(1, "Scrapheap", "travellers.stat.fueleff.name", "m"),
			new EngineCasing(1, "Scrapheap", "travellers.stat.ohl.name", "m"),
			new EngineCasing(1, "Boxpile", "travellers.stat.power.name", "w"),
			new EngineCasing(2, "Spinshaft", "travellers.stat.boost.name", "w"),
			new EngineCasing(2, "Trishell", "travellers.stat.resilience.name", "w"),
			new EngineCasing(2, "Reliant", "travellers.stat.fueleff.name", "w"),
			new EngineCasing(2, "Ventilated", "travellers.stat.ohl.name", "w"),
			new EngineCasing(2, "Spark", "travellers.stat.power.name", "w"),
			new EngineCasing(3, "Dervish", "travellers.stat.boost.name", "m"),
			new EngineCasing(3, "Pinnacle", "travellers.stat.resilience.name", "m"),
			new EngineCasing(3, "Iceberg", "travellers.stat.fueleff.name", "m"),
			new EngineCasing(3, "Iceberg", "travellers.stat.ohl.name", "m"),
			new EngineCasing(3, "Stallion", "travellers.stat.power.name", "m"),
			new EngineCasing(4, "Godfellow", "travellers.stat.boost.name", "m"),
			new EngineCasing(4, "Apotheus", "travellers.stat.resilience.name", "m"),
			new EngineCasing(4, "Sunstream", "travellers.stat.fueleff.name", "m"),
			new EngineCasing(4, "Sunstream", "travellers.stat.ohl.name", "m"),
			new EngineCasing(4, "Ironforge", "travellers.stat.power.name", "m") };

	public static final EnginePropMount[] EnginePropMountList = new EnginePropMount[] {
			new EnginePropMount(59, "Starcaster", "m", "j"), new EnginePropMount(50, "Cloudchaser", "m", "j"),
			new EnginePropMount(44, "Supreme", "m", "p"), new EnginePropMount(39, "Elite", "m", "p"),
			new EnginePropMount(34, "Hurricane", "m", "p"), new EnginePropMount(29, "Tornado", "m", "p"),
			new EnginePropMount(24, "Cyclone", "m", "p"), new EnginePropMount(19, "Pacesetter", "m", "p"),
			new EnginePropMount(14, "Rival", "m", "p"), new EnginePropMount(9, "Populus", "m", "p"),
			new EnginePropMount(0, "Steamer", "m", "p"), new EnginePropMount(27, "Workhorse", "w", "p"),
			new EnginePropMount(19, "Cranker", "w", "p"), new EnginePropMount(14, "Smokie", "w", "p"),
			new EnginePropMount(9, "Crudbait", "w", "p"), new EnginePropMount(0, "Rustbucket", "w", "p") };

	public static final EngineProp[] EnginePropList = new EngineProp[] { new EngineProp(30, "Z", "j"),
			new EngineProp(0, "X", "j"), new EngineProp(58, "N", "p"), new EngineProp(48, "M", "p"),
			new EngineProp(40, "H", "p"), new EngineProp(31, "F", "p"), new EngineProp(25, "B", "p"),
			new EngineProp(20, "U", "p"), new EngineProp(15, "O", "p"), new EngineProp(10, "E", "p"),
			new EngineProp(0, "A", "p") };

	public static class EngineCasing {
		int tier;
		String name;
		String stat;
		String mountType;

		public EngineCasing(int tier, String name, String stat, String mountType) {
			this.tier = tier;
			this.name = name;
			this.stat = stat;
			this.mountType = mountType;
		}
	}

	public static class EnginePropMount {
		int minPower;
		String name;
		String mountType;
		String propType;

		public EnginePropMount(int minPower, String name, String mountType, String propType) {
			this.minPower = minPower;
			this.name = name;
			this.mountType = mountType;
			this.propType = propType;
		}
	}

	public static class EngineProp {
		int minBoost;
		String letter;
		String propType;

		public EngineProp(int minBoost, String letter, String propType) {
			this.minBoost = minBoost;
			this.letter = letter;
			this.propType = propType;
		}
	}

	public static class WingCasing {
	}

	public static class WingTip {
	}

	public static class WingAileron {
	}

	public static class WingMount {
	}

}
