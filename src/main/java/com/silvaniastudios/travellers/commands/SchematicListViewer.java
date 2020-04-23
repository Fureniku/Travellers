package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * 
 * @author jamesm2w
 *
 */
public class SchematicListViewer extends CommandBase {

	@Override
	public String getName() {
		return "schemlist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "schemlist";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			sender.sendMessage(new TextComponentString("Max size = +" + String.valueOf(playerData.getKnowledgeNodeUsage("increase_slots"))));
			for (ItemStack stack : playerData.getSchematicList()) {
				sender.sendMessage(new TextComponentString(stack.getDisplayName() + " UUID {" + stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getUUID() + "}"));
			}
		}
		
	}

}
