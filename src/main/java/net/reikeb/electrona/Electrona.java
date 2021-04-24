package net.reikeb.electrona;

import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.common.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.events.entity.PlayerDiesEvent;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.recipes.*;
import net.reikeb.electrona.recipes.types.*;
import net.reikeb.electrona.setup.RegistryHandler;
import net.reikeb.electrona.setup.client.ClientSetup;
import net.reikeb.electrona.setup.client.render.MechanicWingsLayer;
import net.reikeb.electrona.villages.*;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.gen.*;

import org.apache.logging.log4j.*;

import java.lang.reflect.Method;
import java.util.*;

@Mod(Electrona.MODID)
public class Electrona {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Register the modid
    public static final String MODID = "electrona";

    // Creates a new recipe type. This is used for storing recipes in the map, and looking them up.
    public static final IRecipeType<CompressorRecipe> COMPRESSING = new RecipeTypeCompressor();
    public static final IRecipeType<PurificatorRecipe> PURIFYING = new RecipeTypePurificator();

    public Electrona() {

        // Init the RegistryHandler class
        RegistryHandler.init();

        // Init Advancements
        TTriggers.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
        Structures.DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
        registerAllDeferredRegistryObjects(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new PlayerDiesEvent());
        MinecraftForge.EVENT_BUS.register(new Gamerules());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
        MinecraftForge.EVENT_BUS.addListener(this::setupEngineerHouses);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * This is the event you will use to add anything to any biome.
     * This includes spawns, changing the biome's looks, messing with its surfacebuilders,
     * adding carvers, spawning new features... etc
     * <p>
     * Here, we will use this to add our structure to all biomes.
     */
    public void biomeModification(final BiomeLoadingEvent event) {
        if (event.getName().equals(new ResourceLocation("electrona", "nuclear"))) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_RUINS);
        }
    }

    private static Method GETCODEC_METHOD;

    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                Electrona.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(Structures.RUINS.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.RUINS.get()));
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
        event.enqueueWork(ConfiguredFeatures::registerConfiguredFeatures);
        POIFixup.fixup();

        /**
         * Register biomes
         */
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation("electrona", "nuclear")), 5));

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

    public void clientLoad(FMLClientSetupEvent event) {
        // This is where the wings layer is registered.
        // It can be put in any event listener of FMLClientSetupEvent if you want, like
        // in the main mod class.
        EntityRendererManager dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        registerLayer(dispatcher, "default");
        registerLayer(dispatcher, "slim");
    }

    private static void registerLayer(EntityRendererManager dispatcher, String type) {
        PlayerRenderer playerRenderer = dispatcher.getSkinMap().get(type);
        playerRenderer.addLayer(new MechanicWingsLayer<>(playerRenderer));
    }

    private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {

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
