package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.ChatHandler;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Informs server that a player client has learned a schematic
 * 
 * @author james_pntzyfo
 */
public class LearnSchematicMessage implements IMessage {

	private ItemStack schematic;
	private UUID uuid;

	public LearnSchematicMessage() {
	}

	public LearnSchematicMessage(ItemStack schematic, UUID playeruuid) {
		this.schematic = schematic;
		this.uuid = playeruuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		this.schematic = ByteBufUtils.readItemStack(buf);
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).fromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {

		ByteBufUtils.writeItemStack(buf, schematic);
		ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
		ByteBufUtils.writeTag(buf, schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).toNBT());
	}

	public static class SLearnSchematicMessageHandler implements IMessageHandler<LearnSchematicMessage, IMessage> {

		@Override
		public IMessage onMessage(LearnSchematicMessage message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			server.addScheduledTask(() -> {
				EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.uuid);

				player.getCapability(PlayerDataProvider.PLAYER_DATA, null).learnSchematic(message.schematic);

				ITextComponent msg = ChatHandler.translatedString("chat.message.learnSchematic", TextFormatting.GOLD, 
						ChatHandler.schematicString(message.schematic));
				
				player.sendMessage(msg);
				
				player.inventory.deleteStack(message.schematic);
				
				PacketHandler.INSTANCE.sendTo(
						new PlayerDataSyncMessage(player.getCapability(PlayerDataProvider.PLAYER_DATA, null)),
						(EntityPlayerMP) player);
			});
			
			return null;
		}

	}
}
