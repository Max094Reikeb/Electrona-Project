package net.reikeb.electrona.villages;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * This gets around the fact that sound events might not be registered by the
 * time the villager profession is.
 */
public class ElectronaVillagerProfessions extends VillagerProfession {
    private final List<Supplier<SoundEvent>> soundEventSuppliers;

    @SafeVarargs
    public ElectronaVillagerProfessions(String nameIn, PoiType pointOfInterestIn, ImmutableSet<Item> specificItemsIn,
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
