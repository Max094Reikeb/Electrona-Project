package net.reikeb.electrona.villages;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.SoundsInit;

import java.util.Set;
import java.util.function.Supplier;

public class Villagers {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Electrona.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, Electrona.MODID);
    public static final RegistryObject<PoiType> ENGINEER_POI = POI.register("engineer",
            () -> new PoiType("engineer", getAllStates(BlockInit.COMPRESSOR.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> ENGINEER = registerProfession("engineer", Villagers.ENGINEER_POI);

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<PoiType> poiType) {
        return PROFESSIONS.register(name, () -> new ElectronaVillagerProfessions(Electrona.MODID + ":" + name, poiType.get(), ImmutableSet.of(),
                ImmutableSet.of(), SoundsInit.COMPRESSOR_END_COMPRESSION));
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
