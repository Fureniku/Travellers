package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class KnowledgeCommand extends CommandBase {

	@Override
	public String getName() {
		return "kp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "kp [int]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			
			try {
				playerData.setKnowledgeBalance(Integer.parseInt(args[0]));
				sender.sendMessage(new TextComponentString(String.valueOf(playerData.getKnowledgeBalance())));
				
				PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP)player);
				
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(new TextComponentString(e.toString()));
			}
			
		} else {
			sender.sendMessage(new TextComponentString("No player data found"));
		}
		
	}

}
