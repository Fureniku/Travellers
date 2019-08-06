package com.silvaniastudios.travellers.network;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerDataSyncMessage implements IMessage {

	IPlayerData instance;
	NBTTagCompound nbtData;

	public PlayerDataSyncMessage() {
	}

	public PlayerDataSyncMessage(IPlayerData playerData) {
		this.instance = playerData;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbtTag = ByteBufUtils.readTag(buf);
		
		this.nbtData = nbtTag;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.instance.toNBT());
	}

	public static class CPlayerDataSyncMessageHandler implements IMessageHandler<PlayerDataSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(PlayerDataSyncMessage message, MessageContext ctx) {
			IThreadListener mainThread = Minecraft.getMinecraft();

			mainThread.addScheduledTask(() -> {
				Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA, null)
						.fromNBT(message.nbtData);
			});

			return null;
		}

	}
}
