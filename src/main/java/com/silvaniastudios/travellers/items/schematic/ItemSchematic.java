package com.silvaniastudios.travellers.items.schematic;

import java.util.List;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.client.gui.GuiSchematicInfoScreen;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStats;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemSchematic extends Item {

	private SchematicData schematicData;

	/**
	 * Base constructor for all the types of schematic
	 * 
	 * @param name
	 *            The name of the schematic
	 */
	public ItemSchematic(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		schematicData = new SchematicData();
		this.setMaxStackSize(1);
	}

	/**
	 * Empty Schematic - i.e no information Only used for the creative inventory icon
	 */
	public ItemSchematic() {
		this("schematic_default");
		
		ModItems.schematics.add(this);
		ModItems.schematicsByName.put("schematic_default", this);
	}

	/**
	 * Fixed Schematic - JSON data supplies all fields
	 */
	public ItemSchematic(String name, String rarity, SchematicStats stats,
			SchematicCrafting crafting) {
		this("schematic_" + name);

		this.setCreativeTab(Travellers.tabSchematics);
		schematicData.setName(name);
		schematicData.setRarity(SchematicRarityEnum.fromString(rarity));
		schematicData.setType(SchematicTypeEnum.FIXED);
		schematicData.setStats(stats);
		schematicData.setCrafting(crafting);
		ModItems.schematicsByName.put(name, this);
	}

	/**
	 * Procedural Schematic - Algorithm will generate stats + crafting when
	 * stack is created.
	 */
	public ItemSchematic(String name, SchematicTypeEnum type, SchematicRarityEnum rarity) {
		this("schematic_" + name);
		this.setCreativeTab(Travellers.tabSchematics);
		schematicData.setName(name);
		schematicData.setRarity(rarity);
		schematicData.setType(type);

		ModItems.schematics.add(this);
		ModItems.schematicsByName.put(name, this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.schematicData.getRarity().toMCRarity();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		boolean hasCapability = stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

		if (hasCapability) {
			ISchematicData schematicData = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

			switch (schematicData.getType()) {
			case ENGINE:
				return EngineProceduralData.getEngineName(schematicData);
			default:
				return super.getItemStackDisplayName(stack);
			}
		}

		return super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		boolean hasCapability = stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

		if (hasCapability) {
			ISchematicData schematicData = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			
			if (schematicData.getStats().size() == 0 && schematicData.getCrafting().size() == 0) {			
				schematicData = this.schematicData;
			}
			
			if (schematicData.getType() != SchematicTypeEnum.FIXED) {
				tooltip.add(I18n.format("travellers.tooltip.ship_parts." + schematicData.getType().name.toLowerCase()));
			} else {
				tooltip.add(I18n.format("travellers.tooltip." + schematicData.getName().toLowerCase()));
			}

			tooltip.add(String.format("%s%s%s", getRarity(stack).rarityColor, I18n.format("travellers.tooltip."
					+ schematicData.getType().name.toLowerCase() + "." + schematicData.getRarity().name.toLowerCase()),
					TextFormatting.RESET));

			for (SchematicStatisticSlot stat : schematicData.getStats()) {
				tooltip.add(String.format("%s: %s", I18n.format(stat.name), (flagIn == TooltipFlags.ADVANCED)
						? String.valueOf(stat.amount) : String.valueOf((int) Math.floor(stat.amount))));
			}

		}
	}

	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			boolean hasCapability = stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

			if (hasCapability) {

				ISchematicData schematicData = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

				if (schematicData.getStats().size() == 0 && schematicData.getCrafting().size() == 0) {
					schematicData.copyNBT(this.getSchematicData());

					if (schematicData.getType() != SchematicTypeEnum.FIXED) {
						schematicData.setStats(SchematicRandomGeneration.generateRandomStats(schematicData.getType(),
								schematicData.getRarity()));
						
						schematicData.setCrafting(EngineProceduralData.generateEngineCosts(schematicData));
						schematicData.generateUUID();
					}

				}
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	// TODO: investigate this because of broken dedicated server functionality
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		if (worldIn.isRemote) {
			FMLCommonHandler.instance().showGuiScreen(new GuiSchematicInfoScreen(playerIn.getHeldItem(handIn)));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
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

	@Override
	public void readNBTShareTag(ItemStack stack, NBTTagCompound nbt) {
		boolean hasCapability = stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
		
		if (hasCapability) {

			ISchematicData schematicData = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			schematicData.fromNBT(nbt);
			//System.out.println(schematicData.toNBT().toString());
		}
		super.readNBTShareTag(stack, nbt);
	}

	public ISchematicData getSchematicData() {
		return this.schematicData;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, "schematic_default");
	}
}
