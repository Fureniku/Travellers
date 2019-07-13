package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.capability.tree.IKnTree;
import com.silvaniastudios.travellers.capability.tree.KnTreeProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandResetKnTree extends CommandBase {

	@Override
	public String getName() {
		return "resetkn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2 && args.length > 3) {
			throw new WrongUsageException("commands.kntree.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = args.length > 1 ? getPlayer(server, sender, args[0])
					: getCommandSenderAsPlayer(sender);

			if (entityplayermp.world != null) {
				IKnTree kntree = entityplayermp.getCapability(KnTreeProvider.KNTREE, null);
				kntree.buildMap();
				entityplayermp.sendMessage(new TextComponentString(kntree.toString()));
			}
		}
	}

}
