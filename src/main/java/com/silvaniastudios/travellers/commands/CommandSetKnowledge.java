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

public class CommandSetKnowledge extends CommandBase {
	@Override
	public String getName() {
		return "setkp";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.setkp.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// /setkp 0:player 1:kp
		if (args.length < 2 && args.length > 3) {
			throw new WrongUsageException("commands.setkp.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = args.length > 1 ? getPlayer(server, sender, args[0])
					: getCommandSenderAsPlayer(sender);
			int kpAmount = args.length >= 2 ? parseInt(args[1]) : 0;

			if (entityplayermp.world != null) {
				IKnowledge knowledge = entityplayermp.getCapability(KnowledgeProvider.KNOWLEDGE, null);
				knowledge.setKnowledge(kpAmount);
				String message = String.format("§eSet knowledge to %d§r", knowledge.getKnowledge());
				entityplayermp.sendMessage(new TextComponentString(message));
			}
		}
	}

}
