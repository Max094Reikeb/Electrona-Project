package net.reikeb.electrona.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.enchantments.*;

public class EnchantmentInit {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
            Electrona.MODID);

    public static final RegistryObject<Enchantment> LUMBERJACK = ENCHANTMENTS.register("lumberjack", () -> new Lumberjack(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> VEINMINER = ENCHANTMENTS.register("veinminer", () -> new Veinminer(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> THUNDERING = ENCHANTMENTS.register("thundering", () -> new Thundering(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SMELTING = ENCHANTMENTS.register("smelting", () -> new Smelting(EquipmentSlotType.MAINHAND));
}
