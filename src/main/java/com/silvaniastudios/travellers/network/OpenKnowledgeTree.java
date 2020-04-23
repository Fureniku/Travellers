package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.GuiHandler.TravellersContainers;
import com.silvaniastudios.travellers.Travellers;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenKnowledgeTree implements IMessage {

	public UUID playerUUID;

	public OpenKnowledgeTree(EntityPlayer player) {

		this.playerUUID = player.getUniqueID();
	}

	public OpenKnowledgeTree() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.playerUUID.toString());
	}

	public static class SOpenKnowledgeTreeHandler implements IMessageHandler<OpenKnowledgeTree, IMessage> {

		@Override
		public IMessage onMessage(OpenKnowledgeTree message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			server.addScheduledTask(() -> {
				EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.playerUUID);

				player.openGui(Travellers.instance, TravellersContainers.KNOWLEDGE_TREE.ordinal(),
						player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);

			});

			return null;
		}

	}

}
