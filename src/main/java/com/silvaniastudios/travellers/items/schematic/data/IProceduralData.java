/**
 * 
 */
package com.silvaniastudios.travellers.items.schematic.data;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;

/**
 * Credit to Node, Ziwix, Machine Maker, Kruft, Docfreeman, Vloshko,
 * jamesm2w all from the community and Matt Foster from Bossa Studios (Bossa
 * Moster) For compiling the many engine schematics to make sure all
 * possible part names were covered, and thanks to Moster for confirming
 * which were actually bugs :)
 * @author jamesm2w
 */
public interface IProceduralData {
	
	public SchematicCategories getCategories(ISchematicData schematic);
	
	public String[] getStatNames();
	
	public String[] getSlotNames();
	
	public String getNameFormat();
	
	public String[] getNameComponents(ISchematicData schematic);

	public SchematicCrafting getCrafting(ISchematicData schematic);
}
