package net.reikeb.electrona.gempower;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.GemPowerInit;

import javax.annotation.Nullable;
import javax.print.attribute.Attribute;
import java.util.Map;

public class GemPower extends ForgeRegistryEntry<GemPower> {

    private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();
    private final int cooldown;
    @Nullable
    private String descriptionId;

    public GemPower(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("power", GemPowerInit.GEM_POWER_REGISTRY.get().getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
    }

    @Nullable
    public static GemPower byId(String id) {
        return GemPowerInit.GEM_POWER_REGISTRY.get().getValue(Electrona.RL(id));
    }

    public static String getId(GemPower gemPower) {
        return GemPowerInit.GEM_POWER_REGISTRY.get().toString();
    }
}