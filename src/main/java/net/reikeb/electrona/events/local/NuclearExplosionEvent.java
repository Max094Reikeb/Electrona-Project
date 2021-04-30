package net.reikeb.electrona.events.local;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.*;

import net.reikeb.electrona.world.NuclearExplosion;

import java.util.List;

/**
 * NuclearExplosionEvent triggers when a nuclear explosion happens in the world.<br>
 * <br>
 * NuclearExplosionEvent.Start is fired before the nuclear explosion actually occurs.<br>
 * NuclearExplosionEvent.Detonate is fired once the nuclear explosion has a list of affected blocks and entities.<br>
 * <br>
 * NuclearExplosionEvent.Start is {@link Cancelable}.<br>
 * NuclearExplosionEvent.Detonate can modify the affected blocks and entities.<br>
 * Children do not use {@link Event.HasResult}.<br>
 * Children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
public class NuclearExplosionEvent extends Event {

    private final World world;
    private final NuclearExplosion nuclearExplosion;

    public NuclearExplosionEvent(World world, NuclearExplosion nuclearExplosion) {
        this.world = world;
        this.nuclearExplosion = nuclearExplosion;
    }

    public World getWorld() {
        return world;
    }

    public NuclearExplosion getNuclearExplosion() {
        return nuclearExplosion;
    }

    /**
     * NuclearExplosionEvent.Start is fired before the nuclear explosion actually occurs.  Canceling this event will stop the nuclear explosion.<br>
     * <br>
     * This event is {@link net.minecraftforge.eventbus.api.Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    @Cancelable
    public static class Start extends NuclearExplosionEvent {
        public Start(World world, NuclearExplosion nuclearExplosion) {
            super(world, nuclearExplosion);
        }
    }

    /**
     * NuclearExplosionEvent.Detonate is fired once the nuclear explosion has a list of affected blocks and entities.  These lists can be modified to change the outcome.<br>
     * <br>
     * This event is not {@link Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    public static class Detonate extends NuclearExplosionEvent {
        private final List<Entity> entityList;
        private final List<Block> blockList;

        public Detonate(World world, NuclearExplosion nuclearExplosion, List<Entity> entityList, List<Block> blockList) {
            super(world, nuclearExplosion);
            this.entityList = entityList;
            this.blockList = blockList;
        }

        /**
         * Return the list of entities affected by the explosion.
         */
        public List<Entity> getAffectedEntities() {
            return entityList;
        }

        /**
         * Return the list of blocks affected by the explosion
         */
        public List<Block> getAffectedBlocks() {
            return blockList;
        }
    }
}
