package net.reikeb.electrona.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.NonNullLazy;

import net.reikeb.electrona.blockentities.TileGravitor;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.electrona.setup.client.render.BEWLR;

import java.util.function.Consumer;

public class GravitorItem extends BlockItem {

    public GravitorItem() {
        super(BlockInit.GRAVITOR.get(), new Properties()
                .stacksTo(64).rarity(Rarity.RARE).tab(ItemGroups.ELECTRONA_BLOCKS));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private final NonNullLazy<BlockEntityWithoutLevelRenderer> bewlr = NonNullLazy.of(() -> new BEWLR(() -> new TileGravitor(BlockPos.ZERO, Blocks.CONDUIT.defaultBlockState())));

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return bewlr.get();
            }
        });
    }
}
