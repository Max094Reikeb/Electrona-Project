package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.blockentities.LeadCrateEntity;
import fr.firstmegagame4.electrona.blockentities.SteelCrateEntity;
import fr.firstmegagame4.electrona.screens.CrateScreen;

import fr.firstmegagame4.mega_lib.lib.generation.ores.CustomOre;
import fr.firstmegagame4.mega_lib.lib.utils.RegistrationUtils;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.util.registry.Registry;

public class Registers {

    public static void registerItems() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            RegistrationUtils.registerItem(Utils.electronaIdentifier("raw_tin"), Items.RAW_TIN);
            RegistrationUtils.registerItem(Utils.electronaIdentifier("tin_ingot"), Items.TIN_INGOT);
            RegistrationUtils.registerItem(Utils.electronaIdentifier("raw_lead"), Items.RAW_LEAD);
            RegistrationUtils.registerItem(Utils.electronaIdentifier("lead_ingot"), Items.LEAD_INGOT);
            RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_ingot"), Items.STEEL_INGOT);
        }

        RegistrationUtils.registerItem(Utils.electronaIdentifier("gravitonium"), Items.GRAVITONIUM);

        RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_sword"), Items.STEEL_SWORD);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_pickaxe"), Items.STEEL_PICKAXE);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_axe"), Items.STEEL_AXE);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_shovel"), Items.STEEL_SHOVEL);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("steel_hoe"), Items.STEEL_HOE);

        RegistrationUtils.registerItem(Utils.electronaIdentifier("lead_helmet"), Items.LEAD_HELMET);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("lead_chestplate"), Items.LEAD_CHESTPLATE);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("lead_leggings"), Items.LEAD_LEGGINGS);
        RegistrationUtils.registerItem(Utils.electronaIdentifier("lead_boots"), Items.LEAD_BOOTS);
    }

    public static void registerBlocks() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            Blocks.TIN_ORE.register(Utils.electronaIdentifier("tin_ore"));
            Blocks.LEAD_ORE.register(Utils.electronaIdentifier("lead_ore"));

            Blocks.TIN_BLOCK.register(Utils.electronaIdentifier("tin_block"));
            Blocks.LEAD_BLOCK.register(Utils.electronaIdentifier("lead_block"));
            Blocks.STEEL_BLOCK.register(Utils.electronaIdentifier("steel_block"));
        }

        Blocks.URANIUM_ORE.register(Utils.electronaIdentifier("uranium_ore"));
        Blocks.GRAVITONIUM_ORE.register(Utils.electronaIdentifier("gravitonium_ore"));

        Blocks.GRAVITONIUM_BLOCK.register(Utils.electronaIdentifier("gravitonium_block"));

        Utils.registerBlock("lead_crate", Blocks.LEAD_CRATE, Blocks.LEAD_CRATE_ITEM);
        Utils.registerBlock("steel_crate", Blocks.STEEL_CRATE, Blocks.STEEL_CRATE_ITEM);

        Utils.registerBlock("compressor", Blocks.COMPRESSOR, Blocks.COMPRESSOR_ITEM);
    }

    public static void registerBlockEntities() {
        BlockEntities.STEEL_CRATE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                Utils.modIdentifier+":"+"steel_crate_entity",
                FabricBlockEntityTypeBuilder.create(SteelCrateEntity::new, Blocks.STEEL_CRATE).build(null)
        );
        BlockEntities.LEAD_CRATE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                Utils.modIdentifier+":"+"lead_crate_entity",
                FabricBlockEntityTypeBuilder.create(LeadCrateEntity::new, Blocks.LEAD_CRATE).build(null)
        );
    }

    public static void registerScreens() {
        ScreenRegistry.register(ScreenHandlers.STEEL_CRATE_SCREEN_HANDLER, CrateScreen::new);
        ScreenRegistry.register(ScreenHandlers.LEAD_CRATE_SCREEN_HANDLER, CrateScreen::new);
    }

    public static void registerOres() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            new CustomOre().setVeinSize(10).setNumVeins(5).setMinHeight(0).setMaxHeight(50).overworldOre().register(Blocks.TIN_ORE, "tin_ore");
            new CustomOre().setVeinSize(10).setNumVeins(6).setMinHeight(0).setMaxHeight(24).overworldOre().register(Blocks.LEAD_ORE, "lead_ore");
        }

        new CustomOre().setVeinSize(8).setNumVeins(6).setMinHeight(0).setMaxHeight(30).overworldOre().register(Blocks.URANIUM_ORE, "uranium_ore");
        new CustomOre().setVeinSize(6).setNumVeins(4).setMinHeight(0).setMaxHeight(50).enderOre().register(Blocks.GRAVITONIUM_ORE, "gravitonium_ore");
    }

}
