package com.silvaniastudios.travellers;

import java.util.Random;

import com.silvaniastudios.travellers.blocks.TravellersOre;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {
	
	WorldGenMinable atlasOre;
	TravellersOre atlasBlock = ModBlocks.aluminium_ore;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) { // the overworld
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}		
	}
	
	private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		//addOreSpawn(atlasOre, world, random, chunkX * 16, chunkZ * 16, WorldConfig.ore.atlasVeinsPerChunk, WorldConfig.ore.atlasHeightMin, WorldConfig.ore.atlasHeightMax);
	}

	@SuppressWarnings("unused")
	private void addOreSpawn(WorldGenMinable wgm, World world, Random random, int x, int z, int vpc, int minY, int maxY) {
		int rangeY = maxY - minY;
		for (int i = 0; i < vpc; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(rangeY), z + random.nextInt(16));
			wgm.generate(world, random, pos);
		}
	}
}
