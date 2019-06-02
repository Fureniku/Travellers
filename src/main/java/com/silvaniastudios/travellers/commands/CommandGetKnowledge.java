package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.capability.knowledge.IKnowledge;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandGetKnowledge extends CommandBase {
	@Override
	public String getName() {
		return "getkp";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.getkp.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 1) {
			throw new WrongUsageException("commands.getkp.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = args.length > 0 ? getPlayer(server, sender, args[0])
					: getCommandSenderAsPlayer(sender);

			if (entityplayermp.world != null) {
				IKnowledge knowledge = entityplayermp.getCapability(KnowledgeProvider.KNOWLEDGE, null);

				String message = String.format("§eYou have %d knowledge§r", knowledge.getKnowledge());
				entityplayermp.sendMessage(new TextComponentString(message));
			}
		}
	}

}
