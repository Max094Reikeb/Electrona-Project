package net.reikeb.electrona.gempower;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.init.GemInit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class GemObject extends ForgeRegistryEntry<GemObject> {

    private final String name;
    private final ImmutableList<Supplier<GemPowerInstance>> gemPowerInstances;

    @SafeVarargs
    public GemObject(Supplier<GemPowerInstance>... gemPowerInstances) {
        this(null, gemPowerInstances);
    }

    @SafeVarargs
    public GemObject(@Nullable String name, Supplier<GemPowerInstance>... gemPowerInstances) {
        this.name = name;
        this.gemPowerInstances = ImmutableList.copyOf(gemPowerInstances);
    }

    public static GemObject byName(String name) {
        return GemInit.GEM_REGISTRY.get().getValue(ResourceLocation.tryParse(name));
    }

    public boolean isIn(Tag<GemObject> tag) {
        return tag.getValues().contains(this);
    }

    public String getName(String name) {
        if (GemInit.GEM_REGISTRY.get().getKey(this) == null) return "";
        return name + (this.name == null ? GemInit.GEM_REGISTRY.get().getKey(this).getPath() : this.name);
    }

    public List<Supplier<GemPowerInstance>> getPowers() {
        return this.gemPowerInstances;
    }
}
