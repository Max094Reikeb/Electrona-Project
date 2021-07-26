package net.reikeb.electrona.events.world;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.init.EntityInit;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class WorldTickEvent {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world instanceof ServerLevel) {
                ServerChunkCache chunkProvider = ((ServerLevel) event.world).getChunkSource();
                boolean flag = chunkProvider.level.isDebug();
                if (!flag) {
                    List<ChunkHolder> list = Lists.newArrayList(chunkProvider.chunkMap.getChunks());
                    list.forEach((p_241099_7_) -> {
                        Optional<LevelChunk> optional = p_241099_7_.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
                        if (optional.isPresent()) {
                            Optional<LevelChunk> optional1 = p_241099_7_.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
                            if (optional1.isPresent()) {
                                LevelChunk chunk = optional1.get();
                                ChunkPos chunkpos = p_241099_7_.getPos();
                                if (!chunkProvider.chunkMap.noPlayersCloseForSpawning(chunkpos) || shouldForceTicks(chunkProvider, chunkpos.toLong())) {
                                    lightnings((ServerLevel) event.world, chunk);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private static void lightnings(ServerLevel world, LevelChunk chunk) {
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
            BlockPos pos = world.findLightningTargetAround(world.getBlockRandomPos(i, 0, j, 15));
            if (world.isRainingAt(pos)) {
                EnergeticLightningBolt energeticLightningBolt = EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE.create(world);
                if (energeticLightningBolt == null) return;
                energeticLightningBolt.moveTo(Vec3.atBottomCenterOf(pos));
                world.addFreshEntity(energeticLightningBolt);
            }
        }
    }

    public static boolean shouldForceTicks(ServerChunkCache chunkProvider, long chunkPos) {
        try {
            Method shouldForceTickMethod = ObfuscationReflectionHelper.findMethod(DistanceManager.class, "shouldForceTicks", long.class);
            shouldForceTickMethod.setAccessible(true);
            return (boolean) shouldForceTickMethod.invoke(chunkProvider.chunkMap.getDistanceManager(), chunkPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
