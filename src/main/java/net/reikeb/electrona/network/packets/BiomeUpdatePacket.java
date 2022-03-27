package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.utils.BiomeUtil;

import java.util.function.Supplier;

public class BiomeUpdatePacket {

    private final BlockPos pos;
    private final ResourceLocation biome;
    private final int radius;

    public BiomeUpdatePacket(BlockPos pos, ResourceLocation biome, int radius) {
        this.biome = biome;
        this.pos = pos;
        this.radius = radius;
    }

    public static BiomeUpdatePacket decode(FriendlyByteBuf buf) {
        return new BiomeUpdatePacket(buf.readBlockPos(), buf.readResourceLocation(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(biome);
        buf.writeInt(radius);
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> new BiomeUpdate(pos, radius));
        });
        context.get().setPacketHandled(true);
    }

    static public class BiomeUpdate implements DistExecutor.SafeCallable {

        private final BlockPos pos;
        private final int radius;

        public BiomeUpdate(BlockPos pos, int radius) {
            this.pos = pos;
            this.radius = radius;
        }

        @Override
        public Object call() throws Exception {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return null;
            int onepointfiveradius = (radius / 2) * 3;
            int onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
            BlockPos.MutableBlockPos newPos = new BlockPos.MutableBlockPos().set(pos.getX(), pos.getY(), pos.getZ());
            for (int X = -onepointfiveradius; X <= onepointfiveradius; X++) {
                int xx = pos.getX() + X;
                int XX = X * X;
                for (int Z = -onepointfiveradius; Z <= onepointfiveradius; Z++) {
                    int ZZ = Z * Z + XX;
                    int zz = pos.getZ() + Z;
                    for (int Y = -onepointfiveradius; Y <= onepointfiveradius; Y++) {
                        int YY = Y * Y + ZZ;
                        int yy = pos.getY() + Y;
                        if (YY < onepointfiveradiussqrd) {
                            int dist = (int) Math.sqrt(YY);
                            if (dist < radius || dist < onepointfiveradius) {
                                newPos.set(xx, yy, zz);
                                BiomeUtil.setBiomeKeyAtPos(world, newPos, BiomeInit.NUCLEAR);
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
