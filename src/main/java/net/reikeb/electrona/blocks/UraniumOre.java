package net.reikeb.electrona.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.IRuleTestType;

import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UraniumOre extends Block {

    public UraniumOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(3f, 5f)
                .lightLevel(s -> 0)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
                .hasPostProcess((bs, br, bp) -> true)
                .emissiveRendering((bs, br, bp) -> true));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @SubscribeEvent
    public void addFeatureToBiomes(BiomeLoadingEvent event) {
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> new OreFeature(OreFeatureConfig.CODEC) {
            @Override
            public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, OreFeatureConfig config) {
                RegistryKey<World> dimensionType = world.getLevel().dimension();
                boolean dimensionCriteria = false;
                if (dimensionType == World.OVERWORLD)
                    dimensionCriteria = true;
                if (!dimensionCriteria)
                    return false;
                return super.place(world, generator, rand, pos, config);
            }
        }.configured(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.STONE.defaultBlockState().getBlock()) {
            public boolean test(BlockState blockAt, Random random) {
                boolean blockCriteria = false;
                if (blockAt.getBlock() == Blocks.STONE.defaultBlockState().getBlock())
                    blockCriteria = true;
                return blockCriteria;
            }

            protected IRuleTestType<?> getType() {
                return IRuleTestType.BLOCK_TEST;
            }
        }, this.defaultBlockState(), 4)).range(30).squared().count(3));
    }
}
