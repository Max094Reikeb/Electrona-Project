package net.reikeb.electrona;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.events.entity.EntityDiesEvent;
import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.GameEvents;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.electrona.setup.RegistryHandler;
import net.reikeb.electrona.villages.POIFixup;
import net.reikeb.electrona.villages.StructureGen;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.gen.biomes.SurfaceRuleData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

@Mod(Electrona.MODID)
public class Electrona {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Register the modid
    public static final String MODID = "electrona";

    // Creates a new recipe type. This is used for storing recipes in the map, and looking them up.
    public static final RecipeType<CompressorRecipe> COMPRESSING = new CompressorRecipe.CompressorRecipeType();
    public static final RecipeType<PurificatorRecipe> PURIFYING = new PurificatorRecipe.PurificatorRecipeType();

    public Electrona() {

        // Init Advancements and Registries
        RegistryHandler.init();
        TTriggers.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new EntityDiesEvent());
        MinecraftForge.EVENT_BUS.register(new Gamerules());
        MinecraftForge.EVENT_BUS.addListener(this::setupEngineerHouses);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation RL(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation FRL(String path) {
        return new ResourceLocation("forge", path);
    }

    public static ResourceLocation MRL(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public void setup(final FMLCommonSetupEvent event) {
        POIFixup.fixup();

        // Register biomes, game events, ...
        event.enqueueWork(() -> {
            GameEvents.setupGameEvents();

            Regions.register(new ElectronaRegions(Keys.ELECTRONA_OVERWORLD, 1));
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, SurfaceRuleData.makeRules());

            SculkSensorBlock.VIBRATION_STRENGTH_FOR_EVENT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(), (map) -> {
                map.putAll(SculkSensorBlock.VIBRATION_STRENGTH_FOR_EVENT);
                map.put(GameEvents.SINGULARITY, 7);
                map.put(GameEvents.TELEPORTER_USE, 10);
                map.put(GameEvents.ENERGETIC_LIGHTNING_STRIKE, 15);
                map.put(GameEvents.NUCLEAR_EXPLOSION, 20);
            }));
        });

        BrewingRecipeRegistry.addRecipe(Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(Items.SUGAR),
                new ItemStack(ItemInit.SUGAR_BOTTLE.get())); // sugar bottle brewing recipe

        BrewingRecipeRegistry.addRecipe(Ingredient.of(ItemInit.SUGAR_BOTTLE.get()), Ingredient.of(ItemInit.YELLOWCAKE.get()),
                new ItemStack(ItemInit.CONCENTRATED_URANIUM.get())); // concentrated uranium brewing recipe
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

    public static class ElectronaRegions extends Region {

        public ElectronaRegions(ResourceLocation name, int weight) {
            super(name, RegionType.OVERWORLD, weight);
        }

        @Override
        public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
            this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
                builder.replaceBiome(Biomes.DESERT, BiomeInit.NUCLEAR);
            });
        }
    }
}
