package com.silvaniastudios.travellers.items.tools;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.ItemBasic;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScanner extends ItemBasic implements ITravellerTool {

	public ItemScanner(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);
		System.out.println("Scanner item right clicked");
		
		if (playerData.getScanningEntity() != null) {
			
			playerData.getScanningEntity().handleKill();
			playerIn.swingArm(handIn);
		} else {

			if (!worldIn.isRemote) {
				EntityScannerLine scanner = new EntityScannerLine(worldIn, playerIn);
				
				playerData.setScanning(scanner);
				PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);
				
				worldIn.spawnEntity(scanner);
			}

			playerIn.swingArm(handIn);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

}
