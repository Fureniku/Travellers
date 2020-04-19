package com.silvaniastudios.travellers.items.schematic;

import java.util.Arrays;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;

/**
 * 
 * @author jamesm2w
 */
public class EngineProceduralData {

	/**
	 * Credit to Node, Ziwix, Machine Maker, Kruft, Docfreeman, Vloshko,
	 * jamesm2w all from the community and Matt Foster from Bossa Studios (Bossa
	 * Moster) For compiling the many engine schematics to make sure all
	 * possible part names were covered, and thanks to Moster for confirming
	 * which were actually bugs :)
	 * 
	 * @param schematic The Schematic Data object
	 * @return String representing the name of the engine
	 */
	public static String getEngineName(ISchematicData schematic) {
		EngineCasing casing = getCasing(schematic);
		EnginePropMount propMount = getPropMount(schematic, casing);
		EngineProp prop = getProp(schematic, propMount);
		
		int powerNumber = getPowerOffset(schematic, propMount);
		
		return String.format("%s %s %s%s", casing.name, propMount.name, prop.letter, String.valueOf(powerNumber));
	}
	
	public static EngineCasing getCasing (ISchematicData schematic) {
		EngineCasing foundCasing = EngineCasingList[0];
		
		for (int i = 0; i < EngineCasingList.length; i++) {
			EngineCasing currentCasing = EngineCasingList[i];
			if (currentCasing.tier <= schematic.getRarity().tier
					&& schematic.getStats().maxStat().name.contentEquals(currentCasing.stat)) {

				foundCasing = currentCasing;
			}
		}
		
		return foundCasing;
	}
	
	public static EnginePropMount getPropMount (ISchematicData schematic, EngineCasing casing) {
		
		float power = schematic.getStats().find("travellers.stat.power.name").amount;
		EnginePropMount propMount = EnginePropMountList[0];
		
		for (int i = 0; i < EnginePropMountList.length; i++) {
			if (power > EnginePropMountList[i].minPower && EnginePropMountList[i].mountType.equals(casing.mountType)) {

				propMount = EnginePropMountList[i];

				break;
			}
		}
		
		return propMount;
	}
	
	public static EngineProp getProp (ISchematicData schematic, EnginePropMount propMount) {
		float boost = schematic.getStats().find("travellers.stat.boost.name").amount;
		EngineProp prop = EnginePropList[0];
		
		for (int i = 0; i < EnginePropList.length; i++) {
			if (boost >= EnginePropList[i].minBoost && EnginePropList[i].propType.equals(propMount.propType)) {
				prop = EnginePropList[i];
				break;
			}
		}
		
		return prop;
	}
	
	public static int getPowerOffset (ISchematicData schematic, EnginePropMount propMount) {
		float power = schematic.getStats().find("travellers.stat.power.name").amount;
		return Math.round(power - propMount.minPower);
	}
	
	public static SchematicCrafting generateEngineCosts (ISchematicData schematic) {
		
		SchematicStats stats = schematic.getStats();
		
		SchematicStatisticSlot statResilience = stats.find("travellers.stat.resilience.name");
		SchematicStatisticSlot statPower = stats.find("travellers.stat.power.name");
		SchematicStatisticSlot statBoost = stats.find("travellers.stat.boost.name");
		SchematicStatisticSlot statFuelEff = stats.find("travellers.stat.fueleff.name");
		SchematicStatisticSlot statOhl = stats.find("travellers.stat.ohl.name");
		
		EngineCasing casing = getCasing(schematic);
		EnginePropMount propMount = getPropMount(schematic, casing);
		EngineProp prop = getProp(schematic, propMount);
		
		SchematicCraftingSlot slotCasing = new SchematicCraftingSlot("travellers.slot.casing.name");
		slotCasing.amount = 2 * (int)(statResilience.amount + statPower.amount + statBoost.amount); //2 x (Resilience + Power + Spinup)
		slotCasing.type = casing.materialType(); // Dependent on tier / resilience value
		
		SchematicCraftingSlot slotCombus = new SchematicCraftingSlot("travellers.slot.combus.name"); 
		slotCombus.amount = 2 * (int)(statPower.amount + statFuelEff.amount + statOhl.amount); //2 x (Power + Fuel efficiency + Overheat)
		slotCombus.type = "travellers.material.metal"; // Always metal
		
		SchematicCraftingSlot slotMech = new SchematicCraftingSlot("travellers.slot.mech.name");
		slotMech.amount = 2 * (int)(statPower.amount + statFuelEff.amount); //2 x (Power + Fuel efficiency)
		slotMech.type = "travellers.material.metal"; // Always metal
		
		SchematicCraftingSlot slotProp = new SchematicCraftingSlot("travellers.slot.prop.name");
		slotProp.amount = 2 * (int)(statBoost.amount + statOhl.amount); //2 x (Spinup + Overheat)
		slotProp.type = prop.materialType(); // Is Prop Vowel?
		
		SchematicCrafting slots = new SchematicCrafting();
		slots.add(slotCasing);
		slots.add(slotCombus);
		slots.add(slotMech);
		slots.add(slotProp);
		return slots;

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
		
		public String materialType () {
			System.out.println(this.mountType);
			if (mountType.contentEquals("m")){
				return "travellers.material.metal";
			} else {
				return "travellers.material.wood";
			}
		}
		
		@Override
		public String toString() {
			return String.format("[name=%s, stat=%s, mount=%s]", name, stat, mountType);
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
		
		@Override
		public String toString() {
			return String.format("[power=%d, name=%s, mount=%s, jet=%s]", minPower, name, mountType, propType);
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
		
		public String materialType () {
			String[] vowels = new String[]{"A", "E", "I", "O", "U"};

			if (Arrays.asList(vowels).contains(letter)) {
				return "travellers.material.wood";
			} else {
				return "travellers.material.metal";
			}
		}
		
		@Override
		public String toString() {
			return String.format("[boost=%d, letter=%s, jet=%s]", minBoost, letter, propType);
		}
	}


}
