package net.reikeb.electrona;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.events.entity.EntityDiesEvent;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.electrona.recipes.types.RecipeTypeCompressor;
import net.reikeb.electrona.recipes.types.RecipeTypePurificator;
import net.reikeb.electrona.setup.RegistryHandler;
import net.reikeb.electrona.villages.POIFixup;
import net.reikeb.electrona.villages.StructureGen;
import net.reikeb.electrona.villages.Villagers;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.gen.ConfiguredStructures;
import net.reikeb.electrona.world.gen.Structures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod(Electrona.MODID)
public class Electrona {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Register the modid
    public static final String MODID = "electrona";

    // Creates a new recipe type. This is used for storing recipes in the map, and looking them up.
    public static final RecipeType<CompressorRecipe> COMPRESSING = new RecipeTypeCompressor();
    public static final RecipeType<PurificatorRecipe> PURIFYING = new RecipeTypePurificator();

    public Electrona() {

        // Init the RegistryHandler class
        RegistryHandler.init();

        // Init Advancements
        TTriggers.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
        Structures.DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
        registerAllDeferredRegistryObjects(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new EntityDiesEvent());
        MinecraftForge.EVENT_BUS.register(new Gamerules());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
        MinecraftForge.EVENT_BUS.addListener(this::setupEngineerHouses);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation RL(String path) {
        return new ResourceLocation(MODID, path);
    }

    /**
     * This is the event you will use to add anything to any biome.
     * This includes spawns, changing the biome's looks, messing with its surfacebuilders,
     * adding carvers, spawning new features... etc
     * <p>
     * Here, we will use this to add our structure to all biomes.
     */
    public void biomeModification(final BiomeLoadingEvent event) {
        if (event.getName().equals(RL("nuclear"))) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_RUINS);
        }
    }

    private static Method GETCODEC_METHOD;

    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) event.getWorld();

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                Electrona.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if (serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverWorld.dimension().equals(Level.OVERWORLD)) {
                return;
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(Structures.RUINS.get(), StructureSettings.DEFAULTS.get(Structures.RUINS.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    private void registerAllDeferredRegistryObjects(IEventBus modBus) {
        Villagers.POI.register(modBus);
        Villagers.PROFESSIONS.register(modBus);
    }

    /*
      Add to Village pools in FMLServerAboutToStartEvent so Engineer houses shows up in Villages modified by datapacks.
     */
    public void setupEngineerHouses(FMLServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    public void setup(final FMLCommonSetupEvent event) {
        POIFixup.fixup();

        /**
         * Register biomes
         */
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(ResourceKey.create(Registry.BIOME_REGISTRY,
                RL("nuclear")), 5));

        /**
         * Custom potion recipes
         */
        // Sugar Bottle recipe
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(Items.SUGAR),
                new ItemStack(ItemInit.SUGAR_BOTTLE.get()));
        // Concentrated Uranium recipe
        BrewingRecipeRegistry.addRecipe(Ingredient.of(ItemInit.SUGAR_BOTTLE.get()), Ingredient.of(ItemInit.YELLOWCAKE.get()),
                new ItemStack(ItemInit.CONCENTRATED_URANIUM.get()));

        /**
         * Register structures
         */
        event.enqueueWork(() -> {
            Structures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });
    }

    private void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {

        // Vanilla has a registry for recipe types, but it does not actively use this registry.
        // While this makes registering your recipe type an optional step, I recommend
        // registering it anyway to allow other mods to discover your custom recipe types.
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(COMPRESSING.toString()), COMPRESSING);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(PURIFYING.toString()), PURIFYING);

        // Register the recipe serializer. This handles from json, from packet, and to packet.
        event.getRegistry().register(CompressorRecipe.SERIALIZER);
        event.getRegistry().register(PurificatorRecipe.SERIALIZER);
    }
}
