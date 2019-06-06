package com.silvaniastudios.travellers;

public enum EnumMaterial {
	aluminium("aluminium", 0.33, 345656);
	
	String name;
	double weight;
	int rgb;
	
	private EnumMaterial(String name, double weight, int rgb) {
		this.name = name;
		this.weight = weight;
		this.rgb = rgb;
	}

}
