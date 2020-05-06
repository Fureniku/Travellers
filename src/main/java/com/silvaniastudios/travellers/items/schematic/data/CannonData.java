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
public class CannonData implements IProceduralData {

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getStatNames()
	 */
	@Override
	public String[] getStatNames() {
		return new String[] { "travellers.stat.resilience.name",
				"travellers.stat.range.name", "travellers.stat.frag.name", "travellers.stat.cannonohl.name",
				"travellers.stat.rof.name" };
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getSlotNames()
	 */
	@Override
	public String[] getSlotNames() {
		return new String [] {
				"travellers.slot.casing.name", "travellers.slot.barrel.name",
				"travellers.slot.ammoloader.name", "travellers.slot.firingmech.name"
		};
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getNameComponents()
	 */
	@Override
	public String[] getNameComponents(ISchematicData schematic) {
		
		Casing cannonCasing = getCasing(schematic);
		Barrel cannonBarrel = getBarrel(schematic);
		BaseArm cannonBaseArm = getBaseArm(schematic, cannonCasing);
		AmmoBox cannonAmmoBox = getAmmoBox(schematic, cannonCasing);
		
		String numberOffset = getRangeOffset(schematic, cannonBarrel);
	
		return new String[] {
			cannonCasing.name, cannonBarrel.name, cannonBaseArm.letter, cannonAmmoBox.code, numberOffset
		};
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getNameFormat()
	 */
	@Override
	public String getNameFormat() {
		return "%s \"%s\" %s%s-%s";
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getCrafting()
	 */
	@Override
	public SchematicCrafting getCrafting(ISchematicData schematic) {
		SchematicStats stats = schematic.getStats();
		
		SchematicStatisticSlot statResilience = stats.find("travellers.stat.resilience.name");
		SchematicStatisticSlot statRange = stats.find("travellers.stat.range.name");
		SchematicStatisticSlot statFrag = stats.find("travellers.stat.frag.name");
		SchematicStatisticSlot statOhl = stats.find("travellers.stat.cannonohl.name");
		SchematicStatisticSlot statRof = stats.find("travellers.stat.rof.name");

		Casing cannonCasing = getCasing(schematic);
		AmmoBox cannonAmmoBox = getAmmoBox(schematic, cannonCasing);
		
		SchematicCraftingSlot slotCasing = new SchematicCraftingSlot("travellers.slot.casing.name");
		//Casing = 2 x (Resilience + Power)
		slotCasing.amount = 2 * (int)(statResilience.amount + statRange.amount);
		slotCasing.type = cannonCasing.getMaterial();
		
		SchematicCraftingSlot slotBarrel = new SchematicCraftingSlot("travellers.slot.barrel.name");
		//Barrel = 2 x (Power)
		slotBarrel.amount = 2 * (int)(statRange.amount); 
		slotBarrel.type = "travellers.material.metal";
		
		SchematicCraftingSlot slotAmmoLoader = new SchematicCraftingSlot("travellers.slot.ammoloader.name");
		//Ammo Loader = 2x (Capacity + Overheat)
		slotAmmoLoader.amount = 2 * (int)(statFrag.amount + statOhl.amount);
		slotAmmoLoader.type = cannonAmmoBox.getMaterial();
		
		SchematicCraftingSlot slotFiringMech = new SchematicCraftingSlot("travellers.slot.firingmech.name");
		//Firing Mechanism = 2 x (Rate of fire)
		slotFiringMech.amount = 2 * (int)(statRof.amount); 
		slotFiringMech.type = "travellers.material.metal";
		
		SchematicCrafting slots = new SchematicCrafting();
		slots.add(slotCasing);
		slots.add(slotBarrel);
		slots.add(slotAmmoLoader);
		slots.add(slotFiringMech);
		return slots;
	}

	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getCategories(com.silvaniastudios.travellers.capability.schematicData.ISchematicData)
	 */
	@Override
	public SchematicCategories getCategories(ISchematicData schematic) {
		SchematicCategories categories = new SchematicCategories();
		categories.add("cannon");
		categories.add("assembler");
		categories.add(schematic.getRarity().name);
		return categories;
	}
	
	public Casing getCasing (ISchematicData schematic) {
		Casing foundCasing = CannonCasings[0];
		for (int i = 0; i < CannonCasings.length; i++) {
			Casing selectedCasing = CannonCasings[i];
			
			if (selectedCasing.tier <= schematic.getRarity().tier &&
					selectedCasing.primaryStat.equals(schematic.getStats().maxStat().name)) {
				foundCasing = selectedCasing;
			}
			
		}
		
		return foundCasing;
	}
	
	public Barrel getBarrel(ISchematicData schematic) {
		Barrel foundBarrel = CannonBarrels[0];
		
		SchematicStatisticSlot cannonRange = schematic.getStats().find("travellers.stat.range.name");
		
		for (int i = 0; i < CannonBarrels.length; i++) {
			Barrel selectedBarrel = CannonBarrels[i];
			if (selectedBarrel.minPower <= cannonRange.amount) {
				foundBarrel = selectedBarrel;
			}
		}
		
		return foundBarrel;
	}
	
	public BaseArm getBaseArm(ISchematicData schematic, Casing cannonCasing) {
		BaseArm foundBaseArm = CannonBaseArms[0];
		SchematicStatisticSlot cannonResil = schematic.getStats().find("travellers.stat.resilience.name");
		for (int i = 0; i < CannonBaseArms.length; i++) {
			BaseArm selectedBaseArm = CannonBaseArms[i];
			
			if (selectedBaseArm.minResilience <= cannonResil.amount 
					&& selectedBaseArm.mat == cannonCasing.mat) {
				foundBaseArm = selectedBaseArm;
			}
		}
		
		return foundBaseArm;
	}
	
	public AmmoBox getAmmoBox(ISchematicData schematic, Casing cannonCasing) {
		AmmoBox foundAmmoBox = CannonAmmoBoxes[0];
		SchematicStatisticSlot cannonRof = schematic.getStats().find("travellers.stat.rof.name");
		for (int i = 0; i < CannonAmmoBoxes.length; i++) {
			AmmoBox selectedAmmoBox = CannonAmmoBoxes[i];
			
			if (selectedAmmoBox.minRof <= cannonRof.amount && selectedAmmoBox.mat == cannonCasing.mat) {
				foundAmmoBox = selectedAmmoBox;
			}
		}
		
		return foundAmmoBox;
	}
	
	public String getRangeOffset(ISchematicData schematic, Barrel cannonBarrel) {
		
		SchematicStatisticSlot cannonRange = schematic.getStats().find("travellers.stat.range.name");
		
		return String.valueOf((int)(cannonRange.amount - cannonBarrel.minPower));
	}
	
	
	protected static final Casing[] CannonCasings = new Casing[]{
		new Casing(1, "Barrel", "travellers.stat.resilience.name", MaterialType.WOOD),
		new Casing(1, "Bonebare", "travellers.stat.rof.name", MaterialType.METAL),
		new Casing(1, "Bucket", "travellers.stat.range.name", MaterialType.WOOD),
		new Casing(1, "Tube", "travellers.stat.frag.name", MaterialType.WOOD),
		new Casing(2, "Bracebox", "travellers.stat.resilience.name", MaterialType.WOOD),
		new Casing(2, "Machine", "travellers.stat.range.name", MaterialType.WOOD),
		new Casing(2, "Needleframe", "travellers.stat.cannonohl.name", MaterialType.WOOD),
		new Casing(2, "Trunkmaw", "travellers.stat.frag.name", MaterialType.WOOD),
		new Casing(3, "Banshee", "travellers.stat.rof.name", MaterialType.METAL),
		new Casing(3, "Comet", "travellers.stat.range.name", MaterialType.METAL),
		new Casing(3, "Narwhal", "travellers.stat.cannonohl.name", MaterialType.METAL),
		new Casing(3, "Stonehide", "travellers.stat.resilience.name", MaterialType.METAL),
		new Casing(4, "Aurora", "travellers.stat.rof.name", MaterialType.METAL),
		new Casing(4, "Leviathan", "travellers.stat.resilience.name", MaterialType.METAL),
		new Casing(4, "Nemesis", "travellers.stat.range.name", MaterialType.METAL),
		new Casing(4, "Supernova", "travellers.stat.cannonohl.name", MaterialType.METAL),
		new Casing(4, "Titan", "travellers.stat.frag.name", MaterialType.METAL)
	};
	
	protected static final Barrel[] CannonBarrels = new Barrel[] {
		new Barrel("Shooter", -1),
		new Barrel("Stinger", 13),
		new Barrel("Hunter", 21),
		new Barrel("Scorpion", 29),
		new Barrel("Leveller", 37),
		new Barrel("Desolator", 45),
		new Barrel("Peacemaker", 59)	
	};
	
	protected static final BaseArm[] CannonBaseArms = new BaseArm[] {
		new BaseArm("A",  7, MaterialType.WOOD),
		new BaseArm("B", 15, MaterialType.WOOD),
		new BaseArm("F", 25, MaterialType.WOOD),
		new BaseArm("H",  7, MaterialType.METAL),
		new BaseArm("M", 25, MaterialType.METAL),
		new BaseArm("N", 34, MaterialType.METAL),
		new BaseArm("X", 44, MaterialType.METAL),
		new BaseArm("Z", 62, MaterialType.METAL)
	};
	
	protected static final AmmoBox[] CannonAmmoBoxes = new AmmoBox[] {
		new AmmoBox("C", 9, MaterialType.WOOD),
		new AmmoBox("D", 11, MaterialType.WOOD),
		new AmmoBox("G", 17, MaterialType.WOOD),
		new AmmoBox("K", 24, MaterialType.WOOD),
		new AmmoBox("Q", 30, MaterialType.WOOD),
		new AmmoBox("AB", 7, MaterialType.METAL),
		new AmmoBox("CD", 15, MaterialType.METAL),
		new AmmoBox("LS", 25, MaterialType.METAL),
		new AmmoBox("PR", 35, MaterialType.METAL),
		new AmmoBox("RC", 45, MaterialType.METAL),
		new AmmoBox("ST", 61, MaterialType.METAL)
	};
	
	private static class Casing {
		public int tier;
		public String name;
		public String primaryStat;
		public MaterialType mat;
		
		public Casing (int tier, String name, String primaryStat, MaterialType mat) {
			this.tier = tier;
			this.name = name;
			this.primaryStat = primaryStat;
			this.mat = mat;
		}
		
		public String getMaterial () {
			switch (mat) {
			case WOOD:
				return "travellers.material.wood";
			default:
				return "travellers.material.metal";
			}
		}
	}
	
	private static class Barrel {
		public String name;
		public int minPower;
		public Barrel (String name, int minPower) {
			this.name = name;
			this.minPower = minPower;
		}
	}
	
	private static class BaseArm {
		public String letter;
		public int minResilience;
		public MaterialType mat;
		public BaseArm(String letter, int minResilience, MaterialType mat) {
			this.letter = letter;
			this.minResilience = minResilience;
			this.mat = mat;
		}
	}
	
	private static class AmmoBox {
		public String code;
		public int minRof;
		public MaterialType mat;
		public AmmoBox (String code, int minRof, MaterialType mat) {
			this.code = code;
			this.minRof = minRof;
			this.mat = mat;
		}
		public String getMaterial () {
			switch (mat) {
			case WOOD:
				return "travellers.material.wood";
			default:
				return "travellers.material.metal";
			}
		}
	}

}
