package net.reikeb.electrona.events.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileEnergeticLightningRod;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class EntitySpawnsEvent {

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event == null) return;

        Entity entity = event.getEntity();
        Level world = event.getWorld();

        if (event.getEntity() instanceof EnergeticLightningBolt) {
            EnergeticLightningBolt lightning = (EnergeticLightningBolt) entity;
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();

            for (int cx = -32; cx < 32; cx++) {
                for (int cy = -32; cy < 32; cy++) {
                    for (int cz = -32; cz < 32; cz++) {
                        if (world.getBlockState(new BlockPos(x + cx, y + cy, z + cz)).getBlock() == BlockInit.ENERGETIC_LIGHTNING_ROD.get()) {
                            lightning.teleportTo(x + cx, y + cy, z + cz);
                            BlockEntity tile = world.getBlockEntity(new BlockPos(x + cx, y + cy, z + cz));
                            if (tile instanceof TileEnergeticLightningRod) {
                                ((TileEnergeticLightningRod) tile).struckByLightning();
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}