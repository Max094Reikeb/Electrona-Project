package net.reikeb.electrona.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactiveItem extends Item {

    private final int radioDura;
    private final int radioPower;

    public RadioactiveItem(int radioDura, int radioPower, Item.Properties properties) {
        super(properties);
        this.radioDura = radioDura;
        this.radioPower = radioPower;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        RadioactivityFunction.radioactiveItemInInventory(world, entity, radioDura, radioPower);
    }
}
