package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SalvageSchematicMessage implements IMessage {

	private ItemStack schematic;
	private UUID playerFrom;

	public SalvageSchematicMessage() {
	}

	public SalvageSchematicMessage(ItemStack stack, UUID player) {
		this.schematic = stack;
		this.playerFrom = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		this.playerFrom = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.schematic = ByteBufUtils.readItemStack(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerFrom.toString());
		ByteBufUtils.writeItemStack(buf, schematic);
	}

	public static class SSalvageSchematicMessage implements IMessageHandler<SalvageSchematicMessage, IMessage> {

		@Override
		public IMessage onMessage(SalvageSchematicMessage message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			server.addScheduledTask(() -> {
				
				EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.playerFrom);

				int increase = 0;

				switch (message.schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getRarity()) {
				default:
					increase = 15;
					break;
				case UNCOMMON:
					increase = 20;
					break;
				case RARE:
					increase = 30;
					break;
				case EXOTIC:
					increase = 45;
					break;
				case PRISTINE:
					increase = 55;
					break;
				case LEGENDARY:
					increase = 65;
					break;
				}

				player.getCapability(PlayerDataProvider.PLAYER_DATA, null).incrementKnowledgeBalance(increase);
				
				player.inventory.deleteStack(message.schematic);
				
				player.sendMessage(new TextComponentString(String.format("%sYou gained %s%d%s knowledge%s",
						TextFormatting.GOLD, TextFormatting.RESET, increase, TextFormatting.GOLD, TextFormatting.RESET)));

				PacketHandler.INSTANCE.sendTo(
						new PlayerDataSyncMessage(player.getCapability(PlayerDataProvider.PLAYER_DATA, null)),
						(EntityPlayerMP) player);
			});

			
			return null;
		}
	}

}
