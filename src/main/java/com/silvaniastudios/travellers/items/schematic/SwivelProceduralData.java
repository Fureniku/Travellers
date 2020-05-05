/**
 * 
 */
package com.silvaniastudios.travellers.items.schematic;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;

/**
 * @author jamesm2w
 *
 */
public class SwivelProceduralData {
	
	public static String getCannonName(ISchematicData schematic) {
		return "Procedural Swivel (Tier" + String.valueOf(schematic.getRarity().tier) + ")";
	}
	
	public static SchematicCrafting getCrafting (ISchematicData schematic) {
		return new SchematicCrafting();
	}
	
	public static SchematicCategories getCategories (ISchematicData schematic) {
		SchematicCategories categories = new SchematicCategories();
		categories.add("swivel");
		categories.add("assembler");
		categories.add(schematic.getRarity().name);
		return categories;
	}

}
