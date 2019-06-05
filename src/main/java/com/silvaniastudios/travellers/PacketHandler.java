package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.network.KnowledgeMessage;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Travellers.MODID);
	
	public static void registerPacketsClient () {
		int id = 0;
		
		INSTANCE.registerMessage(KnowledgeMessage.CKnowledgeMessageHandler.class, KnowledgeMessage.class, id++,
				Side.CLIENT);
	}
}
