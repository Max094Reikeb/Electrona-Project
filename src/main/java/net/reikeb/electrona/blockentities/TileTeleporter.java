package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.init.ItemInit;

import static net.reikeb.electrona.init.TileEntityInit.TILE_TELEPORTER;

public class TileTeleporter extends AbstractTileEntity {

    public static final BlockEntityTicker<TileTeleporter> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private double teleportX;
    private double teleportY;
    private double teleportZ;
    private int itemTeleportX;
    private int itemTeleportY;
    private int itemTeleportZ;
    private boolean autoDeletion;
    private boolean isTeleportSaver;
    private boolean isTeleporter;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            return switch (p_39284_) {
                case 0 -> (int) TileTeleporter.this.electronicPower;
                case 1 -> (int) TileTeleporter.this.teleportX;
                case 2 -> (int) TileTeleporter.this.teleportY;
                case 3 -> (int) TileTeleporter.this.teleportZ;
                case 4 -> (TileTeleporter.this.autoDeletion ? 1 : 0);
                case 5 -> TileTeleporter.this.itemTeleportX;
                case 6 -> TileTeleporter.this.itemTeleportY;
                case 7 -> TileTeleporter.this.itemTeleportZ;
                case 8 -> (TileTeleporter.this.isTeleportSaver ? 1 : 0);
                case 9 -> (TileTeleporter.this.isTeleporter ? 1 : 0);
                default -> 0;
            };
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            switch (p_39285_) {
                case 0:
                    TileTeleporter.this.electronicPower = p_39286_;
                case 1:
                    TileTeleporter.this.teleportX = p_39286_;
                case 2:
                    TileTeleporter.this.teleportY = p_39286_;
                case 3:
                    TileTeleporter.this.teleportZ = p_39286_;
                case 4:
                    TileTeleporter.this.autoDeletion = (p_39286_ == 1);
                case 5:
                    TileTeleporter.this.itemTeleportX = p_39286_;
                case 6:
                    TileTeleporter.this.itemTeleportY = p_39286_;
                case 7:
                    TileTeleporter.this.itemTeleportZ = p_39286_;
                case 8:
                    TileTeleporter.this.isTeleportSaver = (p_39286_ == 1);
                case 9:
                    TileTeleporter.this.isTeleporter = (p_39286_ == 1);
            }
        }

        @Override
        public int getCount() {
            return 10;
        }
    };

    public TileTeleporter(BlockPos pos, BlockState state) {
        super(TILE_TELEPORTER.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.teleporter.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("teleporter");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new TeleporterContainer(id, player, this, dataAccess);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tag
        this.getTileData().putInt("MaxStorage", 2000);

        if (world == null) return;
        ItemStack stack = inventory.getStackInSlot(0);
        if ((stack.getItem() == ItemInit.TELEPORT_SAVER.get()) || (stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get())) {
            this.itemTeleportX = stack.getOrCreateTag().getInt("teleportX");
            this.itemTeleportY = stack.getOrCreateTag().getInt("teleportY");
            this.itemTeleportZ = stack.getOrCreateTag().getInt("teleportZ");
            this.isTeleportSaver = stack.getItem() == ItemInit.TELEPORT_SAVER.get();
            this.isTeleporter = stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get();
        }

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public double getTeleportX() {
        return this.teleportX;
    }

    public void setTeleportX(double teleportX) {
        this.teleportX = teleportX;
    }

    public double getTeleportY() {
        return this.teleportY;
    }

    public void setTeleportY(double teleportY) {
        this.teleportY = teleportY;
    }

    public double getTeleportZ() {
        return this.teleportZ;
    }

    public void setTeleportZ(double teleportZ) {
        this.teleportZ = teleportZ;
    }

    public boolean isAutoDeletion() {
        return this.autoDeletion;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.teleportX = compound.getDouble("teleportX");
        this.teleportY = compound.getDouble("teleportY");
        this.teleportZ = compound.getDouble("teleportZ");
        this.autoDeletion = compound.getBoolean("autoDeletion");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putDouble("teleportX", this.teleportX);
        compound.putDouble("teleportY", this.teleportY);
        compound.putDouble("teleportZ", this.teleportZ);
        compound.putBoolean("autoDeletion", this.autoDeletion);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
