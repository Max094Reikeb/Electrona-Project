package net.reikeb.electrona.world.gen.biomes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.init.BlockInit;

public class SurfaceRuleData {
    private static final SurfaceRules.RuleSource RADIOACTIVE_DIRT = makeStateRule(BlockInit.RADIOACTIVE_DIRT.get());

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, RADIOACTIVE_DIRT), RADIOACTIVE_DIRT);

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(BiomeInit.NUCLEAR), RADIOACTIVE_DIRT),

                // Default to a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}