package com.silvaniastudios.travellers.config;

import com.silvaniastudios.travellers.Travellers;

import net.minecraftforge.common.config.Config;

@Config(modid = Travellers.MODID, name = "Travellers/World Config")
@Config.LangKey("travellers.config.title_world")
public class WorldConfig {
	
	@Config.Name("Ore Generation")
	public static OreGen ore = new OreGen();
	
	public static class OreGen {
	}

}
