package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.maxilib.abs.AbstractFluidBlockEntity;

import static net.reikeb.electrona.init.BlockEntityInit.COOLER_BLOCK_ENTITY;

public class CoolerBlockEntity extends AbstractFluidBlockEntity {

    public CoolerBlockEntity(BlockPos pos, BlockState state) {
        super(COOLER_BLOCK_ENTITY.get(), pos, state, "cooler", Electrona.MODID, 1, 10000);
    }
}
