package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.utils.BiomeUtil;

import java.util.Collection;
import java.util.function.Supplier;

public class BiomeUpdatePacket {

    private final Collection<BlockPos> blockPositions;

    public BiomeUpdatePacket(Collection<BlockPos> blockPositions) {
        this.blockPositions = blockPositions;
    }

    public static BiomeUpdatePacket decode(FriendlyByteBuf buf) {
        return new BiomeUpdatePacket(buf.readWithCodec(BlockPos.CODEC.listOf()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeWithCodec(BlockPos.CODEC.listOf(), blockPositions.stream().toList());
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> new BiomeUpdate(blockPositions));
        });
        context.get().setPacketHandled(true);
    }

    static public class BiomeUpdate implements DistExecutor.SafeCallable {

        private final Collection<BlockPos> blockPositions;

        public BiomeUpdate(Collection<BlockPos> blockPositions) {
            this.blockPositions = blockPositions;
        }

        @Override
        public Object call() throws Exception {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return null;
            for (BlockPos pos : this.blockPositions) {
                BiomeUtil.setBiomeAtPos(world, pos, world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(BiomeInit.NUCLEAR).orElseThrow());
            }
            return null;
        }
    }
}
