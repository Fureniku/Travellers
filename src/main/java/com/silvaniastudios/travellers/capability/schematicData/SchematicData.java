package com.silvaniastudios.travellers.capability.schematicData;

import java.util.UUID;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;
import com.silvaniastudios.travellers.items.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;

import net.minecraft.nbt.NBTTagCompound;

public class SchematicData implements ISchematicData {
	
	private UUID uuid = UUID.randomUUID();
	private String name = "schematic";
	private SchematicRarityEnum rarity = SchematicRarityEnum.COMMON;
	private SchematicTypeEnum type = SchematicTypeEnum.FIXED;
	private SchematicStats stats = new SchematicStats();
	private SchematicCrafting crafting = new SchematicCrafting();
	
	public SchematicData () {
		uuid = UUID.randomUUID();
		name = "schematic";
		rarity = SchematicRarityEnum.COMMON;
		type = SchematicTypeEnum.FIXED;
		stats = new SchematicStats();
		crafting = new SchematicCrafting();
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setUniqueId("uuid", getUUID());
		nbt.setString("name", getName());
		nbt.setString("rarity", getRarity().name);
		nbt.setString("type", getType().name);
		
		NBTTagCompound stats = new NBTTagCompound();
		for (SchematicStatisticSlot stat: getStats()) {
			stats.setFloat(stat.name, stat.amount);
		}
		nbt.setTag("stats", stats);
		
		NBTTagCompound crafting = new NBTTagCompound();
		for (SchematicCraftingSlot craft : getCrafting()) {
			NBTTagCompound slotDetail = new NBTTagCompound();
			slotDetail.setString("name", craft.name);
			slotDetail.setString("type", craft.type);
			slotDetail.setInteger("amount", craft.amount);
			crafting.setTag(craft.name, slotDetail);
		}
		nbt.setTag("crafting", crafting);
		
		return nbt;
	}

	@Override
	public void fromNBT(NBTTagCompound compound) {
		
		if (compound == null) {
			return;
		}
		
		if (compound.hasUniqueId("uuid")) {
			setUUID(compound.getUniqueId("uuid"));
		}
		if (compound.hasKey("name")) {
			setName(compound.getString("name"));
		}
		if (compound.hasKey("rarity")) {
			setRarity(SchematicRarityEnum.fromString(compound.getString("rarity")));
		}
		
		if (compound.hasKey("type")) {
			setType(SchematicTypeEnum.fromString(compound.getString("type")));
		}
		if (compound.hasKey("stats")) {
			SchematicStats stats = new SchematicStats();
			for (String key : compound.getCompoundTag("stats").getKeySet()) {
				stats.add(new SchematicStatisticSlot(key, compound.getCompoundTag("stats").getFloat(key)));
			}
			setStats(stats);
		}
		if (compound.hasKey("crafting")) {
			SchematicCrafting crafting = new SchematicCrafting();
			for (String key : compound.getCompoundTag("crafting").getKeySet()) {
				NBTTagCompound craft = compound.getCompoundTag("crafting").getCompoundTag(key);
				crafting.add(new SchematicCraftingSlot(key, craft.getString("type"), craft.getInteger("amount")));
			}
			setCrafting(crafting);
		}
	}
	
	@Override
	public void copyNBT(ISchematicData old) {
		setUUID(old.getUUID());
		setName(old.getName());
		setRarity(old.getRarity());
		setType(old.getType());
		setStats(old.getStats());
		setCrafting(old.getCrafting());
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void generateUUID() {
		this.uuid = UUID.randomUUID();
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public SchematicRarityEnum getRarity() {
		return this.rarity;
	}

	@Override
	public void setRarity(SchematicRarityEnum rarity) {
		this.rarity = rarity;
	}

	@Override
	public SchematicTypeEnum getType() {
		return this.type;
	}

	@Override
	public void setType(SchematicTypeEnum type) {
		this.type = type;
	}

	@Override
	public SchematicStats getStats() {
		return this.stats;
	}

	@Override
	public void setStats(SchematicStats stats) {
		this.stats = stats;
	}

	@Override
	public SchematicCrafting getCrafting() {
		return this.crafting;
	}

	@Override
	public void setCrafting(SchematicCrafting crafting) {
		this.crafting = crafting;
	}
	
	@Override
	public String[] getSlotNames () {
		if (getType() == SchematicTypeEnum.FIXED) {
			String[] slotNames = new String[getCrafting().size()];
			for (int i = 0; i < getCrafting().size(); i++) {
				slotNames[i] = getCrafting().get(i).name;
			}
			return slotNames;
		} else {
			String[] slotNames = getType().getSlotNames();
			return slotNames;
		}
	}
	
}