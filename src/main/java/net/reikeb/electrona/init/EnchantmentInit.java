package net.reikeb.electrona.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.enchantments.Lumberjack;
import net.reikeb.electrona.enchantments.Smelting;
import net.reikeb.electrona.enchantments.Thundering;
import net.reikeb.electrona.enchantments.Veinminer;

public class EnchantmentInit {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
            Electrona.MODID);

    public static final RegistryObject<Enchantment> LUMBERJACK = ENCHANTMENTS.register("lumberjack", () -> new Lumberjack(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> VEINMINER = ENCHANTMENTS.register("veinminer", () -> new Veinminer(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> THUNDERING = ENCHANTMENTS.register("thundering", () -> new Thundering(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SMELTING = ENCHANTMENTS.register("smelting", () -> new Smelting(EquipmentSlot.MAINHAND));
}
