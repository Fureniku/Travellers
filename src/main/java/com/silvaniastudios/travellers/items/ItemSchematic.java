package com.silvaniastudios.travellers.items;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSchematic extends ItemBasic {

	private UUID uUID;
	private int rarity;
	private String type;
	private String[] tags;
	private String title;
	private boolean unlearnable = false;
	private String iconID = "schematic_default";
	private int timeToCraft;
	private int baseHp;

	private HashMap<String, Integer> baseStats;
	private int[] cipherSlots; // TODO: change type of this

	public ItemSchematic(String name) {
		super(name);

		this.uUID = UUID.randomUUID();
		this.setCreativeTab(Travellers.tabSchematics);
		ModItems.schematics.add(this);
	}

	public ItemSchematic(String name, int rarity, String type, String[] tags, String title, boolean unlearnable,
			String iconID, int timeToCraft, int baseHp) {
		
		super(name);
		
		this.uUID = UUID.randomUUID();
		this.rarity = rarity;
		this.type = type;
		this.tags = tags;
		this.title = title;
		this.unlearnable = unlearnable;
		this.iconID = (iconID.length() > 0) ? iconID : "schematic_default";
		this.timeToCraft = timeToCraft;
		this.baseHp = baseHp;
		
		this.setCreativeTab(Travellers.tabSchematics);
		
		ModItems.schematics.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("travellers.tooltip." + this.title));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// TODO Auto-generated method stub
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, iconID);
	}

	public UUID getUUID() {
		return this.uUID;
	}

	protected void setUUID(UUID uuid) {
		this.uUID = uuid;
	}

	public int getRarity() {
		return this.rarity;
	}

	protected void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public String getType() {
		return this.type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	public String[] getTags() {
		return this.tags;
	}

	protected void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return this.title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	public boolean unlearnable() {
		return this.unlearnable;
	}

	protected void setUnlearnable(boolean un) {
		this.unlearnable = un;
	}

	public String getIconID() {
		return this.iconID;
	}

	protected void setIconID(String iconID) {
		this.iconID = iconID;
	}

	public int getTimeToCraft() {
		return this.timeToCraft;
	}

	protected void setTimeToCraft(int time) {
		this.timeToCraft = time;
	}

	public int getBaseHp() {
		return this.baseHp;
	}

	protected void setBaseHp(int baseHp) {
		this.baseHp = baseHp;
	}

	public HashMap<String, Integer> getBaseStats() {
		return this.baseStats;
	}

	protected void setBaseStats(HashMap<String, Integer> stats) {
		this.baseStats = stats;
	}

	public int[] getCipherSlots() { // TODO: type of cipher slots
		return this.cipherSlots;
	}

	protected void setCipherSlots(int[] cipherslots) {
		this.cipherSlots = cipherslots;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setString("uuid", this.uUID.toString());
		nbt.setInteger("rarity", this.rarity);
		nbt.setString("type", this.type);

		NBTTagCompound tags = new NBTTagCompound();
		for (int i = 0; i < this.tags.length; i++) {
			tags.setInteger(this.tags[i], i);
		}

		nbt.setTag("tags", tags);
		nbt.setString("title", this.title);
		nbt.setBoolean("unlearnable", this.unlearnable);
		nbt.setString("iconID", iconID);
		nbt.setInteger("timeToCraft", timeToCraft);
		nbt.setInteger("baseHp", baseHp);

		NBTTagCompound stats = new NBTTagCompound();
		for (String key : this.baseStats.keySet()) {
			stats.setInteger(key, this.baseStats.get(key));
		}

		nbt.setTag("baseStats", stats);
		nbt.setIntArray("cipherSlots", cipherSlots);// TODO: revise this for
													// ciphers

		return nbt;
	}

	public static ItemSchematic fromNBT(NBTTagCompound nbt) {
		ItemSchematic newItem = new ItemSchematic(nbt.getString("title"));
		newItem.setUUID(UUID.fromString(nbt.getString("uuid")));

		newItem.setRarity(nbt.getInteger("rarity"));
		newItem.setType(nbt.getString("type"));

		Set<String> tagset = ((NBTTagCompound) nbt.getTag("tags")).getKeySet();
		newItem.setTags(tagset.toArray(new String[tagset.size()]));

		newItem.setTitle(nbt.getString("title"));
		newItem.setUnlearnable(nbt.getBoolean("unlearnable"));
		newItem.setIconID(nbt.getString("iconID"));
		newItem.setTimeToCraft(nbt.getInteger("timeToCraft"));
		newItem.setBaseHp(nbt.getInteger("baseHp"));

		HashMap<String, Integer> baseStats = new HashMap<String, Integer>();
		for (String key : ((NBTTagCompound) nbt.getTag("baseStats")).getKeySet()) {
			baseStats.put(key, ((NBTTagCompound) nbt.getTag("baseStats")).getInteger(key));
		}
		newItem.setBaseStats(baseStats);

		newItem.setCipherSlots(nbt.getIntArray("cipherSlots")); // TODO: ciphers
																// revision

		return newItem;
	}
}
