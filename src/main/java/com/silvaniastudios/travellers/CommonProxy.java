package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.CapabilityHandler;
import com.silvaniastudios.travellers.capability.knowledge.IKnowledge;
import com.silvaniastudios.travellers.capability.knowledge.Knowledge;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeStorage;
import com.silvaniastudios.travellers.capability.tree.ISchematicTree;
import com.silvaniastudios.travellers.capability.tree.SchematicTreeCapability;
import com.silvaniastudios.travellers.capability.tree.SchematicTreeStorage;

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

	public void init(FMLInitializationEvent event) {
		//OreDictionary.registerOre(CopperConfig.oredict.oreOreDict, new ItemStack(ModBlocks.blockOre1, 1, 0));
		//OreDictionary.registerOre(CopperConfig.oredict.nuggetOreDict, ModItems.nuggetCopper);
		CapabilityManager.INSTANCE.register(ISchematicTree.class, new SchematicTreeStorage(), SchematicTreeCapability.class);
		
		CapabilityManager.INSTANCE.register(IKnowledge.class, new KnowledgeStorage(), Knowledge.class);
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
}