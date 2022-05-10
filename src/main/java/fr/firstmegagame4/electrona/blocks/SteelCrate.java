package fr.firstmegagame4.electrona.blocks;

import fr.firstmegagame4.electrona.blockentities.SteelCrateEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SteelCrate extends Crate {
    public SteelCrate(Settings settings, boolean hasItem, ItemGroup itemGroup) {
        super(settings, hasItem, itemGroup);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SteelCrateEntity(pos, state);
    }
}
