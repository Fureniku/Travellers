package com.silvaniastudios.travellers.blocks.databank;

import java.util.Random;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockBasic;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDatabank extends BlockBasic implements ITileEntityProvider {

	public static final PropertyEnum<DatabankPartEnum> PART = PropertyEnum.<DatabankPartEnum> create("part",
			DatabankPartEnum.class);

	protected DatabankRarityEnum rarity;

	public BlockDatabank(String name, DatabankRarityEnum rarity) {
		super(name, Material.IRON);
		this.setHarvestLevel("pickaxe", 2);

		this.rarity = rarity;

		ModBlocks.block_databanks.add(this);
		ModBlocks.item_databanks.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));

		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, DatabankPartEnum.LOWER));
	}
	
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
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
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/*
	 * Register method
	 */
	public void registerModels() {
		Travellers.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, this.name);
	}

	/*
	 * Tile Entity Methods
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityDatabank();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDatabank();
	}

	/*
	 * Blockstate Methods
	 */

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PART });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		BlockPos blockAbove = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
		BlockPos blockBelow = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

		if (world.getBlockState(blockAbove).getMaterial() == Material.AIR) {
			world.setBlockState(blockAbove, this.getDefaultState().withProperty(PART, DatabankPartEnum.UPPER));

			return this.getDefaultState().withProperty(PART, DatabankPartEnum.LOWER);
		} else if (world.getBlockState(blockBelow).getMaterial() == Material.AIR) {
			world.setBlockState(blockBelow, this.getDefaultState().withProperty(PART, DatabankPartEnum.LOWER));

			return this.getDefaultState().withProperty(PART, DatabankPartEnum.UPPER);
		}

		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PART) == DatabankPartEnum.LOWER ? 0 : 1;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState returnValue = this.getDefaultState();
		if (meta == 0) {
			return returnValue.withProperty(PART, DatabankPartEnum.LOWER);
		} else {
			return returnValue.withProperty(PART, DatabankPartEnum.UPPER);
		}
	}

	/*
	 * Drop Behaviour
	 */
	
	@SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == DatabankPartEnum.LOWER ? Items.AIR : Item.getItemFromBlock(this);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {

		TileEntity tileentity = te;

		if (tileentity instanceof TileEntityDatabank) {

			ItemStack itemstack = new ItemStack(getItemDropped(state, new Random(), 0));
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound.setTag("BlockEntityTag", ((TileEntityDatabank) tileentity).writeToNBT(nbttagcompound1));
			itemstack.setTagCompound(nbttagcompound);

			spawnAsEntity(worldIn, pos, itemstack);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		
		if (state.getValue(PART) != DatabankPartEnum.LOWER) {
			if (worldIn.getBlockState(pos.offset(EnumFacing.UP)).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		} else if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() != this) {
			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}
		} 
	}

	/*
	 * Block Interactions
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (worldIn.isRemote) {
			return true;
		} else if (playerIn.isSpectator()) {
			return true;
		} else if (playerIn.getHeldItem(hand).getItem() == ModItems.scanner) {
			TileEntity dataBankTileEntity = worldIn.getTileEntity(pos);

			if (dataBankTileEntity instanceof TileEntityDatabank) {

				if (((TileEntityDatabank) dataBankTileEntity).isScannedBy(playerIn)) {
					playerIn.sendMessage(new TextComponentString("�eYou have already scanned this databank.�r"));

					return true;
				} else {
					boolean successfulScan = ((TileEntityDatabank) dataBankTileEntity).beScannedBy(playerIn);

					if (!successfulScan) {
						playerIn.sendMessage(new TextComponentString("�eYou have already scanned this databank.�r"));

						return true;
					}

					IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);
					playerData.incrementKnowledgeBalance(rarity.getKnowledgeBoost());

					String rarityString = String.format("%s%s", DatabankRarityEnum.color(rarity),
							rarity.toString().toLowerCase());
					String msg = String.format("�eScanned %s�e databank for %d knowledge�r", rarityString,
							rarity.getKnowledgeBoost());

					playerIn.sendMessage(new TextComponentString(msg));

					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData),
							(EntityPlayerMP) playerIn);

					return true;
				}

			}

		}
		return false;

	}



}
