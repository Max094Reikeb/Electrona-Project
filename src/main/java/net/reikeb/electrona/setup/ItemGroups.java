package net.reikeb.electrona.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemGroups {

    public static final CreativeModeTab ELECTRONA_MACHINES = new CreativeModeTab("electrona_machines") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.SOLAR_PANEL_T_1.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };

    public static final CreativeModeTab ELECTRONA_BLOCKS = new CreativeModeTab("electrona_blocks") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.TIN_ORE.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };

    public static final CreativeModeTab ELECTRONA_ITEMS = new CreativeModeTab("electrona_items") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.PORTABLE_BATTERY.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };

    public static final CreativeModeTab ELECTRONA_TOOLS = new CreativeModeTab("electrona_tools") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.HAMMER.get());
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };
}
