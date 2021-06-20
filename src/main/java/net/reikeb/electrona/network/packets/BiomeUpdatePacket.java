package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.utils.ElectronaUtils;

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

    public static BiomeUpdatePacket decode(PacketBuffer buf) {
        return new BiomeUpdatePacket(buf.readBlockPos(), buf.readResourceLocation(), buf.readInt());
    }

    public void encode(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(biome);
        buf.writeInt(radius);
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeCallWhenOn(Dist.CLIENT, () ->  new BiomeUpdate(pos, radius));
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
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return null;
            int onepointfiveradius = (radius / 2) * 3;
            int onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
            BlockPos.Mutable newPos = new BlockPos.Mutable().set(pos.getX(), pos.getY(), pos.getZ());
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
                                ElectronaUtils.Biome.setBiomeKeyAtPos(world, newPos, BiomeInit.NUCLEAR_BIOME_KEY);
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
