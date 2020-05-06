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
public class SwivelData implements IProceduralData {

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
		Casing swivelCasing = getCasing(schematic);
		Barrel swivelBarrel = getBarrel(schematic);
		BaseArm swivelBaseArm = getBaseArm(schematic, swivelCasing);
		AmmoBox swivelAmmoBox = getAmmoBox(schematic, swivelCasing);
		
		String offset = getRangeOffset(schematic, swivelBarrel);
		
		return new String[] {
				swivelCasing.name, swivelBarrel.name, swivelBaseArm.letter, swivelAmmoBox.code, offset
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
		
		Casing swivelCasing = getCasing(schematic);
		AmmoBox swivelAmmoBox = getAmmoBox(schematic, swivelCasing);

		SchematicCraftingSlot slotCasing = new SchematicCraftingSlot("travellers.slot.casing.name");
		//Casing = 2 x (Resilience + Power)
		slotCasing.amount = 2 * (int)(statResilience.amount + statRange.amount);
		slotCasing.type = swivelCasing.getMaterial();
		
		SchematicCraftingSlot slotBarrel = new SchematicCraftingSlot("travellers.slot.barrel.name");
		//Barrel = 2 x (Power)
		slotBarrel.amount = 2 * (int)(statRange.amount); 
		slotBarrel.type = "travellers.material.metal";
		
		SchematicCraftingSlot slotAmmoLoader = new SchematicCraftingSlot("travellers.slot.ammoloader.name");
		//Ammo Loader = 2x (Capacity + Overheat)
		slotAmmoLoader.amount = 2 * (int)(statFrag.amount + statOhl.amount);
		slotAmmoLoader.type = swivelAmmoBox.getMaterial();
		
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
		categories.add("swivel");
		categories.add("assembler");
		categories.add(schematic.getRarity().name);
		return categories;
	}
	
	public Casing getCasing (ISchematicData schematic) {
		Casing foundCasing = SwivelCasings[0];
		for (int i = 0; i < SwivelCasings.length; i++) {
			Casing selectedCasing = SwivelCasings[i];
			
			if (selectedCasing.tier <= schematic.getRarity().tier) {
				
				if (selectedCasing.primaryStat.equals(schematic.getStats().maxStat().name)) {
					
					foundCasing = selectedCasing;
					break;
				} else if (selectedCasing.primaryStat.equals("*")) {
					
					foundCasing = selectedCasing;
				}

			}
			
		}
		
		return foundCasing;
	}
	
	public Barrel getBarrel(ISchematicData schematic) {
		Barrel foundBarrel = SwivelBarrels[0];
		
		SchematicStatisticSlot swivelRange = schematic.getStats().find("travellers.stat.range.name");
		
		for (int i = 0; i < SwivelBarrels.length; i++) {
			Barrel selectedBarrel = SwivelBarrels[i];
			if (selectedBarrel.minPower <= swivelRange.amount) {
				foundBarrel = selectedBarrel;
			}
		}
		
		return foundBarrel;
	}
	
	public BaseArm getBaseArm(ISchematicData schematic, Casing swivelCasing) {
		BaseArm foundBaseArm = SwivelBaseArms[0];
		SchematicStatisticSlot swivelResil = schematic.getStats().find("travellers.stat.resilience.name");
		for (int i = 0; i < SwivelBaseArms.length; i++) {
			BaseArm selectedBaseArm = SwivelBaseArms[i];
			
			if (selectedBaseArm.minResilience <= swivelResil.amount 
					&& selectedBaseArm.mat == swivelCasing.mat) {
				foundBaseArm = selectedBaseArm;
			}
		}
		
		return foundBaseArm;
	}
	
	public AmmoBox getAmmoBox(ISchematicData schematic, Casing swivelCasing) {
		AmmoBox foundAmmoBox = SwivelAmmoBoxes[0];
		SchematicStatisticSlot swivelRof = schematic.getStats().find("travellers.stat.rof.name");
		for (int i = 0; i < SwivelAmmoBoxes.length; i++) {
			AmmoBox selectedAmmoBox = SwivelAmmoBoxes[i];
			
			if (selectedAmmoBox.minRof <= swivelRof.amount && selectedAmmoBox.mat == swivelCasing.mat) {
				foundAmmoBox = selectedAmmoBox;
			}
		}
		
		return foundAmmoBox;
	}
	
	public String getRangeOffset(ISchematicData schematic, Barrel swivelBarrel) {
		
		SchematicStatisticSlot swivelRange = schematic.getStats().find("travellers.stat.range.name");
		
		return String.valueOf((int)(swivelRange.amount - swivelBarrel.minPower));
	}
	
	
	protected static final Casing[] SwivelCasings = new Casing[]{
		new Casing(1, "Funnel", "*", MaterialType.WOOD),

		new Casing(2, "Rustic", "*", MaterialType.WOOD),

		new Casing(3, "Forged", "*", MaterialType.METAL),
		new Casing(3, "Tempered", "travellers.stat.resilience.name", MaterialType.METAL),
		
		new Casing(4, "Avalanche", "*", MaterialType.METAL),
		new Casing(4, "Typhoon", "travellers.stat.frag.name", MaterialType.METAL)
	};
	
	protected static final Barrel[] SwivelBarrels = new Barrel[] {
		new Barrel("Scrapper", 7),
		new Barrel("Boomer", 15),
		new Barrel("Caster", 23),
		new Barrel("Nailer", 30),
		new Barrel("Shredder", 38),
		new Barrel("Ripper", 47),
		new Barrel("Screamer", 55),
		new Barrel("Atomizer", 64)
	};
	
	protected static final BaseArm[] SwivelBaseArms = new BaseArm[] {
		new BaseArm("N",  7, MaterialType.WOOD),
		new BaseArm("E", 20, MaterialType.WOOD),
		new BaseArm("O",  9, MaterialType.METAL),
		new BaseArm("C", 25, MaterialType.METAL),
		new BaseArm("X", 50, MaterialType.METAL)
	};
	
	protected static final AmmoBox[] SwivelAmmoBoxes = new AmmoBox[] {
		new AmmoBox("SR", 8, MaterialType.WOOD),
		new AmmoBox("O", 30, MaterialType.WOOD),
		new AmmoBox("L", 9, MaterialType.METAL),
		new AmmoBox("R", 25, MaterialType.METAL),
		new AmmoBox("J", 40, MaterialType.METAL),
		new AmmoBox("B", 56, MaterialType.METAL)
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
