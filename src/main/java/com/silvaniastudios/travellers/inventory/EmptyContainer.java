package com.silvaniastudios.travellers.inventory;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class EmptyContainer extends ContainerPlayer {

	public EmptyContainer(InventoryPlayer inventory, boolean isLocalWorld, EntityPlayer player) {
		super(inventory, isLocalWorld, player);

		this.inventorySlots = new ArrayList<Slot>();
		addPlayerSlots(inventory);
	}

	public void addPlayerSlots(IInventory playerInventory) {
		// add inv slots

		int left = 294;
		int top = 174;

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int x = left + j * 18;
				int y = top + i * 18;
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
			}
		}

		left = 294;
		top = 232;

		// add hotbar slots
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInventory, i, left + i * 18, top));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
