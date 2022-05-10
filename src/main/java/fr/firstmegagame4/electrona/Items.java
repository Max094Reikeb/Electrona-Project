package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.armormaterials.LeadArmorMaterial;
import fr.firstmegagame4.electrona.items.CustomAxeItem;
import fr.firstmegagame4.electrona.items.CustomHoeItem;
import fr.firstmegagame4.electrona.items.CustomPickaxeItem;
import fr.firstmegagame4.electrona.toolmaterials.SteelToolMaterial;
import fr.firstmegagame4.mega_lib.lib.initialization.ItemsInitializer;
import fr.firstmegagame4.mega_lib.lib.item.CustomItem;
import fr.firstmegagame4.mega_lib.lib.utils.RegistrationUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

public class Items implements ItemsInitializer {

    public static CustomItem RAW_TIN;
    public static CustomItem TIN_INGOT;
    public static CustomItem RAW_LEAD;
    public static CustomItem LEAD_INGOT;
    public static CustomItem STEEL_INGOT;
    public static final CustomItem GRAVITONIUM = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));

    public static final ToolItem STEEL_SWORD = new SwordItem(
            SteelToolMaterial.INSTANCE, 3, -2.4F, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS)
    );
    public static final ToolItem STEEL_PICKAXE = new CustomPickaxeItem(
            SteelToolMaterial.INSTANCE, 1, -2.8F, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS)
    );
    public static final ToolItem STEEL_AXE = new CustomAxeItem(
            SteelToolMaterial.INSTANCE, 1, -3.0F, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS)
    );
    public static final ToolItem STEEL_SHOVEL = new ShovelItem(
            SteelToolMaterial.INSTANCE, 1, -3.0F, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS)
    );
    public static final ToolItem STEEL_HOE = new CustomHoeItem(
            SteelToolMaterial.INSTANCE, 0, 0.0F, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS)
    );

    public static final Item LEAD_HELMET = new ArmorItem(LeadArmorMaterial.INSTANCE, EquipmentSlot.HEAD, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_CHESTPLATE = new ArmorItem(LeadArmorMaterial.INSTANCE, EquipmentSlot.CHEST, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_LEGGINGS = new ArmorItem(LeadArmorMaterial.INSTANCE, EquipmentSlot.LEGS, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_BOOTS = new ArmorItem(LeadArmorMaterial.INSTANCE, EquipmentSlot.FEET, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));

    public void register() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            RAW_TIN = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
            RAW_TIN.register(Utils.ELIdentifier("raw_tin"));

            TIN_INGOT = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
            TIN_INGOT.register(Utils.ELIdentifier("tin_ingot"));

            RAW_LEAD = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
            RAW_LEAD.register(Utils.ELIdentifier("raw_lead"));

            LEAD_INGOT = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
            LEAD_INGOT.register(Utils.ELIdentifier("lead_ingot"));

            STEEL_INGOT = new CustomItem(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
            STEEL_INGOT.register(Utils.ELIdentifier("steel_ingot"));
        }

        GRAVITONIUM.register(Utils.ELIdentifier("gravitonium"));

        RegistrationUtils.registerItem(Utils.ELIdentifier("steel_sword"), Items.STEEL_SWORD);
        RegistrationUtils.registerItem(Utils.ELIdentifier("steel_pickaxe"), Items.STEEL_PICKAXE);
        RegistrationUtils.registerItem(Utils.ELIdentifier("steel_axe"), Items.STEEL_AXE);
        RegistrationUtils.registerItem(Utils.ELIdentifier("steel_shovel"), Items.STEEL_SHOVEL);
        RegistrationUtils.registerItem(Utils.ELIdentifier("steel_hoe"), Items.STEEL_HOE);

        RegistrationUtils.registerItem(Utils.ELIdentifier("lead_helmet"), Items.LEAD_HELMET);
        RegistrationUtils.registerItem(Utils.ELIdentifier("lead_chestplate"), Items.LEAD_CHESTPLATE);
        RegistrationUtils.registerItem(Utils.ELIdentifier("lead_leggings"), Items.LEAD_LEGGINGS);
        RegistrationUtils.registerItem(Utils.ELIdentifier("lead_boots"), Items.LEAD_BOOTS);
    }

}
