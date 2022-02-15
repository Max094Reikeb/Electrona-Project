package net.reikeb.electrona.utils;

import net.minecraft.nbt.CompoundTag;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class FluidTankHandler extends FluidTank {

    public FluidTankHandler(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("FluidName", this.fluid.getFluid().getRegistryName().toString());
        nbt.putInt("Amount", this.fluid.getAmount());
        return nbt;
    }
}
