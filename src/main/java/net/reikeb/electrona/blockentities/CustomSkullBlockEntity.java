package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.reikeb.electrona.init.BlockEntityInit.CUSTOM_SKULL;

public class CustomSkullBlockEntity extends SkullBlockEntity {

    public CustomSkullBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return CUSTOM_SKULL.get();
    }
}