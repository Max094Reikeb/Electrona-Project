package net.reikeb.electrona.utils;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    /**
     * Method that gets the RayTraceResult of where an entity looks at
     *
     * @param entity        The entity
     * @param range         The range
     * @param height        The speed
     * @param includeFluids Defines if fluids count
     * @return The RayTraceResult
     */
    public static HitResult lookAt(Entity entity, double range, float height, boolean includeFluids) {
        Vec3 vector3d = entity.getEyePosition(height);
        Vec3 vector3d1 = entity.getViewVector(height);
        Vec3 vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
        return entity.level.clip(new ClipContext(vector3d, vector3d2, ClipContext.Block.OUTLINE, includeFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, entity));
    }

    /**
     * Method that get the BlockHitResult of where the player looks at
     *
     * @param world         The world
     * @param player        The player
     * @param fluidHandling Defines the fluid handling
     * @return The BlockHitResult
     */
    public static BlockHitResult rayTrace(Level world, Player player, ClipContext.Fluid fluidHandling) {
        float f = player.xRot;
        float g = player.yRot;
        Vec3 vec3d = player.getPosition(1.0f);
        float h = (float) Math.cos(-g * 0.017453292F - 3.1415927F);
        float i = (float) Math.sin(-g * 0.017453292F - 3.1415927F);
        float j = (float) -Math.cos(-f * 0.017453292F);
        float k = (float) Math.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        Vec3 vec3d2 = new Vec3(l * 5.0D, k * 5.0D, n * 5.0D);
        return world.clip(new ClipContext(vec3d, vec3d.add(vec3d2), ClipContext.Block.OUTLINE, fluidHandling, player));
    }

    /**
     * Method to bind a texture from a RL to a RenderSystem
     *
     * @param res The ResourceLocation of the texture to bind
     */
    public static void bind(ResourceLocation res) {
        RenderSystem.setShaderTexture(0, res);
    }
}
