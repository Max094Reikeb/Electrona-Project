package net.reikeb.electrona.world.gen.objects;

import com.google.gson.*;

import net.minecraft.util.*;

import java.util.*;

/**
 * Created by 50ap5ud5 29 March 2021
 * <br> A builder function that allows you to serialise a SoundEvent to json
 */
public class SoundEventBuilder {
    private SoundCategory category = SoundCategory.BLOCKS;
    private String subtitleTranslationKey = "";
    private final List<SoundBuilder> sounds = new ArrayList<>();
    private final SoundEvent event;

    public SoundEventBuilder(SoundEvent event) {
        this.event = event;
    }

    public SoundEventBuilder withCategory(SoundCategory category) {
        this.category = category;
        return this;
    }

    public SoundEventBuilder withSpecificSubtitle(String subtitleTranslationKey) {
        this.subtitleTranslationKey = subtitleTranslationKey;
        return this;
    }

    public SoundEventBuilder withSubtitle() {
        this.subtitleTranslationKey = this.getTranslationKey(this.event);
        return this;
    }

    public SoundEventBuilder withSound(SoundBuilder sound) {
        this.sounds.add(sound);
        return this;
    }

    public String getTranslationKey(SoundEvent sound) {
        if (subtitleTranslationKey == null || subtitleTranslationKey.isEmpty()) {
            this.subtitleTranslationKey = Util.makeDescriptionId("subtitle", sound.getRegistryName());
        }
        return this.subtitleTranslationKey;
    }

    public JsonElement toJson() {
        JsonObject soundObject = new JsonObject();
        soundObject.addProperty("subtitle", this.subtitleTranslationKey);
        JsonArray soundList = new JsonArray();
        for (SoundBuilder sound : this.sounds) {
            soundList.add(sound.toJson());
        }
        soundObject.add("sounds", soundList);
        return soundObject;
    }

}
