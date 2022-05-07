package net.reikeb.electrona.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.setup.ItemGroups;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("item.electrona.geiger_pointer.desc"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(world instanceof ServerLevel)) return InteractionResultHolder.fail(stack);
        BlockPos pos = getBiomePosition(world, player);
        if (pos == null) return InteractionResultHolder.fail(stack);
        stack.getOrCreateTag().put("CounterPos", NbtUtils.writeBlockPos(pos));
        return InteractionResultHolder.success(stack);
    }

    @Nullable
    private BlockPos getBiomePosition(Level world, Entity entity) {
        Optional<Biome> biome = world.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(Keys.NUCLEAR_BIOME);

        if (biome.isPresent() && (world.getServer() != null)) {
            return world.getServer().overworld().findNearestBiome(biome.get(), entity.blockPosition(), 6400, 8);
        }

        Electrona.LOGGER.error(new TranslatableComponent("commands.locatebiome.invalid"));
        return null;
    }
}
