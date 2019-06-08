package com.silvaniastudios.travellers.blocks.tileentity;

import com.silvaniastudios.travellers.items.ItemWoodPlanks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotWood extends SlotItemHandler {

	public SlotWood(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.getItem() instanceof ItemWoodPlanks) {
			return true;
		}
		return false;
	}

}