package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.CapabilityHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataStorage;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
		
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
	
	public void preInit(FMLPreInitializationEvent event) {

	}

	@SuppressWarnings("deprecation")
	public void init(FMLInitializationEvent event) {
		//OreDictionary.registerOre(CopperConfig.oredict.oreOreDict, new ItemStack(ModBlocks.blockOre1, 1, 0));
		//OreDictionary.registerOre(CopperConfig.oredict.nuggetOreDict, ModItems.nuggetCopper);

		CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData.class);
		
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
}