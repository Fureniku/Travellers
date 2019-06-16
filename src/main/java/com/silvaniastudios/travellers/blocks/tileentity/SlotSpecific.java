package com.silvaniastudios.travellers.blocks.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSpecific extends SlotItemHandler {
	
	Item item;
	
	public SlotSpecific(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item item) {
		super(itemHandler, index, xPosition, yPosition);
		this.item = item;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == item;
	}
}