package com.silvaniastudios.travellers.client;

import com.silvaniastudios.travellers.CommonProxy;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		PacketHandler.registerPacketsClient();
		
		super.preInit(event);
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Travellers.MODID + ":" + id, "inventory"));
	}

}