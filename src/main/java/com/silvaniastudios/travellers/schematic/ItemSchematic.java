package com.silvaniastudios.travellers.schematic;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.client.gui.GuiSchematicInfoScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSchematic extends Item {
	
	String name;

	SchematicRarityEnum rarity;
	SchematicTypeEnum type;
	String[] tags;
	boolean unlearnable;
	String iconRef;
	
	float baseHp;
	UUID uuid;
	
	boolean listInCreativeTab = true;

	/*
	 * Empty Schematic
	 */
	public ItemSchematic(String name) {
		this(name, SchematicRarityEnum.COMMON, false, 0F, false);
	}

	/*
	 * Fixed Schematic
	 */
	public ItemSchematic(String name, SchematicRarityEnum rarity, boolean unlearnable, float baseHp, boolean list) {
		this(name, rarity, SchematicTypeEnum.FIXED, new String[] {}, unlearnable, "schematic_default", list);
		
		this.uuid = UUID.nameUUIDFromBytes(this.name.getBytes());
		this.baseHp = baseHp;
		this.listInCreativeTab = list;
	}

	/*
	 * Proc Schematic
	 */
	public ItemSchematic(String name, SchematicRarityEnum rarity, SchematicTypeEnum type, String[] tags,
			boolean unlearnable, String iconRef, boolean list) {
		
		this.name = name;
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		
		this.rarity = rarity;
		this.type = type;
		this.tags = (tags.length == 0) ? new String[] {
				name, rarity.name, type.name, iconRef
		} : tags;
		this.unlearnable = unlearnable;
		this.iconRef = iconRef;
		
		this.listInCreativeTab = list;
		
		if (this.listInCreativeTab) {
			this.setCreativeTab(Travellers.tabSchematics);
		}
		
		this.setMaxStackSize(1);
		ModItems.schematics.add(this);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		if (stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {

			ISchematicData schemData = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

			if (schemData.isDefault()) {
				schemData.setRarity(this.rarity);
				schemData.setType(this.type);
				
				schemData.setName(this.getUnlocalizedName());
				schemData.setTooltip("travellers.tooltip." + this.name);
				schemData.setIconRef(iconRef);
				schemData.setTags(this.tags);
				schemData.setUnlearnable(this.unlearnable);
				schemData.setStatAmount(this.type.statNo);
				
				if (this.type != SchematicTypeEnum.FIXED) {
					schemData.generateRandomBaseStats();
					byte[] byteArray = floatArray2ByteArray(schemData.getBaseStats());
					schemData.setUUID(UUID.nameUUIDFromBytes(byteArray));
					
				} else {
					schemData.setUUID(uuid);
					if (baseHp != 0) {
						schemData.setBaseStats(new float[] {baseHp});
						schemData.setStatAmount(1);
					}
				}

				schemData.setDefault(false);
			}
			
			tooltip.add(I18n.format("travellers.tooltip." + this.name));

			if (flagIn == TooltipFlags.ADVANCED){
				float[] stats = schemData.getBaseStats();

				for (int i = 0; i < stats.length; i++) {
					tooltip.add(String.format("%s: %f", I18n.format(SchematicTypeEnum.getStatNames(type)[i]), stats[i]));
				}
			} else {
				int[] stats = schemData.getRoundedStats();

				for (int i = 0; i < stats.length; i++) {
					tooltip.add(String.format("%s: %d", I18n.format(SchematicTypeEnum.getStatNames(type)[i]), stats[i]));
				}
			}
			if (this.type == SchematicTypeEnum.ENGINE) {
				String displayName = String.format("§r%s%s§r", SchematicRarityEnum.color(this.rarity), ProcSchematicConfig.getEngineName(schemData.getBaseStats(), this.rarity));
				stack.setStackDisplayName(displayName);
			}
			
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiSchematicInfoScreen(playerIn.getHeldItem(handIn)));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, iconRef);
	}
	
	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound capabilityTag = new NBTTagCompound();
		if (stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {
			capabilityTag = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).toNBT();
		}
		
		if (super.getNBTShareTag(stack) != null) {
			capabilityTag.merge(super.getNBTShareTag(stack));
		}
		return capabilityTag;
	}
	
	// No shame copied from StackOverflow
	public static byte[] floatArray2ByteArray(float[] values){
	    ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);

	    for (float value : values){
	        buffer.putFloat(value);
	    }

	    return buffer.array();
	}
	
}
