package com.silvaniastudios.travellers.blocks;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;
import com.silvaniastudios.travellers.network.ScannerLineSyncMessage;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Basic class which overrides onBlockActivated to allow scanner lines to be spawned when the block is clicked
 * with the scanner
 * 
 * @author jamesm2w
 */
public class BlockScannable extends BlockBasic {

	public BlockScannable(String name, Material mat) {
		super(name, mat);
	}
	
	/**
	 * Handles spawning the scanner line, similar to ItemScanner
	 * 
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand, net.minecraft.util.EnumFacing, float, float, float)
	 */
	
	//TODO: Fix this on the server-side so it doesn't crash
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemScanner) {

			IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);

			if (!worldIn.isRemote) { //if server
				
				if (playerData.getScanningEntity() != null) {
					//System.out.println("scanner_line being used to scan so killing " + playerData.getScanningEntity().getUniqueID().toString());
					playerData.getScanningEntity().handleKill();
					
					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);
					
					playerIn.swingArm(hand);
				} else {
					EntityScannerLine scanner = new EntityScannerLine(worldIn, playerIn);
					
					playerData.setScanning(scanner);
					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);
					
					worldIn.spawnEntity(scanner);
					PacketHandler.INSTANCE.sendToAllTracking(new ScannerLineSyncMessage(scanner), scanner);
				}
			}
			playerIn.swingArm(hand);

		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

}
