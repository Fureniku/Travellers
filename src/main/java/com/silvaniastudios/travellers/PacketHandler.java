package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.network.LearnSchematicMessage;
import com.silvaniastudios.travellers.network.OpenKnowledgeTree;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;
import com.silvaniastudios.travellers.network.SalvageSchematicMessage;
import com.silvaniastudios.travellers.network.ScannerLineSyncMessage;
import com.silvaniastudios.travellers.network.UseKnowledgeNode;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Travellers.MODID);

	public static void registerPacketsClient() {
		int id = 0;

		INSTANCE.registerMessage(PlayerDataSyncMessage.CPlayerDataSyncMessageHandler.class, PlayerDataSyncMessage.class,
				id++, Side.CLIENT);

		// INSTANCE.registerMessage(PlayerDataSyncMessage.SPlayerDataSyncMessageHandler.class,
		// PlayerDataSyncMessage.class,
		// id++, Side.SERVER);

		INSTANCE.registerMessage(LearnSchematicMessage.SLearnSchematicMessageHandler.class, LearnSchematicMessage.class,
				id++, Side.SERVER);

		INSTANCE.registerMessage(SalvageSchematicMessage.SSalvageSchematicMessage.class, SalvageSchematicMessage.class,
				id++, Side.SERVER);

		INSTANCE.registerMessage(ScannerLineSyncMessage.CScannerLineSyncMessage.class, ScannerLineSyncMessage.class,
				id++, Side.CLIENT);

		INSTANCE.registerMessage(OpenKnowledgeTree.SOpenKnowledgeTreeHandler.class, OpenKnowledgeTree.class, id++,
				Side.SERVER);
		
		INSTANCE.registerMessage(UseKnowledgeNode.SUserKnowledgeNodeHandler.class, UseKnowledgeNode.class, id++,
				Side.SERVER);
	}
}
