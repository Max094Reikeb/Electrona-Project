package net.reikeb.electrona.villages;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.SoundsInit;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Villagers {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Electrona.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, Electrona.MODID);
    public static final RegistryObject<PoiType> ENGINEER_POI = POI.register("engineer",
            () -> new PoiType("engineer", getAllStates(BlockInit.COMPRESSOR.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> ENGINEER = registerProfession("engineer", Villagers.ENGINEER_POI, SoundsInit.COMPRESSOR_END_COMPRESSION);

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<PoiType> poiType, Supplier<SoundEvent> professionSound) {
        return PROFESSIONS.register(name, () -> new VillagerProfessions(Electrona.MODID + ":" + name, poiType.get(), ImmutableSet.of(),
                ImmutableSet.of(), professionSound));
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    public static class VillagerProfessions extends VillagerProfession {
        private final List<Supplier<SoundEvent>> soundEventSuppliers;

        @SafeVarargs
        public VillagerProfessions(String nameIn, PoiType pointOfInterestIn, ImmutableSet<Item> specificItemsIn,
                                   ImmutableSet<Block> relatedWorldBlocksIn, Supplier<SoundEvent>... soundEventSuppliers) {
            super(nameIn, pointOfInterestIn, specificItemsIn, relatedWorldBlocksIn, null);
            this.soundEventSuppliers = Arrays.asList(soundEventSuppliers);
        }

        @Nullable
        @Override
        public SoundEvent getWorkSound() {
            int n = ThreadLocalRandom.current().nextInt(soundEventSuppliers.size());
            return soundEventSuppliers.get(n).get();
        }
    }
}
