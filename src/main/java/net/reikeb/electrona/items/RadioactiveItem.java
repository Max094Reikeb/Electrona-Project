package net.reikeb.electrona.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactiveItem extends Item {

    private final int durability;
    private final int power;

    public RadioactiveItem(int durability, int power, Item.Properties properties) {
        super(properties);
        this.durability = durability;
        this.power = power;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        RadioactivityFunction.radioactiveItemInInventory(world, entity, durability, power);
    }
}
