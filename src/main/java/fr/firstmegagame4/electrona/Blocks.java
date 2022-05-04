package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.blocks.LeadCrate;
import fr.firstmegagame4.electrona.blocks.SteelCrate;

import fr.firstmegagame4.mega_lib.lib.blocks.CustomBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;

public class Blocks {

    public static final CustomBlock TIN_ORE = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final CustomBlock LEAD_ORE = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final CustomBlock URANIUM_ORE = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final CustomBlock GRAVITONIUM_ORE = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool());
    public static final BlockItem GRAVITONIUM_ORE_ITEM = new BlockItem(GRAVITONIUM_ORE,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final CustomBlock TIN_BLOCK = new CustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem TIN_BLOCK_ITEM = new BlockItem(TIN_BLOCK,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final CustomBlock LEAD_BLOCK = new CustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem LEAD_BLOCK_ITEM = new BlockItem(LEAD_BLOCK,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final CustomBlock STEEL_BLOCK = new CustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem STEEL_BLOCK_ITEM = new BlockItem(STEEL_BLOCK,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final CustomBlock GRAVITONIUM_BLOCK = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem GRAVITONIUM_BLOCK_ITEM = new BlockItem(GRAVITONIUM_BLOCK,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final Block LEAD_CRATE = new LeadCrate(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem LEAD_CRATE_ITEM = new BlockItem(LEAD_CRATE,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final Block STEEL_CRATE = new SteelCrate(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem STEEL_CRATE_ITEM = new BlockItem(STEEL_CRATE,
            new FabricItemSettings().group(Tabs.ELECTRONA_BLOCKS));

    public static final Block COMPRESSOR = new Block(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
    public static final BlockItem COMPRESSOR_ITEM = new BlockItem(COMPRESSOR,
            new FabricItemSettings().group(Tabs.ELECTRONA_MACHINES));

}
