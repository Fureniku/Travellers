package com.silvaniastudios.travellers.blocks.tileentity;

import com.silvaniastudios.travellers.items.ItemScrapMetal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMetal extends SlotItemHandler {

	public SlotMetal(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.getItem() instanceof ItemScrapMetal) {
			return true;
		}
		return false;
	}

}
