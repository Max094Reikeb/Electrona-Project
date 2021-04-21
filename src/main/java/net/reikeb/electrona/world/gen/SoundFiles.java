package net.reikeb.electrona.world.gen;

import com.google.gson.*;

import net.minecraft.data.*;
import net.minecraft.util.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.world.gen.objects.*;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Credits: 50ap5ud5
 * <br> A Data generator that allows us to automatically add new objects to the sounds.json without needing manual work
 */
public class SoundFiles implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private JsonObject root = new JsonObject();

    public SoundFiles(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(DirectoryCache cache) throws IOException {

        final Path path = this.generator.getOutputFolder();
        final Path outputPath = getPath(path, Electrona.MODID);

        this.addSound(SoundsInit.BIOMASS_GENERATOR_ACTIVE.get(), "electrona/block.biomass_generator.active");
        this.addSound(SoundsInit.COMPRESSOR_END_COMPRESSION.get(), "electrona/block.compressor.compression_end");
        this.addSound(SoundsInit.NUCLEAR_EXPLOSION.get(), "electrona/common.nuclear_explosion");
        this.addSound(SoundsInit.NUCLEAR_GENERATOR_CONTROLLER_ALERT.get(), "electrona/block.nuclear_generator_controller.alert");
        this.addSound(SoundsInit.PURIFICATOR_PURIFICATION.get(), "electrona/block.purificator.purification");
        this.addSound(SoundsInit.WATER_PUMPING.get(), "electrona/block.water_pump.pump");

        //Save the json object to a file after we have added everything
        IDataProvider.save(GSON, cache, this.root, outputPath);

    }

    /**
     * Adds a sound object with multiple sound files that have the default settings
     *
     * @param soundEvent - the {@linkplain SoundEvent} to be added to the sounds.json file
     * @param soundPaths - Define which sound files can be played for this SoundEvent.
     *                   <br> This is the path of where the ogg file is stored.
     *                   <br> The Tardis Modid is already set here
     *                   <br> E.g. If a sound is in assets/tardis/sounds/tardis/tardis_takeoff.ogg,
     *                   <br> the path is tardis/tardis_takeoff because the Tardis modid is already passed in
     */
    public void addSound(SoundEvent soundEvent, String... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withSubtitle();
        for (String resourcePath : soundPaths) {
            builder.withSound(new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    /**
     * Adds a sound object with a specific subtitle.
     * <br> Used when we want to reuse a specific subtitle
     *
     * @param soundEvent
     * @param subtitle
     * @param soundPaths
     */
    public void addSoundWithSubtitle(SoundEvent soundEvent, String subtitle, String... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withSpecificSubtitle(subtitle);
        for (String resourcePath : soundPaths) {
            builder.withSound(new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    public void addSound(SoundEvent soundEvent, boolean stream, String... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withSubtitle();
        for (String resourcePath : soundPaths) {
            builder.withSound(stream ? new SoundBuilder(resourcePath).stream() : new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    /**
     * Add a sound object with streaming enabled and specific subtitle
     *
     * @param soundEvent
     * @param subtitle
     * @param stream
     * @param soundPaths
     */
    public void addSoundWithSubtitle(SoundEvent soundEvent, String subtitle, boolean stream, String... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withSpecificSubtitle(subtitle);
        for (String resourcePath : soundPaths) {
            builder.withSound(stream ? new SoundBuilder(resourcePath).stream() : new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    /**
     * Adds a sound object with multiple sound files that have the default settings
     *
     * @param soundEvent - the {@linkplain SoundEvent} to be added to the sounds.json file
     * @param category   - the {@linkplain SoundCategory}
     * @param soundPaths - Define which sound files can be played for this SoundEvent.
     *                   <br> This is the path of where the ogg file is stored.
     *                   <br> The Tardis Modid is already set here
     *                   <br> E.g. If a sound is in assets/tardis/sounds/tardis/tardis_takeoff.ogg,
     *                   <br> the path is tardis/tardis_takeoff because the Tardis modid is already passed in
     */
    public void addSound(SoundEvent soundEvent, SoundCategory category, String... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withCategory(category).withSubtitle();
        for (String resourcePath : soundPaths) {
            builder.withSound(new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    /**
     * Adds a sound object with multiple sound files that have the default settings
     *
     * @param soundEvent - the {@linkplain SoundEvent} to be added to the sounds.json file
     * @param category   - the {@linkplain SoundCategory}
     * @param soundPaths - Define which sound files can be played for this SoundEvent.
     *                   <br> This is the path of where the ogg file is stored.
     *                   <br> E.g. If a sound is in assets/tardis/sounds/tardis/tardis_takeoff.ogg,
     *                   <br> the path is tardis:tardis/tardis_takeoff
     */
    public void addSound(SoundEvent soundEvent, SoundCategory category, ResourceLocation... soundPaths) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withCategory(category).withSubtitle();
        for (ResourceLocation resourcePath : soundPaths) {
            builder.withSound(new SoundBuilder(resourcePath));
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }

    /**
     * Adds a sound object with multiple sound objects that have can have variable settings.
     *
     * @param soundEvent    - the {@linkplain SoundEvent} to be added to the sounds.json file
     * @param category      - the {@linkplain SoundCategory}
     * @param soundBuilders - An array of {@linkplain SoundBuilder}(s) to add to this sound object
     */
    public void addSound(SoundEvent soundEvent, SoundCategory category, SoundBuilder... soundBuilders) {
        SoundEventBuilder builder = new SoundEventBuilder(soundEvent).withCategory(category).withSubtitle();
        for (SoundBuilder soundBuilder : soundBuilders) {
            builder.withSound(soundBuilder);
        }
        this.root.add(getSoundName(soundEvent), builder.toJson());
    }


    public static Path getPath(Path path, String modid) {
        return path.resolve("assets/" + modid + "/sounds/sounds" + ".json");
    }

    public String getSoundName(SoundEvent sound) {
        return sound.getRegistryName().getPath();
    }

    public String getTranslationKey(SoundEvent sound) {
        String subtitleTranslationKey = "";
        if (subtitleTranslationKey.isEmpty() || subtitleTranslationKey == null) {
            subtitleTranslationKey = Util.makeDescriptionId("subtitle", sound.getRegistryName());
        }
        return subtitleTranslationKey;
    }

    @Override
    public String getName() {
        return "Electrona Sound File Generator";
    }

}
