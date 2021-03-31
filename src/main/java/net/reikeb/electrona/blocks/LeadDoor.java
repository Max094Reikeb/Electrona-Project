package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import net.minecraftforge.common.ToolType;

import java.util.*;

public class LeadDoor extends DoorBlock {

    public LeadDoor() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .lightLevel(s -> 0)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)
            return Collections.emptyList();
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return 1005;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {
        state = state.cycle(OPEN);
        world.setBlock(pos, state, 10);
        world.levelEvent(playerEntity, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        return ActionResultType.sidedSuccess(world.isClientSide);
    }
}
