package com.silvaniastudios.travellers.capability.schematicData;

import java.util.ArrayList;
import java.util.UUID;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;
import com.silvaniastudios.travellers.items.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author jamesm2w
 */
public interface ISchematicData {
	
	/**
	 * Data management methods, to read, write and copy from NBT storage
	 * @return
	 */
	public NBTTagCompound toNBT ();
	public void fromNBT(NBTTagCompound compound);
	public void copyNBT(ISchematicData old);
	
	/**
	 * UUID keeps track of schematic
	 */
	public UUID getUUID();
	public void setUUID(UUID uuid);
	public void generateUUID();
	
	/**
	 * Schematic Name
	 * @return
	 */
	public String getName();
	public void setName(String name);
	
	/**
	 * Rarity (Common -> Legendary)
	 * @return
	 */
	public SchematicRarityEnum getRarity();
	public void setRarity(SchematicRarityEnum rarity);
	
	/**
	 * Type (Fixed, Engine, Wing, etc.)
	 * @return
	 */
	public SchematicTypeEnum getType();
	public void setType(SchematicTypeEnum type);
	
	/**
	 * Stats (resil:20, etc)
	 * @return
	 */
	public SchematicStats getStats();
	public void setStats(SchematicStats stats);
	
	/**
	 * Crafting (casing:20wood, etc.)
	 * @return
	 */
	public ArrayList<SchematicCraftingSlot> getCrafting();
	public void setCrafting(ArrayList<SchematicCraftingSlot> crafting);
}
