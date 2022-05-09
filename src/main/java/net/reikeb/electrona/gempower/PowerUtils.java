package net.reikeb.electrona.gempower;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.reikeb.electrona.init.GemInit;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class PowerUtils {

    private static final MutableComponent NO_CONTENT = (new TranslatableComponent("power.electrona.empty")).withStyle(ChatFormatting.GRAY);

    public static List<GemPowerInstance> getPower(ItemStack stack) {
        return getAllGemPowers(stack.getTag());
    }

    public static List<GemPowerInstance> getAllPowers(GemObject gemObject, Collection<GemPowerInstance> gemPowerInstances) {
        List<GemPowerInstance> list = Lists.newArrayList();
        for (Supplier<GemPowerInstance> gemPowerInstanceSupplier : gemObject.getPowers()) {
            list.add(gemPowerInstanceSupplier.get());
        }
        list.addAll(gemPowerInstances);
        return list;
    }

    public static List<GemPowerInstance> getAllGemPowers(@Nullable CompoundTag nbt) {
        List<GemPowerInstance> list = Lists.newArrayList();
        for (Supplier<GemPowerInstance> gemPowerInstanceSupplier : getGem(nbt).getPowers()) {
            list.add(gemPowerInstanceSupplier.get());
        }
        getCustomPowers(nbt, list);
        return list;
    }

    public static List<GemPowerInstance> getCustomPowers(ItemStack stack) {
        return getCustomPowers(stack.getTag());
    }

    public static List<GemPowerInstance> getCustomPowers(@Nullable CompoundTag nbt) {
        List<GemPowerInstance> list = Lists.newArrayList();
        getCustomPowers(nbt, list);
        return list;
    }

    public static void getCustomPowers(@Nullable CompoundTag nbt, List<GemPowerInstance> gemPowerInstances) {
        if (nbt != null && nbt.contains("CustomPower", 9)) {
            ListTag listNBT = nbt.getList("CustomPower", 10);

            for (int i = 0; i < listNBT.size(); i++) {
                CompoundTag compoundNBT = listNBT.getCompound(i);
                GemPowerInstance gemPowerInstance = GemPowerInstance.load(compoundNBT);
                if (gemPowerInstance != null) {
                    gemPowerInstances.add(gemPowerInstance);
                }
            }
        }
    }

    public static GemObject getGem(ItemStack stack) {
        return getGem(stack.getTag());
    }

    public static GemObject getGem(@Nullable CompoundTag nbt) {
        return nbt == null ? GemInit.EMPTY.get() : GemObject.byName(nbt.getString("Gem"));
    }

    public static ItemStack setGem(ItemStack stack, GemObject gemObject) {
        ResourceLocation resourceLocation = GemInit.GEM_REGISTRY.get().getKey(gemObject);
        if (gemObject == GemInit.EMPTY.get()) {
            stack.removeTagKey("Gem");
        } else {
            stack.getOrCreateTag().putString("Gem", resourceLocation.toString());
        }
        return stack;
    }

    public static ItemStack setCustomPowers(ItemStack stack, Collection<GemPowerInstance> gemPowerInstances) {
        if (!gemPowerInstances.isEmpty()) {
            CompoundTag tag = stack.getOrCreateTag();
            ListTag list = tag.getList("CustomPower", 9);

            for (GemPowerInstance gemPowerInstance : gemPowerInstances) {
                list.add(gemPowerInstance.save(new CompoundTag()));
            }

            tag.put("CustomPower", list);
        }
        return stack;
    }

    public static void setRandomPower(ItemStack stack) {
        if (!getPower(stack).isEmpty()) return;
        @NotNull Collection<GemObject> values = GemInit.GEM_REGISTRY.get().getValues();
        setGem(stack, values.stream().skip(new Random().nextInt(values.size())).findFirst().orElseThrow());
    }

    @OnlyIn(Dist.CLIENT)
    public static void addGemTooltip(ItemStack stack, List<Component> text) {
        List<GemPowerInstance> list = getPower(stack);
        if (list.isEmpty()) {
            text.add(NO_CONTENT);
        } else {
            for (GemPowerInstance gemPowerInstance : list) {
                GemPower gemPower = gemPowerInstance.getGemPower();
                int cooldown = gemPowerInstance.getCooldown();
                text.add(new TranslatableComponent(gemPower.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                text.add(new TranslatableComponent("power.electrona.cosmic_gem_cooldown", cooldown).withStyle(ChatFormatting.DARK_GRAY));
                if (stack.getOrCreateTag().getBoolean("dimensionTravel")) {
                    String dimension = stack.getOrCreateTag().getString("dimension");
                    text.add(new TranslatableComponent(gemPower.getDescriptionId() + "_" + dimension).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }
}
