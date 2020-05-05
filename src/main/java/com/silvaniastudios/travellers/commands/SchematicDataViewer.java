package com.silvaniastudios.travellers.commands;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;

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
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
	
		ItemStack schemHolding = player.getHeldItemMainhand();

		if (schemHolding.getItem() instanceof ItemSchematic
			&& schemHolding.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {
			
			ISchematicData schemdata = schemHolding.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			System.out.println(schemdata.toNBT().toString());
			sender.sendMessage(new TextComponentString("UUID: " + schemdata.getUUID().toString()));
			sender.sendMessage(new TextComponentString("Name: " + schemdata.getName()));
			sender.sendMessage(new TextComponentString("Rarity: " + schemdata.getRarity().toString()));
			sender.sendMessage(new TextComponentString("Type: " + schemdata.getType().toString()));
			sender.sendMessage(new TextComponentString("Stats: " + schemdata.getStats().toString()));
			sender.sendMessage(new TextComponentString("Crafting: " + schemdata.getCrafting().toString()));
			sender.sendMessage(new TextComponentString("Categories: " + schemdata.getCategories().toString()));
		}
	}

}
