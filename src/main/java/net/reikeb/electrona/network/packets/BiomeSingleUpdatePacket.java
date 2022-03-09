package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.utils.BiomeUtil;

import java.util.function.Supplier;

public class BiomeSingleUpdatePacket {

    private final BlockPos pos;
    private final ResourceLocation biome;

    public BiomeSingleUpdatePacket(BlockPos pos, ResourceLocation biome) {
        this.biome = biome;
        this.pos = pos;
    }

    public static BiomeSingleUpdatePacket decode(FriendlyByteBuf buf) {
        return new BiomeSingleUpdatePacket(buf.readBlockPos(), buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(biome);
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> new BiomeUpdate(pos, biome));
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
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return null;
            ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome);
            BiomeUtil.setBiomeKeyAtPos(world, pos, biomeKey);
            return null;
        }
    }
}
