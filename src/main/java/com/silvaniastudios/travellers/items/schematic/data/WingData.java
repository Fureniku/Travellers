/**
 * 
 */
package com.silvaniastudios.travellers.items.schematic.data;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;

/**
 * @author jamesm2w
 *
 */
public class WingData implements IProceduralData {
	
	public SchematicCategories getCategories (ISchematicData schematic) {
		SchematicCategories categories = new SchematicCategories();
		categories.add("wing");
		categories.add("assembler");
		categories.add(schematic.getRarity().name);
		return categories;
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getStatNames()
	 */
	@Override
	public String[] getStatNames() {
		return new String[] { "travellers.stat.resilience.name",
				"travellers.stat.airbrake.name", "travellers.stat.wingpower.name" };
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getSlotNames()
	 */
	@Override
	public String[] getSlotNames() {
		return new String[] { "travellers.slot.casing.name",
				"travellers.slot.mech.name", "travellers.slot.aileron.name" };
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getNameComponents()
	 */
	@Override
	public String[] getNameComponents(ISchematicData schematic) {
		Casing wingCasing = getCasing(schematic);
		Tip wingTip = getTip(schematic, wingCasing);
		Aileron wingAileron = getAileron(schematic);
		Mount wingMount = getMount(schematic, wingCasing);
		String pivotLetter = getPivotLetter(schematic, wingAileron);
		
		return new String[] {
			wingCasing.name, wingTip.name, wingAileron.number, wingMount.code, pivotLetter
		};
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getNameFormat()
	 */
	@Override
	public String getNameFormat() {
		return "%s %s %s%s%s";
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getCrafting()
	 */
	@Override
	public SchematicCrafting getCrafting(ISchematicData schematic) {
		SchematicStats stats = schematic.getStats();
		SchematicStatisticSlot statResilience = stats.find("travellers.stat.resilience.name");
		SchematicStatisticSlot statAirbrake = stats.find("travellers.stat.airbrake.name");
		SchematicStatisticSlot statPower = stats.find("travellers.stat.wingpower.name");
		
		SchematicCraftingSlot slotCasing = new SchematicCraftingSlot("travellers.slot.casing.name");
		slotCasing.amount = (int) (2 * (statResilience.amount + statAirbrake.amount));
		slotCasing.type = getCasing(schematic).getMaterial();
		
		SchematicCraftingSlot slotMechInternals = new SchematicCraftingSlot("travellers.slot.mech.name");
		slotMechInternals.amount = (int)( 2 * (statAirbrake.amount));
		slotMechInternals.type = "travellers.material.metal"; // always metal
		
		SchematicCraftingSlot slotAileron = new SchematicCraftingSlot("travellers.slot.aileron.name");
		slotAileron.amount = (int)(2 * (statPower.amount));
		slotAileron.type = getAileron(schematic).getMaterial();
		
		SchematicCrafting crafting = new SchematicCrafting();
		crafting.add(slotCasing);
		crafting.add(slotMechInternals);
		crafting.add(slotAileron);
		
		return crafting;
	}
	
	public Casing getCasing(ISchematicData schematic) {
		Casing foundCasing = WingCasings[0];
		
		for (int i = 0; i < WingCasings.length; i++) {
			Casing currentCasing = WingCasings[i];
			if (currentCasing.tier <= schematic.getRarity().tier &&
					schematic.getStats().maxStat().name.contentEquals(currentCasing.primaryStat)) {
				
				if (schematic.getStats().secondaryStat().name.contentEquals(currentCasing.secondaryStat)) {
					foundCasing = currentCasing;
				} else if (currentCasing.secondaryStat.contentEquals("*")) {
					foundCasing = currentCasing;
				}
			}
		}
		
		return foundCasing;
	}
	
	public Tip getTip (ISchematicData schematic, Casing casing) {
		Tip foundTip = WingTips[0];
		SchematicStatisticSlot wingPower = schematic.getStats().find("travellers.stat.wingpower.name");
		
		for (int i = 0; i < WingTips.length; i++) {
			Tip currentTip = WingTips[i];
			
			if (wingPower.amount > currentTip.minPower && casing.mat == currentTip.mat) {
				foundTip = currentTip;
			}
		}
		
		return foundTip;
	}
	
	public Aileron getAileron (ISchematicData schematic) {
		Aileron foundAileron = WingAilerons[0];
		SchematicStatisticSlot wingPivot = schematic.getStats().find("travellers.stat.airbrake.name");
		
		for (int i = 0; i < WingAilerons.length; i++) {
			Aileron currentAileron = WingAilerons[i];
			
			if (wingPivot.amount > currentAileron.minPivot) {
				foundAileron = currentAileron;
			}
			
		}
		return foundAileron;
	}
	
	public Mount getMount (ISchematicData schematic, Casing casing) {
		Mount foundMount = WingMounts[0];
		SchematicStatisticSlot wingResil = schematic.getStats().find("travellers.stat.resilience.name");
		
		for (int i = 0; i < WingMounts.length; i++) {
			Mount currentMount = WingMounts[i];
			
			if (wingResil.amount > currentMount.minResil && casing.mat == currentMount.mat) {
				foundMount = currentMount;
			}
			
		}
		return foundMount;
	}
	
	public String getPivotLetter (ISchematicData schematic, Aileron aileron) {
		SchematicStatisticSlot pivot = schematic.getStats().find("travellers.stat.airbrake.name");
		return Character.toString((char) (64 + (int)Math.ceil(pivot.amount - aileron.minPivot)));
	}

	
	public static final Casing[] WingCasings = new Casing[] {
		new Casing("Barndoor", "travellers.stat.resilience.name", "travellers.stat.airbrake.name", 1, MaterialType.WOOD),
		new Casing("Driftwood", "travellers.stat.resilience.name", "travellers.stat.wingpower.name", 1, MaterialType.WOOD),
		new Casing("Hatchflap", "travellers.stat.airbrake.name", "*", 1, MaterialType.WOOD),
		new Casing("Strapped", "travellers.stat.wingpower.name", "*", 1, MaterialType.WOOD),
		new Casing("Consolidated", "travellers.stat.resilience.name", "travellers.stat.airbrake.name", 2, MaterialType.WOOD),
		new Casing("Feather", "travellers.stat.wingpower.name", "travellers.stat.airbrake.name", 2, MaterialType.WOOD),
		new Casing("Hinged", "travellers.stat.airbrake.name", "travellers.stat.resilience.name", 2, MaterialType.WOOD),
		new Casing("Reinforced", "travellers.stat.resilience.name", "travellers.stat.wingpower.name", 2, MaterialType.WOOD),
		new Casing("Rotator", "travellers.stat.airbrake.name", "travellers.stat.wingpower.name", 2, MaterialType.WOOD),
		new Casing("Streamlined", "travellers.stat.wingpower.name", "travellers.stat.resilience.name", 2, MaterialType.WOOD),
		new Casing("Ceres", "travellers.stat.wingpower.name", "travellers.stat.resilience.name", 3, MaterialType.METAL),
		new Casing("Comet", "travellers.stat.wingpower.name", "travellers.stat.airbrake.name", 3, MaterialType.METAL),
		new Casing("Condor", "travellers.stat.resilience.name", "*", 3, MaterialType.METAL),
		new Casing("Dynamo", "travellers.stat.airbrake.name", "travellers.stat.wingpower.name", 3, MaterialType.METAL),
		new Casing("Falcon", "travellers.stat.airbrake.name", "travellers.stat.resilience.name", 3, MaterialType.METAL),
		new Casing("Aether", "travellers.stat.wingpower.name", "travellers.stat.resilience.name", 4, MaterialType.METAL),
		new Casing("Celestius", "travellers.stat.wingpower.name", "travellers.stat.airbrake.name", 4, MaterialType.METAL),
		new Casing("Cloudline", "travellers.stat.airbrake.name", "travellers.stat.resilience.name", 4, MaterialType.METAL),
		new Casing("Empyr", "travellers.stat.airbrake.name", "travellers.stat.wingpower.name", 4, MaterialType.METAL),
		new Casing("Roc", "travellers.stat.resilience.name", "*", 4, MaterialType.METAL)
	};
	
	public static final Tip[] WingTips = new Tip[] {
		new Tip("Board", 7, MaterialType.WOOD),	
		new Tip("Fin", 15, MaterialType.WOOD),	
		new Tip("Blade", 20, MaterialType.WOOD),	
		new Tip("Drifter", 25, MaterialType.WOOD),	
		new Tip("Traveller", 10, MaterialType.METAL),	
		new Tip("Aviator", 27, MaterialType.METAL),	
		new Tip("Navigator", 40, MaterialType.METAL),	
		new Tip("Aeronaut", 55, MaterialType.METAL)
	};
	
	public static final Aileron[] WingAilerons = new Aileron[] {
		new Aileron("1", 0, MaterialType.WOOD),	
		new Aileron("2", 11, MaterialType.WOOD),
		new Aileron("3", 17, MaterialType.METAL),
		new Aileron("4", 23, MaterialType.METAL),
		new Aileron("5", 29, MaterialType.METAL),
		new Aileron("6", 39, MaterialType.METAL),
		new Aileron("7", 49, MaterialType.METAL),
		new Aileron("8", 59, MaterialType.METAL)
	};
	
	public static final Mount[] WingMounts = new Mount[] {
		new Mount("0", 7, MaterialType.WOOD),
		new Mount("5", 20, MaterialType.WOOD),
		new Mount("00", 9, MaterialType.METAL),
		new Mount("25", 25, MaterialType.METAL),
		new Mount("50", 40, MaterialType.METAL),
		new Mount("99", 56, MaterialType.METAL)
	};
	
	private static class Casing {
		public String name;
		public String primaryStat;
		public String secondaryStat;
		public int tier;
		public MaterialType mat;

		public Casing(String name, String primaryStat, String secondaryStat, int tier, MaterialType mat) {
			this.name = name;
			this.primaryStat = primaryStat;
			this.secondaryStat = secondaryStat;
			this.tier = tier;
			this.mat = mat;
		}
		
		public String getMaterial () {
			switch (mat) {
			case METAL:
				return "travellers.material.metal";
			default:
				return "travellers.material.wood";
			}
		}
	}
	
	private static class Tip {
		public String name;
		public int minPower;
		public MaterialType mat;
	
		public Tip (String name, int minPower, MaterialType mat) {
			this.name = name;
			this.minPower = minPower;
			this.mat = mat;
		}
	}
	
	private static class Aileron {
		public String number;
		public int minPivot;
		public MaterialType mat;
		
		public Aileron (String number, int minPivot, MaterialType mat) {
			this.number = number;
			this.minPivot = minPivot;
			this.mat = mat;
		}
		
		public String getMaterial () {
			switch (mat) {
			case METAL:
				return "travellers.material.metal";
			default:
				return "travellers.material.wood";
			}
		}
	}
	
	private static class Mount {
		public String code;
		public int minResil;
		public MaterialType mat;
		
		public Mount (String code, int minResil, MaterialType mat) {
			this.code = code;
			this.minResil = minResil;
			this.mat = mat;
		}
	}
}
