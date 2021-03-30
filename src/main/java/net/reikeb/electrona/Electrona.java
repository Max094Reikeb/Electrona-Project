package net.reikeb.electrona;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.client.render.MechanicWingsLayer;
import net.reikeb.electrona.client.setup.ClientSetup;
import net.reikeb.electrona.events.entity.PlayerDiesEvent;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.recipes.*;
import net.reikeb.electrona.recipes.types.*;
import net.reikeb.electrona.setup.RegistryHandler;
import net.reikeb.electrona.world.gamerules.DoBlackholesExist;
import net.reikeb.electrona.world.gen.ConfiguredFeatures;

import org.apache.logging.log4j.*;

@Mod(Electrona.MODID)
public class Electrona {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

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

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new PlayerDiesEvent());
        MinecraftForge.EVENT_BUS.register(new DoBlackholesExist());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ConfiguredFeatures::registerConfiguredFeatures);

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
