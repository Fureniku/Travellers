package com.silvaniastudios.travellers.blocks.databank;

import java.util.Random;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockBasic;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDatabank extends BlockBasic implements ITileEntityProvider {

	public static final PropertyEnum<DatabankPartEnum> PART = PropertyEnum.<DatabankPartEnum> create("part",
			DatabankPartEnum.class);

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	protected static final AxisAlignedBB DATABANK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

	protected static final AxisAlignedBB DATABANK_INVERSED_AABB = new AxisAlignedBB(0.0D, -1.0D, 0.0D, 1.0D, 1.0D,
			1.0D);

	private DatabankRarityEnum rarity;

	public BlockDatabank(String name, DatabankRarityEnum rarity) {
		super(name, Material.IRON);

		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(Travellers.tabTravellers);
		this.rarity = rarity;
		this.hasTileEntity = true;

		ModBlocks.block_databanks.add(this);
		ModBlocks.item_databanks.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));

		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, DatabankPartEnum.LOWER));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);

		if (playerIn.getHeldItem(hand).getItem() instanceof ItemScanner) {

			if (playerData.getScanningEntity() != null) {
				playerData.getScanningEntity().handleKill();
				playerIn.swingArm(hand);
			} else {

				if (!worldIn.isRemote) {
					EntityScannerLine scanner = new EntityScannerLine(worldIn, playerIn);
					worldIn.spawnEntity(scanner);
					playerData.setScanning(scanner);

					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);

				}
				
				EntityScannerLine scanner = new EntityScannerLine(worldIn, playerIn);
				worldIn.spawnEntity(scanner);

				playerIn.swingArm(hand);
			}

		}

		if (worldIn.isRemote) { // If client
			return true;

		} else if (playerIn.isSpectator()) { // If player is spectator
			return true;

		} else if (playerIn.getHeldItem(hand).getItem() == ModItems.scanner) {
			// If player holds a scanner item

			if (state.getValue(PART) != DatabankPartEnum.LOWER) {
				// If not the lowerpart of a databank (i.e. upper)

				pos = pos.offset(EnumFacing.DOWN);
				// Move the pos down one block to the LOWER part
				state = worldIn.getBlockState(pos);

				if (state.getBlock() != this) {
					// if the block is not a databank
					return true;
				}

			}

			TileEntity dataBankTileEntity = worldIn.getTileEntity(pos);

			if (dataBankTileEntity instanceof TileEntityDatabank) {

				if (((TileEntityDatabank) dataBankTileEntity).isScannedBy(playerIn)) {
					playerIn.sendMessage(new TextComponentString("§eYou have already scanned this databank.§r"));

					return true;
				} else {
					boolean successfulScan = ((TileEntityDatabank) dataBankTileEntity).beScannedBy(playerIn);

					if (!successfulScan) {
						playerIn.sendMessage(new TextComponentString("§eYou have already scanned this databank.§r"));

						return true;
					}

					playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);
					playerData.incrementKnowledgeBalance(rarity.getKnowledgeBoost());

					String rarityString = String.format("%s%s", DatabankRarityEnum.color(rarity),
							rarity.toString().toLowerCase());
					String msg = String.format("§eScanned %s§e databank for %d knowledge§r", rarityString,
							rarity.getKnowledgeBoost());

					playerIn.sendMessage(new TextComponentString(msg));

					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);

					return true;
				}

			}

		}
		return false;

	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

		if (state.getValue(PART) != DatabankPartEnum.LOWER) {
			// i.e. we're the upper part
			if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() != this) {
				// look below
				worldIn.setBlockToAir(pos);
				// if the upper part has no lower part below destroy itself
			}
		} else if (worldIn.getBlockState(pos.offset(EnumFacing.UP)).getBlock() != this) {
			// or look up
			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
				// if the lower part has nothing above it, drop itself
			}
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == DatabankPartEnum.UPPER ? Items.AIR : Item.getItemFromBlock(this);
	} // lower block drops the item

	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (state.getValue(PART) == DatabankPartEnum.LOWER) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityDatabank) {
				ItemStack itemstack = new ItemStack(getItemDropped(state, new Random(), 0));
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound.setTag("BlockEntityTag", ((TileEntityDatabank) tileentity).writeToNBT(nbttagcompound1));
				itemstack.setTagCompound(nbttagcompound);

				spawnAsEntity(worldIn, pos, itemstack);
			}
		}
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos blockpos = pos;

		if (state.getValue(PART) == DatabankPartEnum.UPPER) {
			blockpos = pos.offset(EnumFacing.DOWN); // if we're upper, look
													// below for the tileentity
		}

		TileEntity tileentity = worldIn.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityDatabank) {
			ItemStack itemstack = new ItemStack(getItemDropped(state, new Random(), 0));
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound.setTag("BlockEntityTag", ((TileEntityDatabank) tileentity).writeToNBT(nbttagcompound1));
			itemstack.setTagCompound(nbttagcompound);

			return itemstack;
		}

		return new ItemStack(Item.getItemFromBlock(this), 1);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {

		if (player.capabilities.isCreativeMode && state.getValue(PART) == DatabankPartEnum.LOWER) {
			BlockPos blockpos = pos.offset(EnumFacing.UP);

			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {

		if (state.getValue(PART) == DatabankPartEnum.LOWER && te instanceof TileEntityDatabank) {

			ItemStack itemstack = new ItemStack(getItemDropped(state, new Random(), 0));
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound.setTag("BlockEntityTag", ((TileEntityDatabank) te).writeToNBT(nbttagcompound1));
			itemstack.setTagCompound(nbttagcompound);

			worldIn.setBlockToAir(pos.offset(EnumFacing.UP));

			spawnAsEntity(worldIn, pos, itemstack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, (TileEntity) null, stack);
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return (state.getValue(PART) == DatabankPartEnum.LOWER) ? DATABANK_AABB : DATABANK_INVERSED_AABB;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}

	public EnumPushReaction getMobilityFlag(IBlockState state) {
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

	/*
	 * Tile Entity Methods
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return (state.getValue(PART) == DatabankPartEnum.LOWER) ? new TileEntityDatabank() : null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return ((meta & 8) > 0) ? new TileEntityDatabank() : null;
	}

	/*
	 * Blockstate Methods
	 */

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PART, FACING });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		BlockPos blockAbove = pos.offset(EnumFacing.UP);
		BlockPos blockBelow = pos.offset(EnumFacing.DOWN);

		if (world.getBlockState(blockAbove).getMaterial() == Material.AIR) {

			world.setBlockState(blockAbove, this.getDefaultState().withProperty(PART, DatabankPartEnum.UPPER)
					.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));

			return this.getDefaultState().withProperty(PART, DatabankPartEnum.LOWER).withProperty(FACING,
					placer.getHorizontalFacing().getOpposite());

		} else if (world.getBlockState(blockBelow).getMaterial() == Material.AIR) {

			world.setBlockState(blockBelow, this.getDefaultState().withProperty(PART, DatabankPartEnum.LOWER)
					.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));

			return this.getDefaultState().withProperty(PART, DatabankPartEnum.UPPER).withProperty(FACING,
					placer.getHorizontalFacing().getOpposite());
		}

		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

		if (state.getValue(PART) == DatabankPartEnum.LOWER) {
			i = i | 8;
		}

		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int i = (meta >= 8) ? meta - 8 : meta;
		EnumFacing enumfacing = EnumFacing.getHorizontal(i);

		IBlockState returnValue = this.getDefaultState().withProperty(FACING, enumfacing);

		if ((meta & 8) > 0) {
			returnValue = returnValue.withProperty(PART, DatabankPartEnum.LOWER);
		} else {
			returnValue = returnValue.withProperty(PART, DatabankPartEnum.UPPER);
		}

		return returnValue;
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}
}
