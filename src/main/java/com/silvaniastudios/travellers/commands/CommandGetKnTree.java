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

public class CommandGetKnTree extends CommandBase {

	@Override
	public String getName() {
		return "kntree";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.kntree.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// /kntree 0:player
		if (args.length < 2 && args.length > 3) {
			throw new WrongUsageException("commands.kntree.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = args.length > 1 ? getPlayer(server, sender, args[0])
					: getCommandSenderAsPlayer(sender);

			if (entityplayermp.world != null) {
				IKnTree kntree = entityplayermp.getCapability(KnTreeProvider.KNTREE, null);

				entityplayermp.sendMessage(new TextComponentString(kntree.toString()));
			}
		}
	}

}
