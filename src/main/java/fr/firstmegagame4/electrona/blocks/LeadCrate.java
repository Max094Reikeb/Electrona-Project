package fr.firstmegagame4.electrona.blocks;

import fr.firstmegagame4.electrona.blockentities.LeadCrateEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LeadCrate extends Crate {
    public LeadCrate(Settings settings, boolean hasItem, ItemGroup itemGroup) {
        super(settings, hasItem, itemGroup);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LeadCrateEntity(pos, state);
    }
}
