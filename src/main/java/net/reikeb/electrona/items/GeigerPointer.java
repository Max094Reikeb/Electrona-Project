package net.reikeb.electrona.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.maxilib.utils.BiomeUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

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
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(new TranslatableComponent("item.electrona.geiger_pointer.desc"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (level instanceof ServerLevel serverLevel) {
                return serverSideImpl(serverLevel, player, hand);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                Electrona.LOGGER.error("Our code didn't work and therefore failed bc some mod is doing some scary code and making levels that are non client-side and that don't extend server level.");
                return InteractionResultHolder.fail(stack);
            }
        }

        return super.use(level, player, hand);
    }

    @NotNull
    private InteractionResultHolder<ItemStack> serverSideImpl(ServerLevel level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag stackTag = stack.getOrCreateTag();

        // If we already have a target position, avoid calling "getBiomePosition" because it's computationally expensive.
        // if a player spams this item, they can lock the server thread and the server watch dog can and will kill the server.
        if (stackTag.contains("CounterPos")) {
            return InteractionResultHolder.pass(stack);
        } else {

            BlockPos playerPos = player.blockPosition();
            if (stackTag.contains("LastSearchPos")) {
                BlockPos pos = NbtUtils.readBlockPos(stackTag.getCompound("LastSearchPos"));
                if (playerPos.distSqr(pos) < 1000) { // Corgi: not sure if distSqr is the distance between the block positions lol...

                    // We're to close to where we last checked for the biome pos and we want to prevent the spam usage of this item,
                    // as explained earlier "getBiomePosition" is computationally expensive for the server.
                    return InteractionResultHolder.fail(stack);
                }

            }
            @Nullable
            BlockPos biomePosition = BiomeUtil.getNearestBiomePosition(level, playerPos, BiomeInit.NUCLEAR);
            if (biomePosition == null) {
                // Write where the player last attempted to search.
                stackTag.put("LastSearchPos", NbtUtils.writeBlockPos(playerPos));
                player.displayClientMessage(new TextComponent("item.player.go_farther_away"), true);
                return InteractionResultHolder.fail(stack);
            } else {
                // Where the item points too.
                stackTag.put("CounterPos", NbtUtils.writeBlockPos(biomePosition));

                // Remove this extra data
                if (stackTag.contains("LastSearchPos")) {
                    stackTag.remove("LastSearchPos");
                }

                // Do we need to sync "CounterPos" to the client via packets?!?!


                // We found a biome!
                return InteractionResultHolder.success(stack);
            }
        }
    }
}
