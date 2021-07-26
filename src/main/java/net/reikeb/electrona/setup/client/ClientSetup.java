package net.reikeb.electrona.setup.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.guis.*;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.particles.*;
import net.reikeb.electrona.setup.client.render.*;

import static net.reikeb.electrona.init.ContainerInit.*;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber(modid = Electrona.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        // This is where the wings layer is registered.
        // It can be put in any event listener of FMLClientSetupEvent if you want, like
        // in the main mod class.
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        registerLayer(dispatcher, "default");
        registerLayer(dispatcher, "slim");

        // Connect Containers and Windows
        MenuScreens.register(BIOMASS_GENERATOR_CONTAINER.get(), BiomassGeneratorWindow::new);
        MenuScreens.register(NUCLEAR_GENERATOR_CONTAINER.get(), NuclearGeneratorControllerWindow::new);
        MenuScreens.register(BATTERY_CONTAINER.get(), BatteryWindow::new);
        MenuScreens.register(CONVERTER_CONTAINER.get(), ConverterWindow::new);
        MenuScreens.register(COMPRESSOR_CONTAINER.get(), CompressorWindow::new);
        MenuScreens.register(XP_GENERATOR_CONTAINER.get(), XPGeneratorWindow::new);
        MenuScreens.register(TELEPORTER_CONTAINER.get(), TeleporterWindow::new);
        MenuScreens.register(WATER_PUMP_CONTAINER.get(), WaterPumpWindow::new);
        MenuScreens.register(PURIFICATOR_CONTAINER.get(), PurificatorWindow::new);
        MenuScreens.register(MINING_MACHINE_CONTAINER.get(), MiningMachineWindow::new);
        MenuScreens.register(SPRAYER_CONTAINER.get(), SprayerWindow::new);
        MenuScreens.register(DIMENSION_LINKER_CONTAINER.get(), DimensionLinkerWindow::new);
        MenuScreens.register(STEEL_CRATE_CONTAINER.get(), SteelCrateWindow::new);
        MenuScreens.register(LEAD_CRATE_CONTAINER.get(), LeadCrateWindow::new);
        MenuScreens.register(NUCLEAR_BOMB_CONTAINER.get(), NuclearBombWindow::new);

        // Connect Entities and their renderer
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.RADIOACTIVE_ZOMBIE_TYPE, RadioactiveZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE, EnergeticLightningBoltRenderer::new);

        // Connect TileEntities and their renderer
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TILE_SINGULARITY.get(), TileSingularityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TILE_GRAVITOR.get(), TileGravitorRenderer::new);

        // Make this deferred for unsafe threads
        event.enqueueWork(() -> {
            // Cutout
            ItemBlockRenderTypes.setRenderLayer(BlockInit.SINGULARITY.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.PURIFICATOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.RADIOACTIVE_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.RADIOACTIVE_TALL_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.GRAVITOR.get(), RenderType.cutout());

            // Translucent
            ItemBlockRenderTypes.setRenderLayer(BlockInit.HOLE.get(), RenderType.translucent());

            // Item Properties
            ItemProperties.register(ItemInit.GEIGER_POINTER.get(), new ResourceLocation("angle"), new ItemPropertyFunction() {
                private final ItemProperties.CompassWobble wobble = new ItemProperties.CompassWobble();
                private final ItemProperties.CompassWobble wobbleRandom = new ItemProperties.CompassWobble();

                public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity livingEntity) {
                    Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();

                    if (entity == null) {
                        return 0.0F;
                    } else {
                        if (world == null && entity.level instanceof ClientLevel) {
                            world = (ClientLevel) entity.level;
                        }

                        if (world == null) return 0.0F;
                        BlockPos blockpos = NbtUtils.readBlockPos(stack.getOrCreateTag().getCompound("CounterPos"));
                        long i = world.getGameTime();

                        if (!(entity.position().distanceToSqr((double) blockpos.getX() + 0.5D, entity.position().y(), (double) blockpos.getZ() + 0.5D) < (double) 1.0E-5F)) {
                            boolean flag = livingEntity instanceof Player && ((Player) livingEntity).isLocalPlayer();
                            double d1 = 0.0D;

                            if (flag) {
                                d1 = livingEntity.yRot;
                            } else if (entity instanceof ItemFrame) {
                                d1 = this.getFrameRotation((ItemFrame) entity);
                            } else if (entity instanceof ItemEntity) {
                                d1 = 180.0F - ((ItemEntity) entity).getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
                            } else if (livingEntity != null) {
                                d1 = livingEntity.yBodyRot;
                            }

                            d1 = Mth.positiveModulo(d1 / 360.0D, 1.0D);
                            double d2 = this.getAngleTo(Vec3.atCenterOf(blockpos), entity) / (double) ((float) Math.PI * 2F);
                            double d3;

                            if (flag) {
                                if (this.wobble.shouldUpdate(i)) {
                                    this.wobble.update(i, 0.5D - (d1 - 0.25D));
                                }
                                d3 = d2 + this.wobble.rotation;
                            } else {
                                d3 = 0.5D - (d1 - 0.25D - d2);
                            }

                            return Mth.positiveModulo((float) d3, 1.0F);

                        } else {
                            if (this.wobbleRandom.shouldUpdate(i)) {
                                this.wobbleRandom.update(i, Math.random());
                            }

                            double d0 = this.wobbleRandom.rotation + (double) ((float) stack.hashCode() / 2.14748365E9F);
                            return Mth.positiveModulo((float) d0, 1.0F);
                        }
                    }
                }

                private double getFrameRotation(ItemFrame frameEntity) {
                    Direction direction = frameEntity.getDirection();
                    int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                    return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + frameEntity.getRotation() * 45 + i);
                }

                private double getAngleTo(Vec3 vector3d, Entity entity) {
                    return Math.atan2(vector3d.z() - entity.getZ(), vector3d.x() - entity.getX());
                }
            });
        });
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleInit.DARK_MATTER.get(), DarkMatter.DarkMatterParticleFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleInit.GRAVITORIUM.get(), Gravitorium.GravitoriumParticleFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleInit.RADIOACTIVE_FALLOUT.get(), RadioactiveFallout.RadioactiveFalloutFactory::new);
    }

    @SubscribeEvent
    public static void textureSwitchEvent(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/base"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/cage"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/wind"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/wind_vertical"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/open_eye"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/closed_eye"));
        }
    }

    private static void registerLayer(EntityRenderDispatcher dispatcher, String type) {
        PlayerRenderer playerRenderer = dispatcher.getSkinMap().get(type);
        playerRenderer.addLayer(new MechanicWingsLayer<>(playerRenderer));
    }
}
