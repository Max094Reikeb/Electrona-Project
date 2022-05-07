package net.reikeb.electrona.setup.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.guis.*;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.particles.DarkMatter;
import net.reikeb.electrona.particles.Gravitorium;
import net.reikeb.electrona.particles.RadioactiveFallout;
import net.reikeb.electrona.setup.client.render.*;

import javax.annotation.Nullable;
import java.util.Optional;

import static net.reikeb.electrona.init.ContainerInit.*;

@Mod.EventBusSubscriber(modid = Electrona.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        // Make this deferred for unsafe threads
        event.enqueueWork(() -> {
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

            // Cutout
            ItemBlockRenderTypes.setRenderLayer(BlockInit.SINGULARITY.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.PURIFICATOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.RADIOACTIVE_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.RADIOACTIVE_TALL_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.GRAVITOR.get(), RenderType.cutout());

            // Translucent
            ItemBlockRenderTypes.setRenderLayer(BlockInit.HOLE.get(), RenderType.translucent());

            // Item Properties
            ItemProperties.register(ItemInit.GEIGER_POINTER.get(), Keys.ANGLE_PROPERTY, new ClampedItemPropertyFunction() {
                private final ItemProperties.CompassWobble wobble = new ItemProperties.CompassWobble();
                private final ItemProperties.CompassWobble wobbleRandom = new ItemProperties.CompassWobble();

                public float unclampedCall(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity player, int hash) {
                    Entity entity = player != null ? player : stack.getEntityRepresentation();
                    if (entity == null) return 0.0F;
                    if (world == null && entity.level instanceof ClientLevel) {
                        world = (ClientLevel) entity.level;
                    }
                    if (world == null) return 0.0F;
                    BlockPos blockpos = NbtUtils.readBlockPos(stack.getOrCreateTag().getCompound("CounterPos"));
                    long i = world.getGameTime();
                    if (!(entity.position().distanceToSqr((double) blockpos.getX() + 0.5D, entity.position().y(), (double) blockpos.getZ() + 0.5D) < (double) 1.0E-5F)) {
                        boolean flag = player instanceof Player && ((Player) player).isLocalPlayer();
                        double d1 = 0.0D;
                        if (flag) {
                            d1 = player.getYRot();
                        } else if (entity instanceof ItemFrame) {
                            d1 = this.getFrameRotation((ItemFrame) entity);
                        } else if (entity instanceof ItemEntity) {
                            d1 = 180.0F - ((ItemEntity) entity).getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
                        } else if (player != null) {
                            d1 = player.yBodyRot;
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

                        double d0 = this.wobbleRandom.rotation + (double) ((float) this.hash(hash) / 2.14748365E9F);
                        return Mth.positiveModulo((float) d0, 1.0F);
                    }
                }

                private int hash(int hash) {
                    return hash * 1327217883;
                }

                @Nullable
                private BlockPos getSpawnPosition(ClientLevel world) {
                    return world.dimensionType().natural() ? world.getSharedSpawnPos() : null;
                }

                @Nullable
                private BlockPos getLodestonePosition(Level world, CompoundTag tag) {
                    boolean flag = tag.contains("LodestonePos");
                    boolean flag1 = tag.contains("LodestoneDimension");
                    if (flag && flag1) {
                        Optional<ResourceKey<Level>> optional = CompassItem.getLodestoneDimension(tag);
                        if (optional.isPresent() && world.dimension() == optional.get()) {
                            return NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
                        }
                    }

                    return null;
                }

                private double getFrameRotation(ItemFrame frame) {
                    Direction direction = frame.getDirection();
                    int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                    return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + frame.getRotation() * 45 + i);
                }

                private double getAngleTo(Vec3 vec3, Entity entity) {
                    return Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX());
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
    public static void registerModels(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.RADIOACTIVE_ZOMBIE_TYPE, RadioactiveZombieRenderer::new);
        event.registerEntityRenderer(EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE, EnergeticLightningBoltRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityInit.SINGULARITY_BLOCK_ENTITY.get(), SingularityBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.GRAVITOR_BLOCK_ENTITY.get(), GravitorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void textureSwitchEvent(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            event.addSprite(Keys.GRAVITOR_BASE);
            event.addSprite(Keys.GRAVITOR_CAGE);
            event.addSprite(Keys.GRAVITOR_WIND);
            event.addSprite(Keys.GRAVITOR_WIND_VERTICAL);
            event.addSprite(Keys.GRAVITOR_OPEN_EYE);
            event.addSprite(Keys.GRAVITOR_CLOSED_EYE);
        }
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MechanicWingsLayer.MECHANIC_WINGS_LAYER, MechanicWingsModel::createLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skinTypeName -> {
            if (event.getSkin(skinTypeName) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new MechanicWingsLayer<>(renderer, event.getEntityModels()));
            }
        });
    }
}
