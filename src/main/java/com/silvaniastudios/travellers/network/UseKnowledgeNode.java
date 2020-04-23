package com.silvaniastudios.travellers.network;

import java.util.UUID;

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
import net.minecraft.util.text.TextComponentString;
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
						
						if (node.name.equals("engine.common") && playerData.getKnowledgeNodeUsage("engine.common") <= 1) {
							playerData.useKnowlegeNode("engine.power_generator");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("engine.power_generator"), playerData, true);
							playerData.useKnowlegeNode("engine.moonshine");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("engine.moonshine"), playerData, true);
						}
						
						if (node.name.equals("cannon.common") && playerData.getKnowledgeNodeUsage("cannon.common") <= 1) {
							playerData.useKnowlegeNode("cannon.cannon_shells");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("cannon.cannon_shells"), playerData, true);	
						}
						
						if (node.name.equals("swivel.common") && playerData.getKnowledgeNodeUsage("swivel.common") <= 1) {
							playerData.useKnowlegeNode("swivel.buckshot_shells");
							doNodeRewards(player, Travellers.KNOWLEDGE_TREE.getNode("swivel.buckshot_shells"), playerData, true);	
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
								String response = String.format("%sLearnt %s%s%s", TextFormatting.GOLD,
										stack.getRarity().rarityColor, stack.getDisplayName(), TextFormatting.RESET);

								player.sendMessage(new TextComponentString(response));
							} else {
								EntityItem dropItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY,
										player.posZ, stack.copy());
								dropItem.setPickupDelay(0);
								player.getEntityWorld().spawnEntity(dropItem);
							}
						}
					}
				} else if (node.rewards.type.equals("increase_schematic_slots")) {
					//TODO Schematic Slot limiting
					player.sendMessage(new TextComponentString("Increasing schematic slots has not been implemented yet"));
				} else if (node.rewards.type.equals("none")) {
					// Just learn nothing special here
					if (node.name.equals("reviver_interface")) {
						player.sendMessage(new TextComponentString("Connected to the Ancient Reviver Network"));
					} else if (node.name.equals("engine.moonshine")) {
						player.sendMessage(new TextComponentString("Moonshine is not implemented yet!"));
					}
				} else {
					player.sendMessage(new TextComponentString("There was an error learning that knowledge node"));
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

								player.sendMessage(new TextComponentString(String.format(
										"%1$sResearched %2$s%3$s%1$s for %2$s%4$d%1$s knowledge%2$s",
										TextFormatting.GOLD, TextFormatting.RESET, message.nodeKey, node.cost)));
								
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
