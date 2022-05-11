package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.blocks.LeadCrate;
import fr.firstmegagame4.electrona.blocks.SteelCrate;
import fr.firstmegagame4.mega_lib.lib.blocks.ConditionalCustomBlock;
import fr.firstmegagame4.mega_lib.lib.blocks.CustomBlock;
import fr.firstmegagame4.mega_lib.lib.initialization.BlocksInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class Blocks implements BlocksInitializer {

    public static final ConditionalCustomBlock TIN_ORE = new ConditionalCustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final ConditionalCustomBlock LEAD_ORE = new ConditionalCustomBlock(FabricBlockSettings.of(Material.STONE)
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
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final ConditionalCustomBlock TIN_BLOCK = new ConditionalCustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final ConditionalCustomBlock LEAD_BLOCK = new ConditionalCustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final ConditionalCustomBlock STEEL_BLOCK = new ConditionalCustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS
    );

    public static final CustomBlock GRAVITONIUM_BLOCK = new CustomBlock(FabricBlockSettings.of(Material.STONE)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS);

    public static final CustomBlock LEAD_CRATE = new LeadCrate(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS);

    public static final CustomBlock STEEL_CRATE = new SteelCrate(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_BLOCKS);

    public static final CustomBlock COMPRESSOR = new CustomBlock(FabricBlockSettings.of(Material.METAL)
            .hardness(5.0F)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool(),
            true,
            Tabs.ELECTRONA_MACHINES);

    public void register() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            TIN_ORE.create().register(Utils.ELIdentifier("tin_ore"));
            LEAD_ORE.create().register(Utils.ELIdentifier("lead_ore"));

            TIN_BLOCK.create().register(Utils.ELIdentifier("tin_block"));
            LEAD_BLOCK.create().register(Utils.ELIdentifier("lead_block"));
            STEEL_BLOCK.create().register(Utils.ELIdentifier("steel_block"));
        }

        URANIUM_ORE.register(Utils.ELIdentifier("uranium_ore"));
        GRAVITONIUM_ORE.register(Utils.ELIdentifier("gravitonium_ore"));

        GRAVITONIUM_BLOCK.register(Utils.ELIdentifier("gravitonium_block"));

        LEAD_CRATE.register(Utils.ELIdentifier("lead_crate"));
        STEEL_CRATE.register(Utils.ELIdentifier("steel_crate"));

        COMPRESSOR.register(Utils.ELIdentifier("compressor"));
    }

}
