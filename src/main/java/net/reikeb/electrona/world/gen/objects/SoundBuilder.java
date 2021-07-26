package net.reikeb.electrona.world.gen.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.reikeb.electrona.Electrona;

/**
 * Created by 50ap5ud5 26 March 2021
 * <br> A builder wrapped class that allows you to serialise a Sound ogg file to json with customisable settings
 * <br> To be used in conjunction with {@linkplain SoundEventBuilder}
 */
public class SoundBuilder {

    private boolean nonDefault = false;

    private final String path;
    private float volume = 1;
    private float pitch = 1;
    private int weight = 1;
    private boolean stream = false;
    private int attenuationDistance = 16;
    private boolean preloadSound = false;
    private Sound.Type type = Sound.Type.FILE;

    public SoundBuilder(String path) {
        this.path = Electrona.MODID + ":" + path;
    }

    public SoundBuilder(ResourceLocation path) {
        this.path = path.toString();
    }

    public SoundBuilder volume(float volume) {
        this.volume = Mth.clamp(volume, 0, 1);
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder pitch(float pitch) {
        this.pitch = pitch;
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder weight(int weight) {
        this.weight = Math.max(weight, 0);
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder stream() {
        this.stream = true;
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder attenuationDistance(int distance) {
        this.attenuationDistance = distance;
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder preloaded() {
        this.preloadSound = true;
        this.nonDefault = true;

        return this;
    }

    public SoundBuilder isReference() {
        this.type = Sound.Type.SOUND_EVENT;
        this.nonDefault = true;

        return this;
    }

    public JsonElement toJson() {
        if (!nonDefault)
            return new JsonPrimitive(path);

        JsonObject soundObject = new JsonObject();

        soundObject.addProperty("name", path);

        if (volume != 1)
            soundObject.addProperty("volume", volume);

        if (pitch != 1)
            soundObject.addProperty("pitch", pitch);

        if (weight != 1)
            soundObject.addProperty("weight", weight);

        if (stream)
            soundObject.addProperty("stream", true);

        if (attenuationDistance != 16)
            soundObject.addProperty("attenuation_distance", attenuationDistance);

        if (preloadSound)
            soundObject.addProperty("preload", true);

        if (type != Sound.Type.FILE)
            soundObject.addProperty("type", "event");

        return soundObject;
    }
}
