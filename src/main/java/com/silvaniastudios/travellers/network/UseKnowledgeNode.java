package com.silvaniastudios.travellers.network;

import java.util.UUID;

import com.silvaniastudios.travellers.ChatHandler;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.data.KnowledgeTreeData.KnowledgeNode;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
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

public class UseKnowledgeNode implements IMessage {

	String nodeKey;
	UUID player;

	public UseKnowledgeNode(String nodeKey, EntityPlayer player) {
		this.nodeKey = nodeKey;
		this.player = player.getUniqueID();
	}

	public UseKnowledgeNode() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.nodeKey = ByteBufUtils.readUTF8String(buf);
		this.player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, nodeKey);
		ByteBufUtils.writeUTF8String(buf, player.toString());
	}

	public static class SUserKnowledgeNodeHandler implements IMessageHandler<UseKnowledgeNode, IMessage> {

		public void doNodeRewards(EntityPlayer player, KnowledgeNode node, IPlayerData playerData, boolean instaLearn) {
			if (node.rewards != null) {
				if (node.rewards.type.equals("learn_procedural")) {

					ItemSchematic proceduralSchematic = ModItems.schematicsByName.get(node.name);

					if (proceduralSchematic != null) {
						ItemStack stack = new ItemStack(proceduralSchematic);

						EntityItem dropItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY,
								player.posZ, stack.copy());
						dropItem.setPickupDelay(0);
						player.getEntityWorld().spawnEntity(dropItem);

						if (node.name.equals("engine.common")
								&& playerData.getKnowledgeNodeUsage("engine.common") <= 1) {
							playerData.useKnowlegeNode("engine.power_generator");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("engine.power_generator"),
									playerData, true);
							playerData.useKnowlegeNode("engine.moonshine");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("engine.moonshine"), playerData,
									true);
						}

						if (node.name.equals("cannon.common")
								&& playerData.getKnowledgeNodeUsage("cannon.common") <= 1) {
							playerData.useKnowlegeNode("cannon.cannon_shells");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("cannon.cannon_shells"), playerData,
									true);
						}

						if (node.name.equals("swivel.common")
								&& playerData.getKnowledgeNodeUsage("swivel.common") <= 1) {
							playerData.useKnowlegeNode("swivel.buckshot_shells");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("swivel.buckshot_shells"),
									playerData, true);
						}
					}

				} else if (node.rewards.type.equals("learn_schematic") && node.rewards.schematics != null) {
					for (String schematicName : node.rewards.schematics) {
						ItemSchematic schematic = ModItems.schematicsByName.get(schematicName);
						if (schematic != null) {
							ItemStack stack = new ItemStack(schematic);

							if (instaLearn) {
								schematic.onUpdate(stack, player.getEntityWorld(), player, 0, false);
								playerData.learnSchematic(stack);

								ITextComponent msg = ChatHandler.translatedString("chat.message.learnSchematic",
										TextFormatting.GOLD, ChatHandler.schematicString(stack));

								player.sendMessage(msg);
							} else {
								EntityItem dropItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY,
										player.posZ, stack.copy());
								dropItem.setPickupDelay(0);
								player.getEntityWorld().spawnEntity(dropItem);
							}
						}
					}
				} else if (node.rewards.type.equals("increase_schematic_slots")) {

					ITextComponent msg = ChatHandler.translatedString("chat.message.increasedSlots",
							TextFormatting.GOLD);

					player.sendMessage(msg);
				} else if (node.rewards.type.equals("none")) {
					// Just learn nothing special here
					if (node.name.equals("reviver_interface")) {

						ITextComponent msg = ChatHandler.translatedString("chat.message.reviverNetwork",
								TextFormatting.GOLD);

						player.sendMessage(msg);
					} else if (node.name.equals("engine.moonshine")) {
						// TODO: schematics and moonshine

						ITextComponent msg = ChatHandler.translatedString("chat.message.notImplemented",
								TextFormatting.BLUE, "Moonshine");
						player.sendMessage(msg);
					}
				} else {
					ITextComponent msg = ChatHandler.translatedString("chat.message.error", TextFormatting.RED,
							"process knowledge node rewards");
					player.sendMessage(msg);
				}
			}
		}

		@Override
		public IMessage onMessage(UseKnowledgeNode message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			server.addScheduledTask(() -> {

				EntityPlayer player = server.getPlayerList().getPlayerByUUID(message.player);

				if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
					IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

					KnowledgeNode node = Travellers.KNOWLEDGE_TREE.getNode(message.nodeKey);
					int prevUses = playerData.getKnowledgeNodeUsage(message.nodeKey);

					if (node != null) {

						if (playerData.getKnowledgeBalance() >= node.cost) {
							if (node.infiniteRoll == 1 || prevUses < node.maxUses) {
								playerData.useKnowlegeNode(message.nodeKey);
								playerData.incrementKnowledgeBalance(-node.cost);

								ITextComponent msg = ChatHandler.translatedString("chat.message.researchNode",
										TextFormatting.GOLD,
										ChatHandler.translatedString("travellers.node." + message.nodeKey,
												TextFormatting.WHITE),
										ChatHandler.number(node.cost, TextFormatting.WHITE));

								player.sendMessage(msg);

								doNodeRewards(player, node, playerData, false);
							}
						}

					}

					PacketHandler.INSTANCE.sendTo(
							new PlayerDataSyncMessage(player.getCapability(PlayerDataProvider.PLAYER_DATA, null)),
							(EntityPlayerMP) player);
				}

			});

			return null;
		}

	}

}
