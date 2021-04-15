package net.reikeb.electrona.utils;

import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.World;

public class ElectronaUtils {

    /**
     * Method to rotate a VoxelShape around an axis
     *
     * @param from  Origin Direction
     * @param to    Destination Direction
     * @param shape The VoxelShape to rotate
     * @return The rotated VoxelShape
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    /**
     * Method that plays a sound on both client and server side
     *
     * @param world The world
     * @param pos   The position of the entity/block
     * @param sound The sound
     */
    public static void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category) {
        if (world.isClientSide) {
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound,
                    category, 1F, 1F, false);
        } else {
            world.playSound(null, pos, sound, category, 1F, 1F);
        }
    }
}
