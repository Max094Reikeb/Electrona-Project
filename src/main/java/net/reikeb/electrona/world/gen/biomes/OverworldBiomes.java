package net.reikeb.electrona.world.gen.biomes;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.reikeb.electrona.init.EntityInit;
import net.reikeb.electrona.init.ParticleInit;

public class OverworldBiomes {

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome nuclear() {
        AmbientParticleSettings particleSettings = new AmbientParticleSettings(ParticleInit.RADIOACTIVE_FALLOUT.get(), 0.118093334F);
        AmbientMoodSettings ambientMoodSettings = new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0D);
        AmbientAdditionsSettings ambientAdditionsSettings = new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111D);
        Music backgroundMusic = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST);

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityInit.RADIOACTIVE_ZOMBIE.get(), 2, 2, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MISC, new MobSpawnSettings.SpawnerData(EntityInit.ENERGETIC_LIGHTNING_BOLT.get(), 1, 1, 1));
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
        BiomeDefaultFeatures.addFossilDecoration(biomeBuilder);
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeFeatures.addNuclearVegetation(biomeBuilder);
        return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.DESERT)
                .temperature(2.0F).downfall(0.0F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(14991423)
                        .waterFogColor(3353861).fogColor(11899149).skyColor(11179278).ambientParticle(particleSettings)
                        .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP).ambientMoodSound(ambientMoodSettings)
                        .ambientAdditionsSound(ambientAdditionsSettings).backgroundMusic(backgroundMusic).build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build()).build();
    }
}
