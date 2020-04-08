package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.entity.EntityScannerLine;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author jamesm2w
 */
public class ScannerLineSyncMessage implements IMessage {

	UUID playerId;
	UUID entityId;

	public ScannerLineSyncMessage() {
	}

	public ScannerLineSyncMessage(EntityScannerLine entity) {
		playerId = entity.getPlayer().getUniqueID();
		entityId = entity.getUniqueID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);

		playerId = tag.getUniqueId("player");
		entityId = tag.getUniqueId("entity");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setUniqueId("player", playerId);
		tag.setUniqueId("entity", entityId);

		ByteBufUtils.writeTag(buf, tag);
	}

	public static class CScannerLineSyncMessage implements IMessageHandler<ScannerLineSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(ScannerLineSyncMessage message, MessageContext ctx) {

			IThreadListener mainThread = Minecraft.getMinecraft();

			mainThread.addScheduledTask(() -> {
				//System.out.println("Got sync message for entity " + message.entityId.toString() + " player " + message.playerId.toString());
				Minecraft.getMinecraft().world.getLoadedEntityList().forEach((entity) -> {
					if (entity instanceof EntityScannerLine && entity.getUniqueID().compareTo(message.entityId) == 0) {
						
						EntityScannerLine scannerLine = (EntityScannerLine) entity;
						
						//System.out.println("Updating entity " + scannerLine.getUniqueID().toString() + " from Scanner Line Sync. IUUID: " + scannerLine.initialUUID.toString());
						scannerLine
								.setPlayer(Minecraft.getMinecraft().world.getPlayerEntityByUUID(message.playerId));
					}
				});
			});

			return null;
		}

	}

}
