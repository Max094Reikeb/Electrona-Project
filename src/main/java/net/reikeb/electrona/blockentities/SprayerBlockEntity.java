package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.SprayerContainer;
import net.reikeb.electrona.misc.vm.SprayerFunction;

import static net.reikeb.electrona.init.BlockEntityInit.SPRAYER_BLOCK_ENTITY;

public class SprayerBlockEntity extends AbstractBlockEntity {

    public static final BlockEntityTicker<SprayerBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private int radius;
    private int wait;

    public SprayerBlockEntity(BlockPos pos, BlockState state) {
        super(SPRAYER_BLOCK_ENTITY.get(), pos, state, 4);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.sprayer.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("sprayer");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SprayerContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 3000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions
            SprayerFunction.mainSprayer(this.inventory, this, electronicPower);
            wait++;
            if (wait >= 30) {
                wait = 0;
                SprayerFunction.sprayerParticles(world, this, blockPos);
            }

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getElectronicPowerTimesHundred() {
        return (int) (this.electronicPower * 100);
    }

    public void setElectronicPowerTimesHundred(int electronicPowerTimesHundred) {
        this.electronicPower = electronicPowerTimesHundred / 100.0;
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.radius = compound.getInt("radius");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("radius", this.radius);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
