package com.silvaniastudios.travellers.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScrapMetal extends ItemBasic {

	public ItemScrapMetal(String name) {
		super(name);
		this.setMaxDamage(10);
	}
	
	public int getQuality(ItemStack stack) {
		return stack.getItemDamage();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 5));
        }
    }
	
	@SideOnly(Side.CLIENT)
    @Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Metal");
		tooltip.add("Quality: " + stack.getItemDamage());
		tooltip.add("Subname: " + name.substring(6));
		tooltip.add(I18n.format("travellers.tooltip.metal." + name.substring(6)));
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack item) {
		return false;
	}
}
