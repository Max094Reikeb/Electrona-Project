package fr.firstmegagame4.electrona;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class Tabs {

    public static final ItemGroup ELECTRONA_MACHINES = FabricItemGroupBuilder.create(
            Utils.ELIdentifier("machines"))
            .icon(() -> new ItemStack(Blocks.COMPRESSOR.getItem()))
            .build();

    public static final ItemGroup ELECTRONA_BLOCKS = FabricItemGroupBuilder.create(
            Utils.ELIdentifier("blocks"))
            .icon(() -> new ItemStack(Blocks.STEEL_BLOCK.getIfCreatedOrElse(Blocks.GRAVITONIUM_BLOCK).getItem()))
            .build();

    public static final ItemGroup ELECTRONA_ITEMS = FabricItemGroupBuilder.create(
            Utils.ELIdentifier("items"))
            .icon(() -> new ItemStack(Items.LEAD_INGOT.getIfCreatedOrElse(Items.GRAVITONIUM)))
            .build();

    public static final ItemGroup ELECTRONA_TOOLS = FabricItemGroupBuilder.create(
            Utils.ELIdentifier("tools"))
            .icon(() -> new ItemStack(Items.STEEL_PICKAXE))
            .build();

}