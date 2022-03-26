package fr.firstmegagame4.electrona.setup;

import fr.firstmegagame4.electrona.init.Blocks;
import fr.firstmegagame4.electrona.init.Items;
import fr.firstmegagame4.electrona.utils.Utils;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class Tabs {

    public static final ItemGroup ELECTRONA_MACHINES = FabricItemGroupBuilder.create(
            Utils.electronaIdentifier("machines"))
            .icon(() -> new ItemStack(Blocks.GRAVITONIUM_BLOCK_ITEM))
            .build();

    public static final ItemGroup ELECTRONA_BLOCKS = FabricItemGroupBuilder.create(
            Utils.electronaIdentifier("blocks"))
            .icon(() -> new ItemStack(Blocks.STEEL_BLOCK_ITEM))
            .build();

    public static final ItemGroup ELECTRONA_ITEMS = FabricItemGroupBuilder.create(
            Utils.electronaIdentifier("items"))
            .icon(() -> new ItemStack(Items.LEAD_INGOT))
            .build();

    public static final ItemGroup ELECTRONA_TOOLS = FabricItemGroupBuilder.create(
            Utils.electronaIdentifier("tools"))
            .icon(() -> new ItemStack(Items.STEEL_PICKAXE))
            .build();

}