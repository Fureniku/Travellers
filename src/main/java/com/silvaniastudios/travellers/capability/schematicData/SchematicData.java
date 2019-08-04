package com.silvaniastudios.travellers.capability.schematicData;

import java.util.UUID;

import com.silvaniastudios.travellers.schematic.RandomSchematicGen;
import com.silvaniastudios.travellers.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.schematic.SchematicTypeEnum;

import net.minecraft.nbt.NBTTagCompound;

public class SchematicData implements ISchematicData {
	
	private UUID uuid;
	private SchematicRarityEnum rarity;
	private SchematicTypeEnum type;
	private String name;
	private String tooltip;
	private String iconRef;
	private String[] tags;
	private boolean unlearnable;
	private float[] baseStats;
	private int statAmount;
	
	private boolean isDefault = true;
	
	public SchematicData () {
		setUUID(UUID.randomUUID());
		setRarity(SchematicRarityEnum.COMMON);
		setType(SchematicTypeEnum.FIXED);
		setName("blank.schematic");
		setTooltip("default_schematic");
		setIconRef("schematic_default");
		setTags(new String[]{});
		setUnlearnable(false);
		setBaseStats(generateRandomBaseStats());
		setStatAmount(1);
		setDefault(true);
	}
	

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public SchematicRarityEnum getRarity() {
		return rarity;
	}

	@Override
	public void setRarity(SchematicRarityEnum rarity) {
		this.rarity = rarity;
	}

	@Override
	public SchematicTypeEnum getType() {
		return type;
	}

	@Override
	public void setType(SchematicTypeEnum type) {
		this.type = type;
	}

	@Override
	public String[] getTags() {
		return tags;
	}

	@Override
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	@Override
	public String[] addTag(String tag) {
		String[] newTags = new String[tags.length + 1];
		for (int i = 0; i < tags.length; i++) {
			newTags[i] = tags[i];
		}
		newTags[tags.length + 1] = tag;
		setTags(newTags);
		return tags;
	}

	@Override
	public boolean hasTag(String tag) {
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == tag) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String[] removeTag(String tag) {
		String[] newTags = new String[tags.length - 1];
		
		for (int i = 0; i < tags.length; i++) {
			if (!(tags[i] == tag)) {
				newTags[i] = tags[i];
			} else {
				i--;
			}
		}
		setTags(newTags);
		return tags;
	}

	@Override
	public boolean isUnlearnable() {
		return unlearnable;
	}

	@Override
	public void setUnlearnable(boolean bool) {
		unlearnable = bool;
	}

	@Override
	public String getIconRef() {
		return iconRef;
	}

	@Override
	public void setIconRef(String iconRef) {
		this.iconRef = iconRef;
	}

	@Override
	public float[] getBaseStats() {
		return baseStats;
	}

	@Override
	public void setBaseStats(float[] baseStats) {
		this.baseStats = baseStats;
	}

	@Override
	public int[] getRoundedStats() {
		int[] roundedStats = new int[baseStats.length];
		
		for (int i = 0; i < baseStats.length; i++) {
			roundedStats[i] = Math.round(baseStats[i]);
		}
		
		return roundedStats;
	}
	
	@Override
	public float[] generateRandomBaseStats() {
		float[] baseStats = new float[statAmount];
		
		baseStats = RandomSchematicGen.generateRandomStats(type, rarity);
		
		setBaseStats(baseStats);
		return baseStats;
	}

	@Override
	public int getStatAmount() {
		return statAmount;
	}

	@Override
	public void setStatAmount(int stat) {
		statAmount = stat;
	}
	
	@Override
	public boolean isDefault() {
		return this.isDefault;
	}
	
	@Override
	public void setDefault(boolean bool) {
		this.isDefault = bool;
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setUniqueId("uuid", getUUID());
		nbt.setInteger("rarity", getRarity().tier);
		nbt.setString("type", getType().name);
		nbt.setString("name", getName());
		nbt.setString("tooltip", getTooltip());
		nbt.setString("iconRef", getIconRef());

		NBTTagCompound tags = new NBTTagCompound();
		for (int i = 0; i < getTags().length; i++) {
			tags.setString(String.valueOf(i), getTags()[i]);
		}
		nbt.setTag("tags", tags);
		nbt.setBoolean("unlearnable", isUnlearnable());

		NBTTagCompound baseStats = new NBTTagCompound();
		for (int i = 0; i < getBaseStats().length; i++) {
			baseStats.setFloat(String.valueOf(i), getBaseStats()[i]);
		}
		nbt.setTag("baseStats", baseStats);
		nbt.setInteger("statAmount", getStatAmount());
		
		nbt.setBoolean("default", isDefault);

		return nbt;
	}


	@Override
	public ISchematicData fromNBT(NBTTagCompound nbtTag) {
		
		setUUID(nbtTag.getUniqueId("uuid"));
		setRarity(SchematicRarityEnum.fromTier(nbtTag.getInteger("rarity")));
		setType(SchematicTypeEnum.fromString(nbtTag.getString("type")));
		setName(nbtTag.getString("name"));
		setName(nbtTag.getString("tooltip"));
		setIconRef(nbtTag.getString("iconRef"));
		
		NBTTagCompound tags = nbtTag.getCompoundTag("tags");
		String[] tagArr = new String[tags.getSize()];
		for (int i = 0; i < tagArr.length; i++) {
			tagArr[i] = tags.getString(String.valueOf(i));
		}
		setTags(tagArr);
		
		setUnlearnable(nbtTag.getBoolean("unlearnable"));
		
		NBTTagCompound baseStats = nbtTag.getCompoundTag("baseStats");
		float[] statArr = new float[baseStats.getSize()];
		for (int i = 0; i < statArr.length; i++) {
			statArr[i] = baseStats.getFloat(String.valueOf(i));
		}
		setBaseStats(statArr);
		
		setStatAmount(nbtTag.getInteger("statAmount"));
		
		setDefault(nbtTag.getBoolean("default"));
		
		return this;
	}

}
