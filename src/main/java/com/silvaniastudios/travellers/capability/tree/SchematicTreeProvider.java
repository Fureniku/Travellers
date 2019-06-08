package com.silvaniastudios.travellers.capability.tree;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SchematicTreeProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ISchematicTree.class)
	public static final Capability<ISchematicTree> SCHEMATIC_TREE = null;
	
	private ISchematicTree instance = SCHEMATIC_TREE.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == SCHEMATIC_TREE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		return capability == SCHEMATIC_TREE ? SCHEMATIC_TREE.<T> cast (this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		
		return SCHEMATIC_TREE.getStorage().writeNBT(SCHEMATIC_TREE, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		
		SCHEMATIC_TREE.getStorage().readNBT(SCHEMATIC_TREE, instance, null, nbt);
		
	}

}
