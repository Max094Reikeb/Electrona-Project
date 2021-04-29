package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.setup.ItemGroups;

import javax.annotation.Nullable;
import java.util.*;

public class GeigerPointer extends Item {

    public GeigerPointer() {
        super(new Properties()
                .stacksTo(1)
                .tab(ItemGroups.ELECTRONA_ITEMS));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slot) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("item.electrona.geiger_pointer.desc"));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(world instanceof ServerWorld)) return ActionResult.fail(stack);
        BlockPos pos = getBiomePosition(world, player);
        if (pos == null) return ActionResult.fail(stack);
        stack.getOrCreateTag().put("CounterPos", NBTUtil.writeBlockPos(pos));
        return ActionResult.success(stack);
    }

    @Nullable
    private BlockPos getBiomePosition(World world, Entity entity) {
        Optional<MutableRegistry<Biome>> biomeRegistry = world.registryAccess().registry(Registry.BIOME_REGISTRY);

        if (biomeRegistry.isPresent()) {
            Biome biome = biomeRegistry.get().get(new ResourceLocation(Electrona.MODID, "nuclear"));
            if (biome != null && world.getServer() != null) {
                return world.getServer().overworld().findNearestBiome(biome, entity.blockPosition(), 6400, 8);
            }
        }

        Electrona.LOGGER.error(new TranslationTextComponent("commands.locatebiome.invalid"));
        return null;
    }
}
