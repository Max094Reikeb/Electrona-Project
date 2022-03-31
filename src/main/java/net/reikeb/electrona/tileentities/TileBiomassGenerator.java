package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_BIOMASS_GENERATOR;

public class TileBiomassGenerator extends AbstractTileEntity {

    public static final BlockEntityTicker<TileBiomassGenerator> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private int wait;

    public TileBiomassGenerator(BlockPos pos, BlockState state) {
        super(TILE_BIOMASS_GENERATOR.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.biomass_generator.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("biomass_generator");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new BiomassGeneratorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        t.getTileData().putInt("MaxStorage", 3000);
        double electronicPower = t.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // Handle slot
            if (ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:biomass").toLowerCase(java.util.Locale.ENGLISH)))
                    .contains(this.inventory.getStackInSlot(0).getItem()) && electronicPower < 3000) {
                wait += 1;
                if (wait >= 20) {
                    if (electronicPower <= 2990) {
                        t.getTileData().putDouble("ElectronicPower", electronicPower + 10);
                    } else {
                        t.getTileData().putDouble("ElectronicPower", 3000);
                    }
                    this.inventory.decrStackSize(0, 1);
                    world.playSound(null, blockPos, SoundsInit.BIOMASS_GENERATOR_ACTIVE.get(),
                            SoundSource.BLOCKS, 0.6F, 1.0F);
                    wait = 0;
                }
            } else {
                wait = 0;
            }

            // Transfer energy
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), t.getTileData(), 3, electronicPower, true);

            t.setChanged();
            world.sendBlockUpdated(blockPos, t.getBlockState(), t.getBlockState(), 3);
        }
    }

    public double getElectronicPower() {
        return electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
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
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
