package net.reikeb.electrona.misc;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.reikeb.maxilib.utils.Utils;

import java.util.stream.Stream;

public class CustomShapes {

    /**
     * VoxelShape of the Conveyor
     */
    public static VoxelShape Conveyor = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(15, 2, 0, 16, 4, 16),
            Block.box(0, 2, 0, 1, 4, 16)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();
    /**
     * VoxelShapes of the Energetic Lightning Rod
     */
    public static VoxelShape EnergeticLightningRodUp = Shapes.join(
            Block.box(6, 12, 6, 10, 16, 10),
            Block.box(7, 0, 7, 9, 12, 9),
            BooleanOp.OR);
    public static VoxelShape EnergeticLightningRodDown = Shapes.join(
            Block.box(6, 0, 6, 10, 4, 10),
            Block.box(7, 4, 7, 9, 16, 9),
            BooleanOp.OR);
    public static VoxelShape EnergeticLightningRod = Shapes.join(
            Block.box(6, 6, 0, 10, 10, 4),
            Block.box(7, 7, 4, 9, 9, 16),
            BooleanOp.OR);
    /**
     * VoxelShape of the Nuclear Bomb
     */
    public static VoxelShape NuclearBomb = Stream.of(
            Block.box(1, 2, 3, 15, 8, 13),
            Block.box(-7, -1, 2, -2, 11, 14),
            Block.box(-2, 1, 4, 16, 9, 12),
            Block.box(1, 0, 5, 15, 10, 11),
            Block.box(16, 2, 5, 17, 8, 11),
            Block.box(17, 3, 6, 18, 7, 10)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();
    /**
     * VoxelShape of the Nuclear Generator Controller
     */
    public static VoxelShape NuclearGenerator = Stream.of(
            Block.box(1, 0, 1, 15, 13, 15),
            Block.box(15, 12, 1, 15.5, 13, 2),
            Block.box(14.5, 12, 0.5, 15.5, 13, 1),
            Block.box(14, 12, 0.5, 15, 13, 1),
            Block.box(14.4, 11, 0.5, 15.4, 12, 1),
            Block.box(11, 5, 0.8, 11.5, 5.5, 1),
            Block.box(10, 5, 0.8, 10.5, 5.5, 1),
            Block.box(10, 4, 0.8, 10.5, 4.5, 1),
            Block.box(10, 3, 0.8, 10.5, 3.5, 1),
            Block.box(11, 3, 0.8, 11.5, 3.5, 1),
            Block.box(11, 4, 0.7999999999999999, 11.5, 4.5, 1),
            Block.box(8, 5, 0.8, 8.5, 5.5, 1),
            Block.box(8, 3, 0.8, 8.5, 3.5, 1),
            Block.box(6, 4, 0.8, 6.5, 4.5, 1),
            Block.box(6, 3, 0.7999999999999999, 6.5, 3.5, 1),
            Block.box(6, 5, 0.8, 6.5, 5.5, 1),
            Block.box(4.5, 3, 0.7999999999999999, 5, 3.5, 1),
            Block.box(4.5, 4, 0.7999999999999999, 5, 4.5, 1),
            Block.box(4.5, 5, 0.7999999999999999, 5, 5.5, 1),
            Block.box(3, 5, 0.8, 3.5, 5.5, 1),
            Block.box(3, 4, 0.8, 3.5, 4.5, 1),
            Block.box(3, 3, 0.7999999999999999, 3.5, 3.5, 1),
            Block.box(12, 3, 0.8, 12.5, 3.5, 1),
            Block.box(12, 4, 0.8, 12.5, 4.5, 1),
            Block.box(12, 5, 0.8, 12.5, 5.5, 1),
            Block.box(12, 2, 0.8, 12.5, 2.5, 1),
            Block.box(11, 2, 0.8, 11.5, 2.5, 1),
            Block.box(10, 2, 0.8, 10.5, 2.5, 1),
            Block.box(6, 2, 0.8, 6.5, 2.5, 1),
            Block.box(4.5, 2, 0.8, 5, 2.5, 1),
            Block.box(3, 2, 0.7999999999999999, 3.5, 2.5, 1),
            Block.box(15, 11, 0.5, 15.5, 12, 1.5),
            Block.box(1, 12, 0.5, 2, 13, 1),
            Block.box(0.5, 12, 0.5, 1.5, 13, 1),
            Block.box(0.5, 12, 1, 1, 13, 2),
            Block.box(0.5, 11, 0.5, 1.5, 12, 1),
            Block.box(0.5, 11, 0.5, 1, 12, 1.5),
            Block.box(0.5, 12, 14, 1, 13, 15),
            Block.box(14.5, 12, 15, 15.5, 13, 15.5),
            Block.box(1, 12, 15, 2, 13, 15.5),
            Block.box(0.5, 11, 14.5, 1, 12, 15.5),
            Block.box(0.5, 11, 15, 1.5, 12, 15.5),
            Block.box(0.5, 12, 15, 1.5, 13, 15.5),
            Block.box(14, 12, 15, 15, 13, 15.5),
            Block.box(15, 12, 14, 15.5, 13, 15),
            Block.box(14.5, 11, 15, 15.5, 12, 15.5),
            Block.box(14.5, 0, 0.5, 15.5, 1, 1),
            Block.box(15, 0, 1, 15.5, 1, 2),
            Block.box(14, 0, 0.5, 15, 1, 1),
            Block.box(14.4, 1, 0.5, 15.4, 2, 1),
            Block.box(15, 1, 0.5, 15.5, 2, 1.5),
            Block.box(15, 11, 14.5, 15.5, 12, 15.5),
            Block.box(15, 0, 14, 15.5, 1, 15),
            Block.box(15, 0, 14.5, 15.5, 1, 15.5),
            Block.box(14, 0, 15, 15, 1, 15.5),
            Block.box(15, 1, 14.5, 15.5, 2, 15.5),
            Block.box(14.4, 1, 15, 15.4, 2, 15.5),
            Block.box(0.5, 0, 0.5, 1.5, 1, 1),
            Block.box(1, 0, 0.5, 2, 1, 1),
            Block.box(0.5, 0, 1, 1, 1, 2),
            Block.box(0.5, 1, 0.5, 1.5, 2, 1),
            Block.box(0.5, 1, 14.5, 1, 2, 15.5),
            Block.box(0.5, 0, 15, 1.5, 1, 15.5),
            Block.box(0.5, 0, 14, 1, 1, 15),
            Block.box(1, 0, 15, 2, 1, 15.5),
            Block.box(0.5, 1, 0.5, 1, 2, 1.5),
            Block.box(0.5, 1, 15, 1.5, 2, 15.5)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();
    /**
     * VoxelShape of the Purificator
     */
    public static VoxelShape Purificator = Stream.of(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(0, 4, 12, 16, 12, 16),
            Block.box(2, 4, 2, 14, 12, 14)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();
    /**
     * VoxelShape of the Teleporter
     */
    public static VoxelShape Teleporter = Shapes.join(
            Block.box(-2, 0, -2, 18, 3, 18),
            Block.box(0, 3, 0, 16, 6, 16),
            BooleanOp.OR);
    /**
     * VoxelShape of the Water Turbine
     */
    public static VoxelShape WaterTurbine = Stream.of(
            Block.box(0, 0, 0, 16, 16, 14),
            Block.box(0, 0, 14, 4, 16, 16),
            Block.box(12, 0, 14, 16, 16, 16),
            Block.box(4, 0, 14, 12, 4, 16),
            Block.box(4, 12, 14, 12, 16, 16)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();

    /**
     * Rotates the CustomShape depending on the direction
     *
     * @param facing Direction of the block
     * @param shape  CustomShape of the block
     * @return Rotated CustomShape
     */
    public static VoxelShape getVoxelShape(Direction facing, VoxelShape shape) {
        if (facing == Direction.SOUTH) {
            return Utils.rotateShape(Direction.NORTH, Direction.SOUTH, shape);
        } else if (facing == Direction.EAST) {
            return Utils.rotateShape(Direction.NORTH, Direction.EAST, shape);
        } else if (facing == Direction.WEST) {
            return Utils.rotateShape(Direction.NORTH, Direction.WEST, shape);
        }
        return shape;
    }
}
