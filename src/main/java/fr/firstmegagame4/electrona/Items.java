package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.items.CustomAxeItem;
import fr.firstmegagame4.electrona.items.CustomHoeItem;
import fr.firstmegagame4.electrona.items.CustomPickaxeItem;
import fr.firstmegagame4.electrona.toolmaterials.SteelToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

public class Items {

    public static final Item RAW_TIN = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
    public static final Item TIN_INGOT = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
    public static final Item RAW_LEAD = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
    public static final Item LEAD_INGOT = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
    public static final Item STEEL_INGOT = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));
    public static final Item GRAVITONIUM = new Item(new FabricItemSettings().group(Tabs.ELECTRONA_ITEMS));

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

    public static final Item LEAD_HELMET = new ArmorItem(ArmorMaterials.LEAD_ARMOR_MATERIAL, EquipmentSlot.HEAD, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_CHESTPLATE = new ArmorItem(ArmorMaterials.LEAD_ARMOR_MATERIAL, EquipmentSlot.CHEST, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_LEGGINGS = new ArmorItem(ArmorMaterials.LEAD_ARMOR_MATERIAL, EquipmentSlot.LEGS, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));
    public static final Item LEAD_BOOTS = new ArmorItem(ArmorMaterials.LEAD_ARMOR_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(Tabs.ELECTRONA_TOOLS));

}
