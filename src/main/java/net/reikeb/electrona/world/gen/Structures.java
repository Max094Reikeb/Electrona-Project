package net.reikeb.electrona.world.gen;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.structures.RuinsStructure;

public class Structures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Electrona.MODID);

    public static final RegistryObject<StructureFeature<?>> RUINS = DEFERRED_REGISTRY_STRUCTURE.register("ruins", RuinsStructure::new);
}
