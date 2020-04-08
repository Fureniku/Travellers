package com.silvaniastudios.travellers.capability.schematicData;

import java.util.UUID;

import com.silvaniastudios.travellers.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.schematic.SchematicTypeEnum;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author jamesm2w
 */
public interface ISchematicData {
	
	/*
	 * uuid - UUID : uuid of the schematic -- randomised allways
	 * name - String : name for the item (unlocalized name) -- given by proc and fixed
	 * tooltip - String : string to reference the tooltip -- given by proc and fixed
	 * rarity - SchematicRarityEnum : COMMON, UNCOMMON, RARE, EXOTIC, [PRISTINE, LEGENDARY] -- given by both
	 * type - SchematicTypeEnum : FIXED, ENGINE, WING, CANNON, SWIVELCANNON -- FIXED for fixed, defined otherwise
	 * tags - String[] union of rarity, name, type, etc. -- Generated
	 * unlearnable - boolean : can the schematic be unlearned -- given if false
	 * iconRef - String : texture name of the icon to be used -- given
	 * 
	 * baseStats - float[] : {Resilience, 1, 2, 3, 4} -- generated by proc, given by fixed
	 * statAmount - int : pointer to how many stats we have (0 -> 4) -- fixed == 0, proc up to 4
	 */
	
	public NBTTagCompound toNBT();
	public ISchematicData fromNBT(NBTTagCompound nbtTag);
	
	public UUID getUUID();
	public void setUUID(UUID uuid);
	
	public String getName();
	public void setName(String name);
	
	public String getTooltip();
	public void setTooltip(String tooltip);
	
	public SchematicRarityEnum getRarity();
	public void setRarity(SchematicRarityEnum rarity);
	
	public SchematicTypeEnum getType();
	public void setType(SchematicTypeEnum type);
	
	public String[] getTags();
	public void setTags(String[] tags);
	public String[] addTag(String tag);
	public boolean hasTag(String tag);
	public String[] removeTag(String tag);
	
	public boolean isUnlearnable();
	public void setUnlearnable(boolean bool);
	
	public String getIconRef();
	public void setIconRef(String iconRef);
	
	public float[] getBaseStats();
	public void setBaseStats(float[] baseStats);
	public int[] getRoundedStats();
	
	public float[] generateRandomBaseStats();
	
	public int getStatAmount();
	public void setStatAmount(int stat);
	
	public boolean isDefault();
	public void setDefault(boolean bool);
}
