package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.function.Supplier;

public class BiomeSingleUpdatePacket {

    private final BlockPos pos;
    private final ResourceLocation biome;

    public BiomeSingleUpdatePacket(BlockPos pos, ResourceLocation biome) {
        this.biome = biome;
        this.pos = pos;
    }

    public static BiomeSingleUpdatePacket decode(PacketBuffer buf) {
        return new BiomeSingleUpdatePacket(buf.readBlockPos(), buf.readResourceLocation());
    }

    public void encode(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(biome);
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeCallWhenOn(Dist.CLIENT, () ->  new BiomeUpdate(pos, biome));
        });
        context.get().setPacketHandled(true);
    }

    static public class BiomeUpdate implements DistExecutor.SafeCallable {

        private final BlockPos pos;
        private final ResourceLocation biome;

        public BiomeUpdate(BlockPos pos, ResourceLocation biome) {
            this.pos = pos;
            this.biome = biome;
        }

        @Override
        public Object call() throws Exception {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return null;
            RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome);
            ElectronaUtils.Biome.setBiomeKeyAtPos(world, pos, biomeKey);
            return null;
        }
    }
}
