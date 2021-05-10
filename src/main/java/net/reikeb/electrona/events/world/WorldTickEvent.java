package net.reikeb.electrona.events.world;

import com.google.common.collect.Lists;

import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.*;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.init.EntityInit;

import java.lang.reflect.Method;
import java.util.*;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class WorldTickEvent {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world instanceof ServerWorld) {
                ServerChunkProvider chunkProvider = ((ServerWorld) event.world).getChunkSource();
                boolean flag = chunkProvider.level.isDebug();
                if (!flag) {
                    List<ChunkHolder> list = Lists.newArrayList(chunkProvider.chunkMap.getChunks());
                    list.forEach((p_241099_7_) -> {
                        Optional<Chunk> optional = p_241099_7_.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
                        if (optional.isPresent()) {
                            Optional<Chunk> optional1 = p_241099_7_.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
                            if (optional1.isPresent()) {
                                Chunk chunk = optional1.get();
                                ChunkPos chunkpos = p_241099_7_.getPos();
                                if (!chunkProvider.chunkMap.noPlayersCloseForSpawning(chunkpos) || shouldForceTicks(chunkProvider, chunkpos.toLong())) {
                                    lightnings((ServerWorld) event.world, chunk);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private static void lightnings(ServerWorld world, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        int delay = 0;
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            delay = 100000;
        } else if (world.getDifficulty() == Difficulty.EASY) {
            delay = 500000;
        } else if (world.getDifficulty() == Difficulty.NORMAL) {
            delay = 800000;
        } else {
            delay = 1000000;
        }
        if (world.isRaining() && world.isThundering() && world.random.nextInt(delay) == 0) {
            BlockPos pos = world.findLightingTargetAround(world.getBlockRandomPos(i, 0, j, 15));
            if (world.isRainingAt(pos)) {
                EnergeticLightningBolt energeticLightningBolt = EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE.create(world);
                if (energeticLightningBolt == null) return;
                energeticLightningBolt.moveTo(Vector3d.atBottomCenterOf(pos));
                world.addFreshEntity(energeticLightningBolt);
            }
        }
    }

    public static boolean shouldForceTicks(ServerChunkProvider chunkProvider, long chunkPos) {
        try {
            Method shouldForceTickMethod = ObfuscationReflectionHelper.findMethod(TicketManager.class, "shouldForceTicks", long.class);
            shouldForceTickMethod.setAccessible(true);
            return (boolean) shouldForceTickMethod.invoke(chunkProvider.chunkMap.getDistanceManager(), chunkPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
