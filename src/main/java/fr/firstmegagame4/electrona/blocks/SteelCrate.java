package fr.firstmegagame4.electrona.blocks;

import fr.firstmegagame4.electrona.blockentities.SteelCrateEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

public class SteelCrate extends Crate {
    public SteelCrate(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SteelCrateEntity(pos, state);
    }
}
