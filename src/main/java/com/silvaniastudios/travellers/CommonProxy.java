package com.silvaniastudios.travellers;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
		
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
	
	public void preInit(FMLPreInitializationEvent event) {

	}

	public void init(FMLInitializationEvent event) {
		//OreDictionary.registerOre(CopperConfig.oredict.oreOreDict, new ItemStack(ModBlocks.blockOre1, 1, 0));
		//OreDictionary.registerOre(CopperConfig.oredict.nuggetOreDict, ModItems.nuggetCopper);
	}
}
