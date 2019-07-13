package com.silvaniastudios.travellers.blocks.tileentity.assembler;

import javax.annotation.Nonnull;

import com.silvaniastudios.travellers.blocks.BlockTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AssemblerEntity extends BlockTileEntity implements ITickable, ICapabilityProvider {
	
	public int buildTime = 0;
	public int slot1 = 0;
	public int slot2 = 0;
	public int slot3 = 0;
	public int slot4 = 0;

	public AssemblerEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(5) {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			AssemblerEntity.this.markDirty();
		}
	};
	
	public Container createContainer(EntityPlayer player) {
		return new AssemblerContainer(player.inventory, this);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		
		buildTime = nbt.getInteger("build_time");
		slot1 = nbt.getInteger("slot1");
		slot2 = nbt.getInteger("slot2");
		slot3 = nbt.getInteger("slot3");
		slot4 = nbt.getInteger("slot4");
	}
	
	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setTag("items", inventory.serializeNBT());
		
		nbt.setInteger("build_time", buildTime);
		nbt.setInteger("slot1", slot1);
		nbt.setInteger("slot2", slot2);
		nbt.setInteger("slot3", slot3);
		nbt.setInteger("slot4", slot4);
		return nbt;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return writeNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readNBT(nbt);
	}
}
