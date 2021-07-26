package net.reikeb.electrona.tileentities;

import com.google.common.collect.Lists;

import net.minecraft.block.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.api.distmarker.*;

import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.ElectronaUtils;

import javax.annotation.Nullable;
import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileGravitor extends BlockEntity implements TickableBlockEntity {

    public int tickCount;
    private float activeRotation;
    private boolean isActive;
    private boolean isHunting;
    private final List<BlockPos> effectBlocks = Lists.newArrayList();
    private long nextAmbientSoundActivation;

    public double electronicPower;
    private int maxStorage;

    public TileGravitor() {
        this(TileEntityInit.TILE_GRAVITOR.get());
    }

    public TileGravitor(BlockEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void tick() {
        if (this.level == null) return;

        this.getTileData().putInt("MaxStorage", 50);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        long i = this.level.getGameTime();

        ++this.tickCount;

        if (i % 40L == 0L) {
            boolean flag = (this.updateShape()) && (electronicPower >= 10);
            this.setActive(flag);
            if (!this.level.isClientSide && this.isActive()) {
                this.getTileData().putDouble("ElectronicPower", (electronicPower - 10));
                this.applyGravity();
            }
        }

        if (i % 80L == 0L && this.isActive()) {
            this.playSound(SoundEvents.CONDUIT_AMBIENT);
        }

        if (i > this.nextAmbientSoundActivation && this.isActive()) {
            this.nextAmbientSoundActivation = i + 60L + (long) this.level.getRandom().nextInt(40);
            this.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT);
        }

        if (this.level.isClientSide) {
            this.animationTick();
            if (this.isActive()) {
                ++this.activeRotation;
            }
        }
    }

    private void setActive(boolean flag) {
        if (flag != this.isActive) {
            this.playSound(flag ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE);
        }

        this.isActive = flag;
    }

    private boolean updateShape() {
        if (this.level == null) return false;
        this.effectBlocks.clear();

        for (int j1 = -2; j1 <= 2; ++j1) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                for (int l1 = -2; l1 <= 2; ++l1) {
                    int i2 = Math.abs(j1);
                    int l = Math.abs(k1);
                    int i1 = Math.abs(l1);
                    if ((i2 > 1 || l > 1 || i1 > 1) && (j1 == 0 && (l == 2 || i1 == 2) || k1 == 0 && (i2 == 2 || i1 == 2) || l1 == 0)) {
                        BlockPos blockpos1 = this.worldPosition.offset(j1, k1, l1);
                        BlockState blockstate = this.level.getBlockState(blockpos1);

                        if (blockstate.getBlock() == BlockInit.GRAVITONIUM_BLOCK.get()) {
                            this.effectBlocks.add(blockpos1);
                        }
                    }
                }
            }
        }

        this.setHunting(this.effectBlocks.size() >= 42);
        return this.effectBlocks.size() >= 16;
    }

    private void applyGravity() {
        if (this.level == null) return;
        int i = this.effectBlocks.size();
        int j = i / 7 * 8;
        int k = this.worldPosition.getX();
        int l = this.worldPosition.getY();
        int i1 = this.worldPosition.getZ();
        AABB axisalignedbb = (new AABB(k, l, i1, k + 1, l + 1, i1 + 1)).inflate(j);
        BlockPos minPos = new BlockPos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        BlockPos maxPos = new BlockPos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        Iterable<BlockPos> posList = BlockPos.betweenClosed(minPos, maxPos);
        for (BlockPos pos : posList) {
            boolean flag = (pos.getX() - k > 2) || (pos.getX() - k < -2) || (pos.getY() - l > 2) || (pos.getY() - l < -2) || (pos.getZ() - i1 > 2) || (pos.getZ() - i1 < -2);
            if (flag) ElectronaUtils.Gravity.applyGravity(this.level, pos);
        }
    }

    private void animationTick() {
        if (this.level == null) return;
        Random random = this.level.random;
        double d0 = Mth.sin((float) (this.tickCount + 35) * 0.1F) / 2.0F + 0.5F;
        d0 = (d0 * d0 + d0) * (double) 0.3F;
        Vec3 vector3d = new Vec3((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 1.5D + d0, (double) this.worldPosition.getZ() + 0.5D);

        for (BlockPos blockpos : this.effectBlocks) {
            if (random.nextInt(50) == 0) {
                float f = -0.5F + random.nextFloat();
                float f1 = -2.0F + random.nextFloat();
                float f2 = -0.5F + random.nextFloat();
                BlockPos blockpos1 = blockpos.subtract(this.worldPosition);
                Vec3 vector3d1 = (new Vec3(f, f1, f2)).add(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                this.level.addParticle(ParticleInit.GRAVITORIUM.get(), vector3d.x, vector3d.y, vector3d.z, vector3d1.x, vector3d1.y, vector3d1.z);
            }
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isHunting() {
        return this.isHunting;
    }

    private void setHunting(boolean flag) {
        this.isHunting = flag;
    }

    @OnlyIn(Dist.CLIENT)
    public float getActiveRotation(float rotate) {
        return (this.activeRotation + rotate) * -0.0375F;
    }

    public void playSound(SoundEvent sound) {
        if (this.level == null) return;
        this.level.playSound(null, this.worldPosition, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.electronicPower = nbt.getDouble("ElectronicPower");
        this.maxStorage = nbt.getInt("MaxStorage");
    }

    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putDouble("ElectronicPower", this.electronicPower);
        nbt.putInt("MaxStorage", this.maxStorage);
        return nbt;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 5, this.getUpdateTag());
    }

    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
}
