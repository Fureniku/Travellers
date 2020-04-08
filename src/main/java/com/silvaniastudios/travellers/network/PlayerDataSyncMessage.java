package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Syncs player's PlayerData capability from server to client
 * 
 * @author jamesm2w
 */
public class PlayerDataSyncMessage implements IMessage {

	IPlayerData instance;
	NBTTagCompound nbtData;
	
	UUID player = UUID.randomUUID();

	public PlayerDataSyncMessage() {
	}

	public PlayerDataSyncMessage(IPlayerData playerData) {
		this.instance = playerData;
	}
	
	public PlayerDataSyncMessage(IPlayerData playerData, UUID uuid) {
		this.instance = playerData;
		this.player = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbtTag = ByteBufUtils.readTag(buf);
		UUID uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.player = uuid;
		this.nbtData = nbtTag;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.instance.toNBT());
		ByteBufUtils.writeUTF8String(buf, this.player.toString());
	}

	public static class CPlayerDataSyncMessageHandler implements IMessageHandler<PlayerDataSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(PlayerDataSyncMessage message, MessageContext ctx) {
			IThreadListener mainThread = Minecraft.getMinecraft();

			mainThread.addScheduledTask(() -> {
				IPlayerData playerData = Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
				
				playerData.fromNBT(message.nbtData);
				
				//System.out.println("scanner_line read from nbt is " + entityScanning.getUniqueID().toString());
				//Minecraft.getMinecraft().world.spawnEntity(entityScanning);
				//EntityScannerLine entityScanning = playerData.getScanningEntity();
				//if (entityScanning != null) {
					//System.out.println("scanner_line read from nbt is " + entityScanning.getUniqueID().toString());
				//	Minecraft.getMinecraft().world.spawnEntity(entityScanning);
				//}
			});

			return null;
		}

	}
	
	public static class SPlayerDataSyncMessageHandler implements IMessageHandler<PlayerDataSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(PlayerDataSyncMessage message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.player);
			
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			playerData.fromNBT(message.nbtData);
			
			return null;
		}
		
	}
}
