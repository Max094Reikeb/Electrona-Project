package net.reikeb.electrona;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.events.entity.EntityDiesEvent;
import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.GameEvents;
import net.reikeb.electrona.misc.Keys;
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
import net.reikeb.electrona.world.structures.RuinsStructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import terrablender.api.BiomeProviders;

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
    private static Method GETCODEC_METHOD;

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
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, RuinsStructure::setupStructureSpawns);
        MinecraftForge.EVENT_BUS.addListener(this::setupEngineerHouses);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation RL(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> STStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        STStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = STStructureToMultiMap.get(configuredStructureFeature.feature);
        if (configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            Electrona.LOGGER.error("""
                                Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                                This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                                The two conflicting ConfiguredStructures are: {}, {}
                                The biome that is attempting to be shared: {}
                            """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey
            );
        } else {
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }

    public void setup(final FMLCommonSetupEvent event) {
        POIFixup.fixup();

        // Register structures, biomes, game events, ...
        event.enqueueWork(() -> {
            Structures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
            GameEvents.setupGameEvents();

            BiomeProviders.register(new net.reikeb.electrona.world.gen.biomes.BiomeProvider(Keys.NUCLEAR_BIOME, 1));

            SculkSensorBlock.VIBRATION_STRENGTH_FOR_EVENT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(), (map) -> {
                map.putAll(SculkSensorBlock.VIBRATION_STRENGTH_FOR_EVENT);
                map.put(GameEvents.SINGULARITY, 7);
                map.put(GameEvents.TELEPORTER_USE, 10);
                map.put(GameEvents.ENERGETIC_LIGHTNING_STRIKE, 15);
            }));
        });

        /**
         * Custom potion recipes
         */
        // Sugar Bottle recipe
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(Items.SUGAR),
                new ItemStack(ItemInit.SUGAR_BOTTLE.get()));
        // Concentrated Uranium recipe
        BrewingRecipeRegistry.addRecipe(Ingredient.of(ItemInit.SUGAR_BOTTLE.get()), Ingredient.of(ItemInit.YELLOWCAKE.get()),
                new ItemStack(ItemInit.CONCENTRATED_URANIUM.get()));
    }

    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

            if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) return;

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();
            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> STStructureToMultiMap = new HashMap<>();

            ImmutableSet<ResourceKey<Biome>> overworldBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(BiomeInit.NUCLEAR).build();
            overworldBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(STStructureToMultiMap, ConfiguredStructures.CONFIGURED_RUINS, biomeKey));

            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !STStructureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

            STStructureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                Electrona.LOGGER.error("Was unable to check if " + serverLevel.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.putIfAbsent(Structures.RUINS.get(), StructureSettings.DEFAULTS.get(Structures.RUINS.get()));
            worldStructureConfig.structureConfig = tempMap;
        }
    }

    private void registerAllDeferredRegistryObjects(IEventBus modBus) {
        Villagers.POI.register(modBus);
        Villagers.PROFESSIONS.register(modBus);
    }

    /*
      Add to Village pools in FMLServerAboutToStartEvent so Engineer houses shows up in Villages modified by datapacks.
     */
    public void setupEngineerHouses(ServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
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
