package com.silvaniastudios.travellers.config;

import com.silvaniastudios.travellers.Travellers;

import net.minecraftforge.common.config.Config;

@Config(modid = Travellers.MODID, name = "Travellers/Travellers Config")
@Config.LangKey("travellers.config.title_world")
public class TravellersConfig {
	
	@Config.Name("Ore Generation")
	public static OreGen ore = new OreGen();
	
	@Config.Name("Legacy Options")
	public static Legacy legacy = new Legacy();
		
	public static class Legacy {
		@Config.Comment("Enable legacy woods; Ebony, Ironwood, Mahogony and Maple. May cause balance issues.")
		public boolean legacyWoods = false;
		@Config.Comment("Enable legacy metals; Magnesium, Palladium and Platinum. May cause balance issues.")
		public boolean legacyMetals = false;
	}
	
	public static class OreGen {
	}

}
