package net.reikeb.electrona.events.local;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.*;

/**
 * TeleporterUseEvent triggers when an entity uses the Teleporter (or Portable Teleporter) to teleport itself.<br>
 * <br>
 * TeleporterUseEvent.Pre is fired before the entity is teleported.<br>
 * TeleporterUseEvent.Post is fired once the entity has teleported.<br>
 * <br>
 * TeleporterUseEvent.Pre is {@link Cancelable}.<br>
 * Children do not use {@link net.minecraftforge.eventbus.api.Event.HasResult}.<br>
 * Children of this event are fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}.<br>
 */
public class TeleporterUseEvent extends Event {

    private final World world;
    private final World destinationWorld;
    private final BlockPos pos;
    private final BlockPos destinationPos;
    private final Entity entity;

    public TeleporterUseEvent(World world, World destinationWorld, BlockPos pos, BlockPos destinationPos,
                              Entity entity) {
        this.world = world;
        this.destinationWorld = destinationWorld;
        this.pos = pos;
        this.destinationPos = destinationPos;
        this.entity = entity;
    }

    public World getWorld() {
        return world;
    }

    public World getDestinationWorld() {
        return destinationWorld;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockPos getDestinationPos() {
        return destinationPos;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * TeleporterUseEvent.Pre is fired before the entity is teleported. Canceling this event will prevent the entity to teleport itself.<br>
     * <br>
     * This event is {@link Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    @Cancelable
    public static class Pre extends TeleporterUseEvent {
        public Pre(World world, World destinationWorld, BlockPos pos, BlockPos destinationPos, Entity entity) {
            super(world, destinationWorld, pos, destinationPos, entity);
        }
    }

    /**
     * TeleporterUseEvent.Post is fired once the entity has teleported.<br>
     * <br>
     * This event is not {@link Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    public static class Post extends TeleporterUseEvent {
        public Post(World world, World destinationWorld, BlockPos pos, BlockPos destinationPos, Entity entity) {
            super(world, destinationWorld, pos, destinationPos, entity);
        }
    }
}
