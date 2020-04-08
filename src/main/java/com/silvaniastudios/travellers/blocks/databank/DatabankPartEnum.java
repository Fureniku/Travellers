package com.silvaniastudios.travellers.blocks.databank;

import net.minecraft.util.IStringSerializable;

/**
 * Represents a Lower or Upper databank block
 * 
 * @author jamesm2w
 */
public enum DatabankPartEnum implements IStringSerializable {
	UPPER("upper"), LOWER("lower");

	private final String name;

	private DatabankPartEnum(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}
}