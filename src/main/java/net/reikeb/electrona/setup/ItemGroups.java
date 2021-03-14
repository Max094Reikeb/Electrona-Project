package net.reikeb.electrona.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemGroups {

    public static final ItemGroup ELECTRONA_MACHINES = new ItemGroup("tabelectronamachines") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegistryHandler.SOLAR_PANEL_T_1.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };

    public static final ItemGroup ELECTRONA_BLOCKS = new ItemGroup("tabelectronablocks") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegistryHandler.TIN_ORE.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };

    public static final ItemGroup ELECTRONA_ITEMS = new ItemGroup("tabelectronaitem") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegistryHandler.PORTABLE_BATTERY.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };
}
