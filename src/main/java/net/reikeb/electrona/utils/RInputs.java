package net.reikeb.electrona.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

public class RInputs {

    private final ItemStack input1;
    private final ItemStack input2;

    public RInputs(Item input1, Item input2) {
        this.input1 = new ItemStack(input1, 1);
        this.input2 = new ItemStack(input2, 1);
    }

    public RInputs(ItemStack input1, ItemStack input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    public RInputs(Block block1, Block block2) {
        this.input1 = new ItemStack(block1.asItem());
        this.input2 = new ItemStack(block2.asItem());
    }

    public final ItemStack getInput1() {
        return this.input1;
    }

    public final Item getItemInput1() {
        return this.input1.getItem();
    }

    public final ItemStack getInput2() {
        return this.input2;
    }

    public final Item getItemInput2() {
        return this.input2.getItem();
    }

    public final ArrayList<ItemStack> getInputs() {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        inputs.add(this.input1);
        inputs.add(this.input2);
        return inputs;
    }
}
