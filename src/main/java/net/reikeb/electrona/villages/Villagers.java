package net.reikeb.electrona.villages;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.*;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.village.PointOfInterestType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.init.*;

import java.util.function.Supplier;
import java.util.Set;

public class Villagers {
    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, "electrona");
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, "electrona");
    public static final RegistryObject<PointOfInterestType> ENGINEER_POI = POI.register("engineer",
            () -> new PointOfInterestType("engineer", getAllStates(BlockInit.COMPRESSOR.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> ENGINEER = registerProfession("engineer", Villagers.ENGINEER_POI);

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<PointOfInterestType> poiType) {
        return PROFESSIONS.register(name, () -> new ElectronaVillagerProfessions("electrona" + ":" + name, poiType.get(), ImmutableSet.of(),
                ImmutableSet.of(), SoundsInit.COMPRESSOR_END_COMPRESSION));
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
