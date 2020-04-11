package com.silvaniastudios.travellers.network;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.entity.EntityScannerLine.EnumAttachmentType;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author jamesm2w
 */
public class ScannerLineSyncMessage implements IMessage {

	/**
	 * Player which the scanner belongs to
	 */
	public UUID playerId;
	
	/**
	 * Entity which is being synced
	 */
	public UUID entityId;
	
	/**
	 * AttachmentType of the scanner
	 */
	@Nullable
	public EntityScannerLine.EnumAttachmentType attachmentType;
	
	/**
	 * Vector of the scanner ray trace hit
	 */
	@Nullable
	public Vec3d hitVec;
	
	/**
	 * Entity which the scanner is connected to
	 */
	@Nullable
	public UUID hitEntity;
	
	/**
	 * BlockPos which the scanner is scanning
	 */
	@Nullable
	public BlockPos hitBlockPos;

	public ScannerLineSyncMessage() {
	}

	public ScannerLineSyncMessage(EntityScannerLine entity) {
		playerId = entity.getPlayer().getUniqueID();
		entityId = entity.getUniqueID();
		
		attachmentType = entity.attachmentType;
		hitVec = entity.getHitVec();
		
		if (attachmentType == EntityScannerLine.EnumAttachmentType.BLOCK) {
			hitBlockPos = entity.getHitBlockPos();
			
		} else if (attachmentType == EntityScannerLine.EnumAttachmentType.ENTITY) {
			hitEntity = entity.getHitEntity().getUniqueID();
		}

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);

		playerId = tag.getUniqueId("player");
		entityId = tag.getUniqueId("entity");
		
		if (tag.hasKey("attachmentType")) {
			this.attachmentType = EnumAttachmentType.fromValue(tag.getInteger("attachmentType"));
		}
		
		if (tag.hasKey("hitVec")) {
			NBTTagCompound hitVecArray = tag.getCompoundTag("hitVec");
			this.hitVec = new Vec3d(hitVecArray.getDouble("x"), hitVecArray.getDouble("y"), hitVecArray.getDouble("z"));
		}
		
		if (this.attachmentType == EnumAttachmentType.BLOCK) {
			
			if (tag.hasKey("hitPos")) {
				int[] hitPos = tag.getIntArray("hitPos");
				this.hitBlockPos = new BlockPos(hitPos[0], hitPos[1], hitPos[2]);
			}
			
		} else if (this.attachmentType == EnumAttachmentType.ENTITY) {
			
			if (tag.hasUniqueId("hitEntity")) {
				this.hitEntity = tag.getUniqueId("hitEntity");
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setUniqueId("player", playerId);
		tag.setUniqueId("entity", entityId);
		
		tag.setInteger("attachmentType", this.attachmentType.getValue());
		
		if (this.hitVec != null) {
			NBTTagCompound hitVecArray = new NBTTagCompound();
			hitVecArray.setDouble("x", hitVec.x);
			hitVecArray.setDouble("y", hitVec.y);
			hitVecArray.setDouble("z", hitVec.z);
			
			tag.setTag("hitVec", hitVecArray);
		}

		if (this.attachmentType == EnumAttachmentType.BLOCK) {

			if (this.hitBlockPos != null) {
				tag.setIntArray("hitPos", new int[] { hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ() });
			}
			
		} else if (this.attachmentType == EnumAttachmentType.ENTITY) {
			
			if (this.hitEntity != null) {
				tag.setUniqueId("hitEntity", this.hitEntity);
			}
			
		}
		//System.out.println(tag.toString());
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class CScannerLineSyncMessage implements IMessageHandler<ScannerLineSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(ScannerLineSyncMessage message, MessageContext ctx) {

			IThreadListener mainThread = Minecraft.getMinecraft();

			mainThread.addScheduledTask(() -> {
				////System.out.println("Got sync message for entity " + message.entityId.toString() + " player " + message.playerId.toString());
				Minecraft.getMinecraft().world.getLoadedEntityList().forEach((entity) -> {
					if (entity instanceof EntityScannerLine && entity.getUniqueID().compareTo(message.entityId) == 0) {
						
						EntityScannerLine scannerLine = (EntityScannerLine) entity;
						
						////System.out.println("Updating entity " + scannerLine.getUniqueID().toString() + " from Scanner Line Sync. IUUID: " + scannerLine.initialUUID.toString());
						scannerLine
								.setPlayer(Minecraft.getMinecraft().world.getPlayerEntityByUUID(message.playerId));
						
						//System.out.println("Scanner Line Chosen: " + entity.toString());
						//System.out.println("Player of Scanner Line: " + scannerLine.getPlayer().toString());
						
						if (message.hitVec != null) {
							scannerLine.setHitVec(message.hitVec);
							//System.out.println("Hit Vec of Scanner Line: " + scannerLine.getHitVec().toString());
						}
						
						if (message.attachmentType != null) {
							scannerLine.attachmentType = message.attachmentType;
							//System.out.println("Attachment Type of Scanner Line: " + scannerLine.attachmentType);
						}
						
						if (message.attachmentType == EnumAttachmentType.BLOCK) {
							if (message.hitBlockPos != null) {
								scannerLine.setHitBlockPos(message.hitBlockPos);
								//System.out.println("Block Pos of Scanner Line: " + scannerLine.getHitBlockPos().toString()); 
							}
						} else if (message.attachmentType == EnumAttachmentType.ENTITY) {
							if (message.hitEntity != null) {
								
								
								List<Entity> entityList2 = Minecraft.getMinecraft().world
										.getEntitiesWithinAABB(Entity.class, scannerLine.getEntityBoundingBox().grow(1.0D));
								
								for (int i = 0; i < entityList2.size(); i++) {
									Entity currentEntity = entityList2.get(i);
									//System.out.println(currentEntity);
									if (currentEntity.getUniqueID().compareTo(message.hitEntity) == 0) {
										scannerLine.setHitEntity(currentEntity);
										//System.out.println("Found " + currentEntity.toString());
										break;
									} 
								}
								
								//System.out.println("Hit Entity of Scanner Line: " + scannerLine.getHitEntity().toString());
							}
						}
					}
				});
			});

			return null;
		}

	}

}
