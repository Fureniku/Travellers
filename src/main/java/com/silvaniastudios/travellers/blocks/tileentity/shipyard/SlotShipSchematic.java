package com.silvaniastudios.travellers.blocks.tileentity.shipyard;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotShipSchematic extends SlotItemHandler {

	public SlotShipSchematic(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		
		return false;
	}

}