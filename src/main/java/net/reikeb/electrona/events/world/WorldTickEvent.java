package net.reikeb.electrona.events.world;

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

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.init.EntityInit;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class WorldTickEvent {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.world instanceof ServerLevel serverLevel)) return;
        if (serverLevel.getGameTime() % 100 != 0) return;
        ServerChunkCache chunkProvider = serverLevel.getChunkSource();
        if (chunkProvider.level.isDebug()) return;
        chunkProvider.chunkMap.getChunks().forEach(chunkHolder -> {
            Optional<LevelChunk> optional = chunkHolder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
            if (optional.isEmpty()) return;
            ChunkPos chunkpos = optional.get().getPos();
            DistanceManager distanceManager = chunkProvider.chunkMap.getDistanceManager();
            if ((serverLevel.isPositionEntityTicking(chunkpos) && !chunkProvider.chunkMap.anyPlayerCloseEnoughForSpawning(chunkpos))
                    || distanceManager.shouldForceTicks(chunkpos.toLong())) {
                lightnings(serverLevel, optional.get());
            }
        });
    }

    private static void lightnings(ServerLevel world, LevelChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int delay = 10000;
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            delay = 1000;
        } else if (world.getDifficulty() == Difficulty.EASY) {
            delay = 5000;
        } else if (world.getDifficulty() == Difficulty.NORMAL) {
            delay = 8000;
        }
        if (world.isRaining() && world.isThundering() && world.random.nextInt(delay) == 0) {
            BlockPos pos = world.findLightningTargetAround(world.getBlockRandomPos(chunkPos.getMinBlockX(),
                    0, chunkPos.getMinBlockZ(), 15));
            if (!world.isRainingAt(pos)) return;
            EnergeticLightningBolt energeticLightningBolt = EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE.create(world);
            if (energeticLightningBolt == null) return;
            energeticLightningBolt.moveTo(Vec3.atBottomCenterOf(pos));
            world.addFreshEntity(energeticLightningBolt);
        }
    }
}
