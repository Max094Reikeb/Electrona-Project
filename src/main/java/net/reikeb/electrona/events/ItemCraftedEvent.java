package net.reikeb.electrona.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class ItemCraftedEvent {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack eventCrafting = event.getCrafting();
        Block blockOutput = Block.byItem(eventCrafting.getItem());

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> generatorTag, machineTag;
        generatorTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/generators"));
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/machines_all"));

        if (!(generatorTag.contains(blockOutput) || machineTag.contains(blockOutput))) return;

        if (generatorTag.contains(blockOutput)) {
            Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation("electrona:generator"));
            AdvancementProgress advancementProgress = player.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String criteria : advancementProgress.getRemainingCriteria()) {
                    player.getAdvancements().award(advancement, criteria);
                }
            }
        }

        if (machineTag.contains(blockOutput)) {
            Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation("electrona:mechinator"));
            AdvancementProgress advancementProgress = player.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String criteria : advancementProgress.getRemainingCriteria()) {
                    player.getAdvancements().award(advancement, criteria);
                }
            }
        }
    }
}
