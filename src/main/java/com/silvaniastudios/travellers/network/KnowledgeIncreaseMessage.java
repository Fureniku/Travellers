package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Informs the server that a client's knowledge has increased
 * 
 * @author jamesm2w
 */
public class KnowledgeIncreaseMessage implements IMessage {

	private int amount;
	private UUID uuid;

	public KnowledgeIncreaseMessage() {
	}

	public KnowledgeIncreaseMessage(int amount, UUID playerUUID) {
		this.amount = amount;
		this.uuid = playerUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		this.amount = buf.readInt();
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));

	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(this.amount);
		ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
	}

	public static class SKnowledgeIncreaseMessage implements IMessageHandler<KnowledgeIncreaseMessage, IMessage> {

		@Override
		public IMessage onMessage(KnowledgeIncreaseMessage message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.uuid);

			player.getCapability(PlayerDataProvider.PLAYER_DATA, null).incrementKnowledgeBalance(message.amount);

			String response = String.format("%sYou gained %s%d%s knowledge%s", TextFormatting.GOLD,
					TextFormatting.RESET, message.amount, TextFormatting.GOLD, TextFormatting.RESET);
			player.sendMessage(new TextComponentString(response));

			PacketHandler.INSTANCE.sendTo(
					new PlayerDataSyncMessage(player.getCapability(PlayerDataProvider.PLAYER_DATA, null)),
					(EntityPlayerMP) player);
			return null;
		}

	}

}
