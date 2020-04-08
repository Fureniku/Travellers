package com.silvaniastudios.travellers.capability;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.schematic.ItemSchematic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * @author jamesm2w
 */
public class CapabilityHandler {

	public static final ResourceLocation PLAYER_DATA = new ResourceLocation(Travellers.MODID, "playerData");

	public static final ResourceLocation SCHEMATIC_DATA = new ResourceLocation(Travellers.MODID, "schematicData");

	@SubscribeEvent
	public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		Entity obj = event.getObject();
		if (obj instanceof EntityPlayer) {
			event.addCapability(PLAYER_DATA, new PlayerDataProvider());
		}
	}

	@SubscribeEvent
	public void attachCapabilityItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack itemStack = event.getObject();

		if (itemStack.getItem() instanceof ItemSchematic) {

			event.addCapability(SCHEMATIC_DATA, new SchematicDataProvider());

		}
	}
}
