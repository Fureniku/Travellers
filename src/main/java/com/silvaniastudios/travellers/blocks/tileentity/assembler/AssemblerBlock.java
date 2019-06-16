package com.silvaniastudios.travellers.blocks.tileentity.assembler;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockBasic;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockCore.EnumRotate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class AssemblerBlock extends BlockBasic {
	
	public static final PropertyEnum<EnumRotate> ROTATE_ID = PropertyEnum.create("rotate", EnumRotate.class);

	public AssemblerBlock(String name) {
		super(name, Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATE_ID, EnumRotate.n));
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
   	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
   	
	@Override
   	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {ROTATE_ID});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ROTATE_ID, EnumRotate.byMetadata(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumRotate)state.getValue(ROTATE_ID)).getMetadata();
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.assembling_station, 1);
    }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new AssemblerEntity();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote) {
			if (te != null && te instanceof AssemblerEntity) {
				player.openGui(Travellers.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
}
