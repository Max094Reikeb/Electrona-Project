package net.reikeb.electrona.misc;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockStateProperties {

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final BooleanProperty COMPRESSING = BooleanProperty.create("compressing");
    public static final BooleanProperty HEATING = BooleanProperty.create("heating");
    public static final BooleanProperty PUMPING = BooleanProperty.create("pumping");
}
