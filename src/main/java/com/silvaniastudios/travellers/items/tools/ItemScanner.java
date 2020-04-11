package com.silvaniastudios.travellers.items.tools;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.entity.EntityScannerLine.EnumAttachmentType;
import com.silvaniastudios.travellers.items.ItemBasic;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;
import com.silvaniastudios.travellers.network.ScannerLineSyncMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gauntlet Scanner Tool
 * 
 * @author jamesm2w
 */
public class ItemScanner extends ItemBasic implements ITravellerTool {

	public ItemScanner(String name) {
		super(name);
	}

	/**
	 * Handles spawning the scanner line entity when the item is right clicked
	 * 
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand)
	 */
	//TODO: Fix this on the server-side so people can see it and not crash the game
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);

		if (!worldIn.isRemote) { //if server
			
			//System.out.println(playerData.getScanningEntity().getPlayer());
			
			if (playerData.getScanningEntity() != null || (playerData.getScanningEntity() != null && playerData.getScanningEntity().getPlayer() == null)) {
				//System.out.println("scanner_line being used to scan so killing " + playerData.getScanningEntity().getUniqueID().toString());
				playerData.getScanningEntity().handleKill();
				playerData.setScanning(null);
				PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);
				
				playerIn.swingArm(handIn);
			} else {
				EntityScannerLine scanner = new EntityScannerLine(worldIn, playerIn);
				
				if (scanner.attachmentType != EnumAttachmentType.NONE) {
					playerData.setScanning(scanner);
					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);
					
					worldIn.spawnEntity(scanner);
					PacketHandler.INSTANCE.sendToAllTracking(new ScannerLineSyncMessage(scanner), scanner);
				} else {
					scanner.handleKill();
				}
				
				
			}
		}
		playerIn.swingArm(handIn);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

}
