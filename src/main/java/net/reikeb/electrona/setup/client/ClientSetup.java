package net.reikeb.electrona.setup.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.guis.*;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.particles.*;
import net.reikeb.electrona.setup.client.render.*;

import static net.reikeb.electrona.init.ContainerInit.*;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Electrona.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        // Connect Containers and Windows
        ScreenManager.register(BIOMASS_GENERATOR_CONTAINER.get(), BiomassGeneratorWindow::new);
        ScreenManager.register(NUCLEAR_GENERATOR_CONTAINER.get(), NuclearGeneratorControllerWindow::new);
        ScreenManager.register(BATTERY_CONTAINER.get(), BatteryWindow::new);
        ScreenManager.register(CONVERTER_CONTAINER.get(), ConverterWindow::new);
        ScreenManager.register(COMPRESSOR_CONTAINER.get(), CompressorWindow::new);
        ScreenManager.register(XP_GENERATOR_CONTAINER.get(), XPGeneratorWindow::new);
        ScreenManager.register(TELEPORTER_CONTAINER.get(), TeleporterWindow::new);
        ScreenManager.register(WATER_PUMP_CONTAINER.get(), WaterPumpWindow::new);
        ScreenManager.register(PURIFICATOR_CONTAINER.get(), PurificatorWindow::new);
        ScreenManager.register(MINING_MACHINE_CONTAINER.get(), MiningMachineWindow::new);
        ScreenManager.register(SPRAYER_CONTAINER.get(), SprayerWindow::new);
        ScreenManager.register(DIMENSION_LINKER_CONTAINER.get(), DimensionLinkerWindow::new);
        ScreenManager.register(STEEL_CRATE_CONTAINER.get(), SteelCrateWindow::new);
        ScreenManager.register(LEAD_CRATE_CONTAINER.get(), LeadCrateWindow::new);
        ScreenManager.register(NUCLEAR_BOMB_CONTAINER.get(), NuclearBombWindow::new);

        // Connect Entities and their renderer
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.RADIOACTIVE_ZOMBIE_TYPE, RadioactiveZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.ENERGETIC_LIGHTNING_BOLT_TYPE, EnergeticLightningBoltRenderer::new);

        // Connect TileEntities and their renderer
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TILE_SINGULARITY.get(), TileSingularityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityInit.TILE_GRAVITOR.get(), TileGravitorRenderer::new);

        // Make this deferred for unsafe threads
        event.enqueueWork(() -> {
            // Cutout
            RenderTypeLookup.setRenderLayer(BlockInit.SINGULARITY.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockInit.PURIFICATOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockInit.RADIOACTIVE_GRASS.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockInit.RADIOACTIVE_TALL_GRASS.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockInit.GRAVITOR.get(), RenderType.cutout());

            // Translucent
            RenderTypeLookup.setRenderLayer(BlockInit.HOLE.get(), RenderType.translucent());

            // Item Properties
            ItemModelsProperties.register(ItemInit.GEIGER_POINTER.get(), new ResourceLocation("angle"), new IItemPropertyGetter() {
                private final ItemModelsProperties.Angle wobble = new ItemModelsProperties.Angle();
                private final ItemModelsProperties.Angle wobbleRandom = new ItemModelsProperties.Angle();

                public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
                    Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();

                    if (entity == null) {
                        return 0.0F;
                    } else {
                        if (world == null && entity.level instanceof ClientWorld) {
                            world = (ClientWorld) entity.level;
                        }

                        if (world == null) return 0.0F;
                        BlockPos blockpos = NBTUtil.readBlockPos(stack.getOrCreateTag().getCompound("CounterPos"));
                        long i = world.getGameTime();

                        if (!(entity.position().distanceToSqr((double) blockpos.getX() + 0.5D, entity.position().y(), (double) blockpos.getZ() + 0.5D) < (double) 1.0E-5F)) {
                            boolean flag = livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isLocalPlayer();
                            double d1 = 0.0D;

                            if (flag) {
                                d1 = livingEntity.yRot;
                            } else if (entity instanceof ItemFrameEntity) {
                                d1 = this.getFrameRotation((ItemFrameEntity) entity);
                            } else if (entity instanceof ItemEntity) {
                                d1 = 180.0F - ((ItemEntity) entity).getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
                            } else if (livingEntity != null) {
                                d1 = livingEntity.yBodyRot;
                            }

                            d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                            double d2 = this.getAngleTo(Vector3d.atCenterOf(blockpos), entity) / (double) ((float) Math.PI * 2F);
                            double d3;

                            if (flag) {
                                if (this.wobble.shouldUpdate(i)) {
                                    this.wobble.update(i, 0.5D - (d1 - 0.25D));
                                }
                                d3 = d2 + this.wobble.rotation;
                            } else {
                                d3 = 0.5D - (d1 - 0.25D - d2);
                            }

                            return MathHelper.positiveModulo((float) d3, 1.0F);

                        } else {
                            if (this.wobbleRandom.shouldUpdate(i)) {
                                this.wobbleRandom.update(i, Math.random());
                            }

                            double d0 = this.wobbleRandom.rotation + (double) ((float) stack.hashCode() / 2.14748365E9F);
                            return MathHelper.positiveModulo((float) d0, 1.0F);
                        }
                    }
                }

                private double getFrameRotation(ItemFrameEntity frameEntity) {
                    Direction direction = frameEntity.getDirection();
                    int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                    return MathHelper.wrapDegrees(180 + direction.get2DDataValue() * 90 + frameEntity.getRotation() * 45 + i);
                }

                private double getAngleTo(Vector3d vector3d, Entity entity) {
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
}
