package com.silvaniastudios.travellers.blocks.databank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

/**
 * Tile Entity of the Databank
 * 
 * @author jamesm2w
 */
public class TileEntityDatabank extends TileEntity {

	/**
	 * List of Entity UUIDs which have scanned this databank
	 */
	private List<UUID> scannedBy = new ArrayList<UUID>(); // Array of Entity IDs which scanned the databank
	
	/**
	 * Which part of the databank this tile enttiy represents (should be lower)
	 */
	protected DatabankPartEnum part;
	
	public TileEntityDatabank() {
	}

	/**
	 * Tries to scan the databank and add player to the scanned UUIDs, returns whether successful
	 * 
	 * @param player Player which is scanning the databank
	 * @return boolean whether successful scan
	 */
	public boolean beScannedBy (EntityPlayer player) {
		
		if (this.isScannedBy(player)) {
			return false;
		}
		
		this.scannedBy.add(player.getUniqueID());
		markDirty();
		this.part = this.world.getBlockState(this.pos).getValue(BlockDatabank.PART);
		
		return true;
	}
	
	/**
	 * Checks if scanned list contains this player's UUID
	 * 
	 * @param player player to test
	 * @return boolean whether the player has scanned this databank
	 */
	public boolean isScannedBy (EntityPlayer player) {
		return this.scannedBy.contains(player.getUniqueID());
	}

	/**
	 * Parses NBT data into the UUID list
	 * 
	 * @see net.minecraft.tileentity.TileEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
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

	/**
	 * Transforms the scanned list into NBT format
	 * 
	 * @see net.minecraft.tileentity.TileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
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
