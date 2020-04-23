package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class PlayerDataViewer extends CommandBase {

	@Override
	public String getName() {
		return "playerData";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "playerData";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			
			sender.sendMessage(new TextComponentString("Knowledge: " + String.valueOf(playerData.getKnowledgeBalance())));
			sender.sendMessage(new TextComponentString("Shipyard: " + String.valueOf(playerData.getShipyardVisitorCode())));
			sender.sendMessage(new TextComponentString("IsDev: " + String.valueOf(playerData.isDev())));
			sender.sendMessage(new TextComponentString("EntityScanning: " + String.valueOf(playerData.getScanningEntity())));
			sender.sendMessage(new TextComponentString("Lore Pieces: " + playerData.getKnownLorePieces().toString()));
			sender.sendMessage(new TextComponentString("Schematic List: " + playerData.getSchematicList().toString()));
			sender.sendMessage(new TextComponentString("Scanned Objects: " + playerData.getScannedObjects().toString()));
			
			sender.sendMessage(new TextComponentString("Knowledge Tree: " + playerData.getKnowledgeTreeUses().toString()));
			
		} else {
			sender.sendMessage(new TextComponentString("No player data found"));
		}
		
	}

}
