package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

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
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return;
            RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome);
            ElectronaUtils.Biome.setBiomeKeyAtPos(world, pos, biomeKey);
        });
        context.get().setPacketHandled(true);
    }
}
