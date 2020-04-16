package com.silvaniastudios.travellers.capability.schematicData;

import java.util.ArrayList;
import java.util.UUID;

import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.items.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;

import net.minecraft.nbt.NBTTagCompound;

public class SchematicData implements ISchematicData {
	
	private UUID uuid = UUID.randomUUID();
	private String name = "schematic";
	private SchematicRarityEnum rarity = SchematicRarityEnum.COMMON;
	private SchematicTypeEnum type = SchematicTypeEnum.FIXED;
	private ArrayList<SchematicStatisticSlot> stats = new ArrayList<SchematicStatisticSlot>();
	private ArrayList<SchematicCraftingSlot> crafting = new ArrayList<SchematicCraftingSlot>();
	
	public SchematicData () {
		uuid = UUID.randomUUID();
		name = "schematic";
		rarity = SchematicRarityEnum.COMMON;
		type = SchematicTypeEnum.FIXED;
		stats = new ArrayList<SchematicStatisticSlot>();
		crafting = new ArrayList<SchematicCraftingSlot>();
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
			slotDetail.setString("type", craft.type);
			slotDetail.setInteger("amount", craft.amount);
			crafting.setTag(craft.name, slotDetail);
		}
		nbt.setTag("crafting", crafting);
		
		return nbt;
	}

	@Override
	public void fromNBT(NBTTagCompound compound) {
		setUUID(compound.getUniqueId("uuid"));
		setName(compound.getString("name"));
		
		setRarity(SchematicRarityEnum.fromString(compound.getString("rarity")));
		setType(SchematicTypeEnum.fromString(compound.getString("type")));
		
		ArrayList<SchematicStatisticSlot> stats = new ArrayList<SchematicStatisticSlot>();
		for (String key : compound.getCompoundTag("stats").getKeySet()) {
			stats.add(new SchematicStatisticSlot(key, compound.getCompoundTag("stats").getFloat(key)));
		}
		setStats(stats);
		
		ArrayList<SchematicCraftingSlot> crafting = new ArrayList<SchematicCraftingSlot>();
		for (String key : compound.getCompoundTag("crafting").getKeySet()) {
			NBTTagCompound craft = compound.getCompoundTag("crafting").getCompoundTag(key);
			crafting.add(new SchematicCraftingSlot(key, craft.getString("type"), craft.getInteger("amount")));
		}
		setCrafting(crafting);
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
	public ArrayList<SchematicStatisticSlot> getStats() {
		return this.stats;
	}

	@Override
	public void setStats(ArrayList<SchematicStatisticSlot> stats) {
		this.stats = stats;
	}

	@Override
	public ArrayList<SchematicCraftingSlot> getCrafting() {
		return this.crafting;
	}

	@Override
	public void setCrafting(ArrayList<SchematicCraftingSlot> crafting) {
		this.crafting = crafting;
	}
	
}