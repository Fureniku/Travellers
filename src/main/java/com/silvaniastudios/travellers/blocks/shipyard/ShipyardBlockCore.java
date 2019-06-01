package com.silvaniastudios.travellers.blocks.shipyard;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockBasic;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class ShipyardBlockCore extends BlockBasic {

	public ShipyardBlockCore(String name) {
		super(name, Material.IRON);
		this.setCreativeTab(Travellers.tabTravellers);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
