package net.reikeb.electrona.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

import net.reikeb.electrona.Electrona;

import org.apache.logging.log4j.Level;

import java.util.List;

public class RuinsStructure extends Structure<NoFeatureConfig> {

    public RuinsStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
        return RuinsStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
            new MobSpawnInfo.Spawners(EntityType.ILLUSIONER, 100, 4, 9),
            new MobSpawnInfo.Spawners(EntityType.VINDICATOR, 100, 4, 9)
    );

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
            new MobSpawnInfo.Spawners(EntityType.SHEEP, 30, 10, 15),
            new MobSpawnInfo.Spawners(EntityType.RABBIT, 100, 1, 2)
    );

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            // Finds the y value of the terrain at location.
            int surfaceY = generator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            // Now adds the structure pieces to this.components with all details such as where each part goes
            // so that the structure can be added to the world by worldgen.
            RuinsPieces.start(templateManagerIn, blockpos, rotation, this.pieces, this.random);

            // Sets the bounds of the structure.
            this.calculateBoundingBox();

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            Electrona.LOGGER.log(Level.DEBUG, "Ruins at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
            System.out.println(this.pieces);
        }
    }
}
