package com.silvaniastudios.travellers.blocks.databank;

import java.util.List;
import java.util.Random;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockScannable;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Databank block, extends BlockScannable to provide scanner line functionality,
 * has a tileentity which stores the data about which players have scanned it
 * Drop functionality is a little weird, but should work.
 * 
 * @author james_pntzyfo
 *
 */
public class BlockDatabank extends BlockScannable implements ITileEntityProvider {

	/**
	 * Represents whether this is a top half or bottom half databank
	 */
	public static final PropertyEnum<DatabankPartEnum> PART = PropertyEnum.<DatabankPartEnum> create("part",
			DatabankPartEnum.class);

	/**
	 * Represents which direction the databank is facing in
	 */
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	/**
	 * This is the extended AABB which covers two blocks vertically, starting
	 * from the bottom block
	 */
	protected static final AxisAlignedBB DATABANK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

	/**
	 * This is an extended AABB covering two blocks vertically, starting from
	 * the top block
	 */
	protected static final AxisAlignedBB DATABANK_INVERSED_AABB = new AxisAlignedBB(0.0D, -1.0D, 0.0D, 1.0D, 1.0D,
			1.0D);

	/**
	 * Represents what rarity this databank is and thus how much knowledge it
	 * should give the player once scanned
	 */
	private DatabankRarityEnum rarity;

	/**
	 * Creates new Databank block with default state lower part, and sets it to
	 * the travellers creative tab
	 * 
	 * @constructor
	 * @param name
	 *            Unlocalised name of the databank block
	 * @param rarity
	 *            Rarity of the databank block
	 */
	public BlockDatabank(String name, DatabankRarityEnum rarity) {
		super(name, Material.IRON);

		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(Travellers.tabTravellers);
		this.rarity = rarity;
		this.hasTileEntity = true;

		ModBlocks.block_databanks.add(this);
		ModBlocks.item_databanks.add(new ItemDatabankBlock(this, rarity.getKnowledgeBoost()).setRegistryName(this.getRegistryName()));

		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, DatabankPartEnum.LOWER));
	}
	
	public static class ItemDatabankBlock extends ItemBlock {
		
		private int knowledgeGranted;
		
		public ItemDatabankBlock(Block block, int knowledge) {
			super(block);
			
			knowledgeGranted = knowledge;
		}
		
		@Override
		public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			tooltip.add("Grants " + String.valueOf(knowledgeGranted) + " knowledge");
			super.addInformation(stack, worldIn, tooltip, flagIn);
		}
		
	}

	/**
	 * Runs when the databank block is activated (clicked on), calls super for
	 * scanner line spawning, performs checks and adds knowledge to players
	 * account
	 * 
	 * @see com.silvaniastudios.travellers.blocks.BlockScannable#onBlockActivated(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState,
	 *      net.minecraft.entity.player.EntityPlayer,
	 *      net.minecraft.util.EnumHand, net.minecraft.util.EnumFacing, float,
	 *      float, float)
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		// Does the scanner_line spawning
		boolean result = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		
		if (!result) {
			return result;
		}
		
		IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);

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
					playerIn.sendMessage(new TextComponentString(
							TextFormatting.YELLOW + "You have already scanned this databank" + TextFormatting.RESET));
					return true;
				} else {
					boolean successfulScan = ((TileEntityDatabank) dataBankTileEntity).beScannedBy(playerIn);

					if (!successfulScan) {
						playerIn.sendMessage(new TextComponentString(TextFormatting.YELLOW
								+ "You have already scanned this databank" + TextFormatting.RESET));
						return true;
					}

					playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);
					playerData.incrementKnowledgeBalance(rarity.getKnowledgeBoost());

					String rarityString = String.format("%s%s", DatabankRarityEnum.color(rarity),
							rarity.toString().toLowerCase());
					String msg = String.format(TextFormatting.YELLOW + "Scanned %s" + TextFormatting.YELLOW
							+ " databank for %d knowledge" + TextFormatting.RESET, rarityString,
							rarity.getKnowledgeBoost());

					playerIn.sendMessage(new TextComponentString(msg));

					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);

					return true;
				}

			}

		}
		return false;

	}

	/**
	 * Runs when the neighbour block has changed. This is the way it detects
	 * whether to destroy itself because it's missing the other half
	 * 
	 * @see net.minecraft.block.Block#neighborChanged(net.minecraft.block.state.IBlockState,
	 *      net.minecraft.world.World, net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.Block, net.minecraft.util.math.BlockPos)
	 */
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

	/**
	 * Only the lower part drops the item, the upper drops air, to stop databank
	 * duping.
	 * 
	 * @see net.minecraft.block.Block#getItemDropped(net.minecraft.block.state.IBlockState,
	 *      java.util.Random, int)
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return /*state.getValue(PART) == DatabankPartEnum.UPPER ? Items.AIR :*/ Item.getItemFromBlock(this);
	}

	/**
	 * This is responsible for adding the TileEntity data as NBT onto the item
	 * stack
	 * 
	 * @see net.minecraft.block.Block#dropBlockAsItemWithChance(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState, float, int)
	 */
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

	/**
	 * Returns an item stack with the correct nbt data from the attached tile
	 * entity
	 * 
	 * @see net.minecraft.block.Block#getItem(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState)
	 */
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

	/**
	 * Fixes an issue with dropping in creative mode
	 * 
	 * @see net.minecraft.block.Block#onBlockHarvested(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState,
	 *      net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {

		if (player.capabilities.isCreativeMode && state.getValue(PART) == DatabankPartEnum.LOWER) {
			BlockPos blockpos = pos.offset(EnumFacing.UP);

			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}
	}

	/**
	 * Handles harvesting the block and spawning the item stack with the correct
	 * NBT data
	 * 
	 * @see net.minecraft.block.Block#harvestBlock(net.minecraft.world.World,
	 *      net.minecraft.entity.player.EntityPlayer,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState,
	 *      net.minecraft.tileentity.TileEntity, net.minecraft.item.ItemStack)
	 */
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

	/**
	 * Removes block and tile entity
	 * 
	 * @see net.minecraft.block.Block#breakBlock(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos,
	 *      net.minecraft.block.state.IBlockState)
	 */
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	/**
	 * Returns the correct AABB depending on the part
	 * 
	 * @see net.minecraft.block.Block#getBoundingBox(net.minecraft.block.state.IBlockState,
	 *      net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos)
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return (state.getValue(PART) == DatabankPartEnum.LOWER) ? DATABANK_AABB : DATABANK_INVERSED_AABB;
	}

	/**
	 * Flag for telling whether this has a custom breaking process
	 * 
	 * @return true
	 * @see net.minecraft.block.Block#hasCustomBreakingProgress(net.minecraft.block.state.IBlockState)
	 */
	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}

	/**
	 * Makes pistons destroy databanks (this is because of the tile entity)
	 * 
	 * @see net.minecraft.block.Block#getMobilityFlag(net.minecraft.block.state.IBlockState)
	 */
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	/**
	 * Fixes some lighting issues, and lets the game know there are transparent
	 * sides
	 * 
	 * @see net.minecraft.block.Block#doesSideBlockRendering(net.minecraft.block.state.IBlockState,
	 *      net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos,
	 *      net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	/**
	 * @return EnumBlockRenderType.MODEL
	 * @see net.minecraft.block.Block#getRenderType(net.minecraft.block.state.IBlockState)
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	/**
	 * @return BlockRenderLayer.CUTOUT
	 * @see net.minecraft.block.Block#getBlockLayer()
	 */
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/*
	 * Tile Entity Methods
	 */

	/**
	 * @return true
	 * @see net.minecraft.block.Block#hasTileEntity(net.minecraft.block.state.IBlockState)
	 */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	/**
	 * Only the lower part has the tile entity attached
	 * 
	 * @see net.minecraft.block.Block#createTileEntity(net.minecraft.world.World,
	 *      net.minecraft.block.state.IBlockState)
	 */
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return (state.getValue(PART) == DatabankPartEnum.LOWER) ? new TileEntityDatabank() : null;
	}

	/**
	 * Gives lower section the tile entity, with the block meta
	 * 
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World,
	 *      int)
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return ((meta & 8) > 0) ? new TileEntityDatabank() : null;
	}

	/*
	 * Blockstate Methods
	 */

	/**
	 * Gives block two states: PART and FACING
	 * 
	 * @see net.minecraft.block.Block#createBlockState()
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PART, FACING });
	}

	/**
	 * Handles placing the block with the right direction and and sets the
	 * opposite part to be the other part
	 * 
	 * @see net.minecraft.block.Block#getStateForPlacement(net.minecraft.world.World,
	 *      net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing,
	 *      float, float, float, int, net.minecraft.entity.EntityLivingBase,
	 *      net.minecraft.util.EnumHand)
	 */
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
		} else {
			world.setBlockToAir(pos);
			return this.getDefaultState();
		}

		//return null;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockDatabank) {
			
			DatabankPartEnum part = state.getValue(PART);
			
			boolean secondaryBlockIsDatabank = false;
			
			BlockPos secondaryBlock = pos;
			
			if (part == DatabankPartEnum.LOWER) {
				secondaryBlock = pos.up();
				secondaryBlockIsDatabank = worldIn.getBlockState(secondaryBlock).getBlock() instanceof BlockDatabank;
			} else if (part == DatabankPartEnum.UPPER) {
				secondaryBlock = pos.down();
				secondaryBlockIsDatabank = worldIn.getBlockState(secondaryBlock).getBlock() instanceof BlockDatabank;
			}
			
			if (!secondaryBlockIsDatabank) {
				
				if (part == DatabankPartEnum.LOWER) {
					worldIn.removeTileEntity(pos);
				} else if (part == DatabankPartEnum.UPPER) {
					worldIn.removeTileEntity(secondaryBlock);
				}
				
				worldIn.setBlockToAir(pos);
			}
			
		}
		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	/**
	 * Converts block state into meta. 0 | 0 | 0 | 0 ^ Part ^^^ Facing
	 * 
	 * @see net.minecraft.block.Block#getMetaFromState(net.minecraft.block.state.IBlockState)
	 */
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
