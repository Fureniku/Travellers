package com.silvaniastudios.travellers.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.databank.BlockDatabank;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Scanner line entity which renders the little black line between the scanner
 * tool and whatever was being scanned
 * 
 * @author jamesm2w
 */
public class EntityScannerLine extends Entity {
	/**
	 * Amount of ticks this entity has not been scanning a block which can be
	 * scanned
	 */
	int ticksNotInScannable;

	/**
	 * Initial UUID this class was given. Used in debugging.
	 */
	public UUID initialUUID;

	/**
	 * Whether this entity should be killed immediately, or should wait for NBT
	 * data first
	 */
	boolean deleteImmediately = false;

	/**
	 * Represents whether the scanner hit a block/entity, or otherwise
	 */
	public EnumAttachmentType attachmentType = EnumAttachmentType.NONE;

	/**
	 * Player which started scanning
	 */
	EntityPlayer player;

	/**
	 * 3D Vector of where the RayTrace hit the block/entity
	 */
	private Vec3d hitVec;

	/**
	 * BlockPos of the block which was targeted
	 */
	private BlockPos hitBlockPos;

	/**
	 * Entity which was targeted
	 */
	private Entity hitEntity;

	/**
	 * Mandatory constructor by Forge. THIS SHOULD NEVER BE USED.
	 * 
	 * @constructor
	 * @deprecated
	 * @param worldIn
	 */
	public EntityScannerLine(World worldIn) {
		super(worldIn);

		// if (worldIn.isRemote) {
		// this.player = Minecraft.getMinecraft().player;
		// this.init(player);
		// this.shoot();
		// }
		// System.out.println("scanner line created (via worldin) " +
		// this.entityUniqueID.toString());
		initialUUID = this.entityUniqueID;
		deleteImmediately = false;
		this.ignoreFrustumCheck = true;
	}

	public EntityScannerLine(World worldIn, UUID playerUUID) {
		super(worldIn);
		this.player = worldIn.getPlayerEntityByUUID(playerUUID);
		// System.out.println("scanner line created (via worldin, uuid) " +
		/// this.entityUniqueID.toString() + " UUID: " + playerUUID.toString());
		initialUUID = this.entityUniqueID;
		deleteImmediately = true;
	}

	/**
	 * Creates the new scanner line entity, and raytraces to its position
	 * 
	 * @constructor
	 * @param worldIn
	 *            World in which this scanner line was created
	 * @param player
	 *            Player which started scanning
	 */
	public EntityScannerLine(World worldIn, EntityPlayer player) {
		super(worldIn);
		// System.out.println("scanner line created (via worldin, player) " +
		// this.entityUniqueID.toString());
		// //System.out.println("player given to " +
		// this.entityUniqueID.toString() + " is " +
		// player.getUniqueID().toString());
		this.player = player;
		this.init(player);
		this.shoot();

		initialUUID = this.entityUniqueID;
		deleteImmediately = true;
	}

	/**
	 * Client-Side constructor of the entity - rarely seen this used in testing
	 * 
	 * @param worldIn
	 * @param x
	 * @param y
	 * @param z
	 */
	// @SideOnly(Side.CLIENT)
	// public EntityScannerLine(World worldIn, double x, double y, double z) {
	// super(worldIn);
	// // //System.out.println("scanner line created (via world, x,y,z) " +
	// this.entityUniqueID.toString());
	// player = Minecraft.getMinecraft().player;
	// this.init(player);
	// this.setPosition(x, y, z);
	// this.prevPosX = this.posX;
	// this.prevPosY = this.posY;
	// this.prevPosZ = this.posZ;
	// }

	/**
	 * Initialises some basic attributes and sizing.
	 * 
	 * @param player
	 *            Player which started the scanning
	 */
	public void init(EntityPlayer player) {
		this.setSize(0.25F, 0.25F);
		this.player = player;
		this.ticksNotInScannable = 0;
		this.ignoreFrustumCheck = true;
	}

	/**
	 * 
	 * @see net.minecraft.entity.Entity#isInRangeToRenderDist(double)
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	/**
	 * Raytraces for a block within 20 blocks. This result will be the position
	 * of the entity
	 */
	private void shoot() {
		float pitch = this.player.prevRotationPitch + (this.player.rotationPitch - this.player.prevRotationPitch);
		float yaw = this.player.prevRotationYaw + (this.player.rotationYaw - this.player.prevRotationYaw);

		// RayTraceResult res = player.rayTrace(20.0F, 0);
		RayTraceResult res = this.getLookedAt(0);
		//// System.out.println(this.getLookedAt(0).toString());

		//// System.out.println("Shooting scanner_line " +
		//// this.entityUniqueID.toString());
		// System.out.println(res.toString());

		String objectString = null;

		if (res.typeOfHit == RayTraceResult.Type.BLOCK) {

			this.attachmentType = EnumAttachmentType.BLOCK;
			this.setLocationAndAngles(res.hitVec.x, res.hitVec.y, res.hitVec.z, yaw, pitch);
			this.setHitBlockPos(res.getBlockPos());
			this.setHitVec(res.hitVec);

			Block block = world.getBlockState(res.getBlockPos()).getBlock();
			if (useItemStackBecauseOfSubBlocks(block)) {
				objectString = block
						.getPickBlock(world.getBlockState(res.getBlockPos()), null, world, res.getBlockPos(), player)
						.getUnlocalizedName() + ".name";
			} else {
				objectString = block.getUnlocalizedName() + ".name";
			}

		} else if (res.typeOfHit == RayTraceResult.Type.ENTITY) {

			this.attachmentType = EnumAttachmentType.ENTITY;
			this.setLocationAndAngles(res.hitVec.x, res.hitVec.y, res.hitVec.z, yaw, pitch);
			this.setHitVec(res.hitVec);
			this.setHitEntity(res.entityHit);

			objectString = "entity." + EntityList.getEntityString(res.entityHit) + ".name";

		} else {
			// System.out.println("scanner_line didnt hit a block so killing " +
			// this.entityUniqueID.toString());
			this.attachmentType = EnumAttachmentType.NONE;
			this.handleKill();
		}

		if (objectString != null) {
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			boolean success = playerData.scanObject(objectString);

			if (success) {
				int knowledgeBonus = Travellers.SCAN_REWARDS.findReward(objectString);

				if (knowledgeBonus != 0) {
					playerData.incrementKnowledgeBalance(knowledgeBonus);
					player.sendMessage(
							new TextComponentString(String.format("%sYou gained %s%d%s knowledge from scanning %s%s",
									TextFormatting.GOLD, TextFormatting.RESET, knowledgeBonus, TextFormatting.GOLD,
									TextFormatting.RESET, objectString)));
					
					PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP)player);
				}
			}

		}

	}

	/**
	 * Checks whether the entity should kill itself this tick, and updates the
	 * rotation to the player
	 * 
	 * @see net.minecraft.entity.Entity#onUpdate()
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		// //System.out.println(this.getUniqueID().toString() + " player " +
		// (this.player != null ? this.player.getUniqueID().toString() : "null")
		// + " IUUD: " + this.initialUUID.toString());

		if (this.player == null && this.deleteImmediately) {
			// //System.out.println("scanner_line not attached to a player so
			// killing " + this.entityUniqueID.toString());
			this.handleKill();
		} else if (this.player != null) {
			boolean shouldDestroy = this.shouldDestroy();
			if (!this.world.isRemote && !shouldDestroy) {

				BlockPos blockpos = new BlockPos(this);
				IBlockState iblockstate = this.world.getBlockState(blockpos);

				this.updateRotation();
				this.updatePosition();

				if (!(iblockstate.getBlock() instanceof BlockDatabank)) {
					++this.ticksNotInScannable;

					if (ticksNotInScannable >= 600) {
						this.handleKill();
					}
				}
			} else if (shouldDestroy) {
				this.handleKill();
			}
		}
	}

	/**
	 * @return boolean Whether this entity should kill itself.
	 */
	private boolean shouldDestroy() {
		ItemStack heldItemMainhand = this.player.getHeldItemMainhand();
		ItemStack heldItemOffhand = this.player.getHeldItemOffhand();
		boolean mainhandHolding = heldItemMainhand.getItem() instanceof ItemScanner;
		boolean offhandHolding = heldItemOffhand.getItem() instanceof ItemScanner;

		boolean notHoldingScanner = !mainhandHolding && !offhandHolding;

		boolean missed = this.attachmentType == EnumAttachmentType.NONE;

		boolean isConnected = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null).isScanning();

		// This checks if the game is running as a client and the entity is
		// attached to another player. if so, we don't have authority over when
		// this thing dies

		if (missed) {
			return true;
		}

		if (!world.isRemote || this.player == Minecraft.getMinecraft().player) {
			if (notHoldingScanner || missed || !isConnected || (this.getDistance(this.player) >= 10.0D)
					|| (this.player.isDead && !this.player.isEntityAlive())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * Copied from the fishing bobber, basically magic
	 */
	private void updateRotation() {
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f)
				* (180D / Math.PI)); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
	}

	private void updatePosition() {
		if (attachmentType == EnumAttachmentType.ENTITY && this.hitEntity != null) {

			double entityHeight = (double) this.hitEntity.height;

			this.posX = (this.hitEntity.getEntityBoundingBox().minX + this.hitEntity.getEntityBoundingBox().maxX) / 2;
			this.posY = this.hitEntity.getEntityBoundingBox().minY + entityHeight * 0.8D;
			this.posZ = (this.hitEntity.getEntityBoundingBox().minZ + this.hitEntity.getEntityBoundingBox().maxZ) / 2;

			this.setPosition(this.posX, this.posY, this.posZ);

		} else {
			return;
		}
	}

	/**
	 * Handles making this entity dead, and updating the player capabilities
	 */
	public void handleKill() {
		if (!this.world.isRemote && this.player != null) {

			// System.out.println("Killed scanner_line because it was marked
			// dead on server with player " + this.getCachedUniqueIdString());
			this.setDead();
			IPlayerData playerData = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			playerData.setScanning(null);

			PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) player);
		} else if (this.player == null) {
			this.setDead();
			// System.out.println("Killed scanner_line " +
			// this.getUniqueID().toString() + " because it has no parent player
			// on" + (this.world.isRemote ? "client" : "server") + " IUUID: " +
			// initialUUID.toString());
		} else {
			// System.out.println("Killed scanner_line which has player but is
			// marked dead on client " + this.getCachedUniqueIdString());
			this.setDead();
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

		boolean invalidNBTStructure = false;

		if (compound.hasUniqueId("player")) {
			this.player = this.world.getPlayerEntityByUUID(compound.getUniqueId("player"));
		} else {
			invalidNBTStructure = true;
		}

		if (compound.hasKey("attachmentType")) {
			this.attachmentType = EnumAttachmentType.fromValue(compound.getInteger("attachmentType"));
		} else {
			invalidNBTStructure = true;
		}

		if (compound.hasKey("hitVec")) {
			NBTTagCompound hitVecArray = compound.getCompoundTag("hitVec");
			this.hitVec = new Vec3d(hitVecArray.getDouble("x"), hitVecArray.getDouble("y"), hitVecArray.getDouble("z"));
		} else {
			invalidNBTStructure = true;
		}

		if (this.attachmentType == EnumAttachmentType.BLOCK) {

			if (compound.hasKey("hitPos")) {
				int[] hitPos = compound.getIntArray("hitPos");
				this.hitBlockPos = new BlockPos(hitPos[0], hitPos[1], hitPos[2]);
			} else {
				invalidNBTStructure = true;
			}

		} else if (this.attachmentType == EnumAttachmentType.ENTITY) {

			if (compound.hasUniqueId("hitEntity")) {
				world.getLoadedEntityList().forEach((entity) -> {
					if (entity.getUniqueID().compareTo(compound.getUniqueId("hitEntity")) == 0) {
						this.hitEntity = entity;
					}
				});

				if (this.hitEntity == null) {
					invalidNBTStructure = true;
				}
			} else {
				invalidNBTStructure = true;
			}

		} else {
			invalidNBTStructure = true;
		}

		this.deleteImmediately = true;

		if (invalidNBTStructure) {
			this.handleKill();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		if (this.player != null) {
			compound.setUniqueId("player", this.player.getUniqueID());
		}

		compound.setInteger("attachmentType", this.attachmentType.getValue());

		if (this.hitVec != null) {
			NBTTagCompound hitVecArray = new NBTTagCompound();
			hitVecArray.setDouble("x", hitVec.x);
			hitVecArray.setDouble("y", hitVec.y);
			hitVecArray.setDouble("z", hitVec.z);

			compound.setTag("hitVec", hitVecArray);
		}

		if (this.attachmentType == EnumAttachmentType.BLOCK) {

			if (this.hitBlockPos != null) {
				compound.setIntArray("hitPos",
						new int[] { hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ() });
			}

		} else if (this.attachmentType == EnumAttachmentType.ENTITY) {

			if (this.hitEntity != null) {
				compound.setUniqueId("hitEntity", this.hitEntity.getUniqueID());
			}

		}

	}

	@Override
	protected void entityInit() {
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;

		this.deleteImmediately = true;
	}

	/**
	 * @return the hitBlockPos
	 */
	public BlockPos getHitBlockPos() {
		return hitBlockPos;
	}

	/**
	 * @param hitBlockPos
	 *            the hitBlockPos to set
	 */
	public void setHitBlockPos(BlockPos hitBlockPos) {
		this.hitBlockPos = hitBlockPos;
	}

	/**
	 * @return the hitEntity
	 */
	public Entity getHitEntity() {
		return this.hitEntity;
	}

	/**
	 * @param hitEntity
	 *            the hitEntity to set
	 */
	public void setHitEntity(Entity hitEntity) {
		this.hitEntity = hitEntity;
	}

	/**
	 * @return the hitVec
	 */
	public Vec3d getHitVec() {
		return hitVec;
	}

	/**
	 * @param hitVec
	 *            the hitVec to set
	 */
	public void setHitVec(Vec3d hitVec) {
		this.hitVec = hitVec;
	}

	public RayTraceResult getLookedAt(float partialTicks) {
		Entity player = this.player;

		Entity pointedEntity;

		RayTraceResult objectMouseOver = null;

		if (player != null) {
			if (this.world != null) {

				double reachDistance = (double) 10.0F;
				objectMouseOver = player.rayTrace(reachDistance, partialTicks);
				Vec3d playerEyeVector = player.getPositionEyes(partialTicks);
				boolean reachOver3 = false;

				double d1 = reachDistance;

				if (reachDistance > 10.0D) {
					reachOver3 = true;
				}
				if (objectMouseOver != null) {
					d1 = objectMouseOver.hitVec.distanceTo(playerEyeVector);
				}

				Vec3d playerLook = player.getLook(1.0F);
				Vec3d playerLookExtended = playerEyeVector.addVector(playerLook.x * reachDistance,
						playerLook.y * reachDistance, playerLook.z * reachDistance);
				pointedEntity = null;
				Vec3d vec3d3 = null;

				List<Entity> closeEntityList = world.getEntitiesInAABBexcluding(player,
						player.getEntityBoundingBox()
								.expand(playerLook.x * reachDistance, playerLook.y * reachDistance,
										playerLook.z * reachDistance)
								.grow(1.0D, 1.0D, 1.0D),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
							public boolean apply(@Nullable Entity entity) {
								return entity != null && entity.canBeCollidedWith();
							}
						}));
				double d2 = d1;

				for (int j = 0; j < closeEntityList.size(); ++j) {
					Entity selectedEntity = closeEntityList.get(j);
					AxisAlignedBB entityBoundingBox = selectedEntity.getEntityBoundingBox()
							.grow((double) selectedEntity.getCollisionBorderSize());
					RayTraceResult playerEyeLookIntercept = entityBoundingBox.calculateIntercept(playerEyeVector,
							playerLookExtended);

					if (entityBoundingBox.contains(playerEyeVector)) {
						if (d2 >= 0.0D) {
							pointedEntity = selectedEntity;
							vec3d3 = (playerEyeLookIntercept == null) ? playerEyeVector : playerEyeLookIntercept.hitVec;
							d2 = 0.0D;
						}
					} else if (playerEyeLookIntercept != null) {
						double d3 = playerEyeVector.distanceTo(playerEyeLookIntercept.hitVec);

						if (d3 < d2 || d2 == 0.0D) {
							if (selectedEntity.getLowestRidingEntity() == player.getLowestRidingEntity()
									&& !selectedEntity.canRiderInteract()) {
								if (d2 == 0.0D) {
									pointedEntity = selectedEntity;
									vec3d3 = playerEyeLookIntercept.hitVec;
								}
							} else {
								pointedEntity = selectedEntity;
								vec3d3 = playerEyeLookIntercept.hitVec;
								d2 = d3;
							}
						}
					}
				}

				if (pointedEntity != null && reachOver3 && playerEyeVector.distanceTo(vec3d3) > 3.0D) {
					pointedEntity = null;
					objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null,
							new BlockPos(vec3d3));
				}

				if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
					objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
				}
			}
		}

		return objectMouseOver;
	}

	private boolean useItemStackBecauseOfSubBlocks(Block block) {
		String name = block.getUnlocalizedName();

		String[] haveSubBlocks = new String[] { "tile.doublePlant", "tile.leaves", "tile.stainedGlass",
				"tile.thinStainedGlass", "tile.cloth", "tile.log", "tile.sandStone", "tile.redSandStone", "tile.dirt",
				"tile.stonebricksmooth", "tile.flower1", "tile.flower2", "tile.clayHardenedStained", "tile.wood",
				"tile.stone", "tile.sand", "tile.sponge", "tile.skull" };

		for (String entry : haveSubBlocks) {
			if (name.contentEquals(entry)) {
				return true;
			}
		}

		return false;
	}

	public static enum EnumAttachmentType {
		ENTITY(2), BLOCK(1), NONE(0);

		private int value;

		private EnumAttachmentType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static EnumAttachmentType fromValue(int value) {
			switch (value) {
			case 2:
				return EnumAttachmentType.ENTITY;
			case 1:
				return EnumAttachmentType.BLOCK;
			default:
				return EnumAttachmentType.NONE;
			}
		}
	}
}