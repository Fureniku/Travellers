/**
 * 
 */
package com.silvaniastudios.travellers.items.schematic.data;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;

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
		return new String[] {
				"Procedural", "Cannon", "T", "14", "S" 
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
		// TODO Auto-generated method stub
		return null;
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

}
