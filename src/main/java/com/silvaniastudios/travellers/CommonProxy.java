package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.CapabilityHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataStorage;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataStorage;

import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

	public void registerItemRenderer(Item item, int meta, String id) {

	}

	public void preInit(FMLPreInitializationEvent event) {

		EnumHelper.addRarity("T_COMMON", TextFormatting.WHITE, "Common");
		EnumHelper.addRarity("T_UNCOMMON", TextFormatting.GREEN, "Uncommon");
		EnumHelper.addRarity("T_RARE", TextFormatting.YELLOW, "Rare");
		EnumHelper.addRarity("T_EXOTIC", TextFormatting.BLUE, "Exotic");
		EnumHelper.addRarity("T_PRISTINE", TextFormatting.LIGHT_PURPLE, "Pristine");
		EnumHelper.addRarity("T_LEGENDARY", TextFormatting.RED, "Legendary");

	}

	@SuppressWarnings("deprecation")
	public void init(FMLInitializationEvent event) {
		// OreDictionary.registerOre(CopperConfig.oredict.oreOreDict, new
		// ItemStack(ModBlocks.blockOre1, 1, 0));
		// OreDictionary.registerOre(CopperConfig.oredict.nuggetOreDict,
		// ModItems.nuggetCopper);

		CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData.class);
		CapabilityManager.INSTANCE.register(ISchematicData.class, new SchematicDataStorage(), SchematicData.class);

		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {		
	}
}