package com.silvaniastudios.travellers.commands;

import java.util.Arrays;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.schematic.ItemSchematic;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class SchematicDataViewer extends CommandBase {

	@Override
	public String getName() {
		return "schemdata";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "schemdata";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityLivingBase entitylivingbase = (EntityLivingBase) getEntity(server, sender, args[0],
				EntityLivingBase.class);
		ItemStack schemHolding = entitylivingbase.getHeldItemMainhand();

		if (schemHolding.getItem() instanceof ItemSchematic
			&& schemHolding.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {
			
			ISchematicData schemdata = schemHolding.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			
			sender.sendMessage(new TextComponentString(schemdata.getUUID().toString()));
			sender.sendMessage(new TextComponentString(schemdata.getName()));
			sender.sendMessage(new TextComponentString(schemdata.getTooltip()));
			sender.sendMessage(new TextComponentString(schemdata.getRarity().name));
			sender.sendMessage(new TextComponentString(schemdata.getType().name));
			sender.sendMessage(new TextComponentString(Arrays.toString(schemdata.getTags())));
			sender.sendMessage(new TextComponentString(String.format("%b", schemdata.isUnlearnable())));
			sender.sendMessage(new TextComponentString(schemdata.getIconRef()));
			sender.sendMessage(new TextComponentString(Arrays.toString(schemdata.getBaseStats())));
			sender.sendMessage(new TextComponentString(String.valueOf(schemdata.getStatAmount())));
			sender.sendMessage(new TextComponentString(String.format("%b", schemdata.isDefault())));
		}
	}

}
