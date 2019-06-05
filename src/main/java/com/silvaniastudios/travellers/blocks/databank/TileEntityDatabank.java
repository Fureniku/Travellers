package com.silvaniastudios.travellers.blocks.databank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class TileEntityDatabank extends TileEntity {

	private List<UUID> scannedBy = new ArrayList<UUID>(); // Array of Entity IDs which scanned the databank
	
	protected DatabankPartEnum part;
	
	public TileEntityDatabank() {
	}

	public boolean beScannedBy (EntityPlayer player) {
		this.scannedBy.add(player.getUniqueID());
		markDirty();
		this.part = this.world.getBlockState(this.pos).getValue(BlockDatabank.PART);
		
		if (this.part == DatabankPartEnum.UPPER) {
			BlockPos blockBelow = new BlockPos(this.getPos().getX(), this.getPos().getY() - 1, this.getPos().getZ());
			TileEntity tileEntityBelow = this.getWorld().getTileEntity(blockBelow);
			
			if (tileEntityBelow instanceof TileEntityDatabank) {
				if (((TileEntityDatabank) tileEntityBelow).isScannedBy(player)) {
					return false;
				} else {
					((TileEntityDatabank) tileEntityBelow).beScannedBy(player);
					return true;
				}
			}
		} else if (this.part == DatabankPartEnum.LOWER) {
			BlockPos blockAbove = new BlockPos(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ());
			TileEntity tileEntityAbove = this.getWorld().getTileEntity(blockAbove);
			
			if (tileEntityAbove instanceof TileEntityDatabank) {
				if ( ((TileEntityDatabank) tileEntityAbove).isScannedBy(player)) {
					return false;
				} else {
					((TileEntityDatabank) tileEntityAbove).beScannedBy(player);
					return true;
				}
			}			
		}
		
		return false;
	}
	
	public boolean isScannedBy (EntityPlayer player) {
		return this.scannedBy.contains(player.getUniqueID());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		NBTTagList tagList = compound.getTagList("scannedBy", Constants.NBT.TAG_COMPOUND);
		// get that scanned by list from the tag
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i); // iterate through parsing the entityid out
			String entityId = tag.getString(String.format("EntityId%d", i));
			scannedBy.add(i, UUID.fromString(entityId));
		}
		
		super.readFromNBT(compound); //move on with your life!
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < scannedBy.size(); i++) { // Iterate through the scannedBy list
			String stringID = scannedBy.get(i).toString();
			if (stringID != null) {
				NBTTagCompound tag = new NBTTagCompound(); // For each append a new NBT tag string to the NBT tag list
				tag.setString(String.format("EntityId%d", i), stringID); // give it the unique key "EntityId1", etc.
				
				tagList.appendTag(tag); 
			}
		}
		
		compound.setTag("scannedBy", tagList); // Finally add that list to the main compound
		
		return super.writeToNBT(compound); // Move on with life
	}

}
