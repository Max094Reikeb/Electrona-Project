package fr.firstmegagame4.electrona.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;

public abstract class HorizontalFacingBlockWithEntity extends FacingBlockWithEntity {

    protected HorizontalFacingBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }

}
