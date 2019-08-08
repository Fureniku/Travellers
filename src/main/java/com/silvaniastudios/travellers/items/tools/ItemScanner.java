package com.silvaniastudios.travellers.items.tools;

import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.ItemBasic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemScanner extends ItemBasic implements ITravellerTool {

	public ItemScanner(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldStack = playerIn.getHeldItem(handIn);
		
		if (!worldIn.isRemote)
        {
            EntityScannerLine scannerline = new EntityScannerLine(worldIn, playerIn);
            scannerline.setPlayer(playerIn);

            worldIn.spawnEntity(scannerline);
            playerIn.swingArm(handIn);
        } else {
        	EntityScannerLine scannerline = new EntityScannerLine(worldIn, playerIn);
            scannerline.setPlayer(playerIn);

            worldIn.spawnEntity(scannerline);
            playerIn.swingArm(handIn);
        }
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldStack);
	}

}
