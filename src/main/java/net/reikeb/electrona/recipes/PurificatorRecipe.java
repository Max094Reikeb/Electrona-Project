package net.reikeb.electrona.recipes;

import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import net.minecraftforge.registries.ForgeRegistryEntry;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.recipes.contexts.PurifyingContext;
import net.reikeb.electrona.utils.SingletonInventory;

import javax.annotation.Nullable;

import java.util.Optional;

public class PurificatorRecipe implements IRecipe<IInventory> {

    public static final PurificatorRecipe.Serializer SERIALIZER = new PurificatorRecipe.Serializer();

    private final ItemStack input;
    private final ItemStack output;
    private final ResourceLocation id;
    private final boolean randomOutput;
    private final int purifyingTime;
    private final int waterRequired;

    public PurificatorRecipe(ResourceLocation id, ItemStack input, ItemStack output, boolean randomOutput, int purifyingTime, int waterRequired) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.randomOutput = randomOutput;
        this.purifyingTime = purifyingTime;
        this.waterRequired = waterRequired;

        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        System.out.println("Loaded " + this.toString());
    }

    @Override
    public String toString() {

        // Overriding toString is not required, it's just useful for debugging.
        return "Purificator recipe [input=" + this.input + ", output=" + this.output + ", id=" + this.id + "]";
    }

    public int getPurifyingTime() {
        return this.purifyingTime;
    }

    public int getWaterRequired() {
        return this.waterRequired;
    }

    public int getCountInput() {
        return this.input.getCount();
    }

    public int getCountOutput() {
        int count = this.output.getCount() == 0 ? 1 : this.output.getCount();
        return this.randomOutput ? (Math.random() < 0.5 ? count : (count - 1)) : count;
    }

    public static Optional<PurificatorRecipe> getRecipe(World world, @Nullable BlockPos pos, ItemStack stack) {
        return getRecipe(world, new PurifyingContext(new SingletonInventory(stack), null, pos != null ? () -> Vector3d.atCenterOf(pos) : null, null));
    }

    public static Optional<PurificatorRecipe> getRecipe(World world, PurifyingContext ctx) {
        return world.getRecipeManager().getRecipeFor(Electrona.PURIFYING, ctx, world);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return ItemStack.matches(this.input, inv.getItem(1));
    }

    @Override
    public ItemStack assemble(IInventory inv) {

        // This method is ignored by our custom recipe system. getRecipeOutput().copy() is used instead.
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {

        // Unnecessary method, just need to override it to true.
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return Electrona.PURIFYING;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.PURIFICATOR.get());
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PurificatorRecipe> {
        Serializer() {

            // This registry name is what people will specify in their json files.
            this.setRegistryName(new ResourceLocation(Electrona.MODID, "purifying"));
        }

        @Override
        public PurificatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            // Reads a recipe from json.

            // Reads the inputs. The common utility method in ShapedRecipe is what all vanilla
            // recipe classes use for this.
            final ItemStack input = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "input"));


            // Reads the output. The common utility method in ShapedRecipe is what all vanilla
            // recipe classes use for this.
            final ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

            final boolean randomOutput = JSONUtils.getAsBoolean(json, "random_output", false);

            final int purifyingTime = JSONUtils.getAsInt(json, "time", 20);
            final int waterRequired = JSONUtils.getAsInt(json, "water", 20);

            return new PurificatorRecipe(recipeId, input, output, randomOutput, purifyingTime, waterRequired);
        }

        @Override
        public PurificatorRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {

            // Reads a recipe from a packet buffer. This code is called on the client.
            final ItemStack input = buffer.readItem();
            final ItemStack output = buffer.readItem();
            final boolean randomOutput = buffer.readBoolean();
            final int purifyingTime = buffer.readVarInt();
            final int waterRequired = buffer.readVarInt();

            return new PurificatorRecipe(recipeId, input, output, randomOutput, purifyingTime, waterRequired);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, PurificatorRecipe recipe) {

            // Writes the recipe to a packet buffer. This is called on the server when a player
            // connects or when /reload is used.
            buffer.writeItemStack(recipe.input, true);
            buffer.writeItemStack(recipe.output, true);
            buffer.writeBoolean(recipe.randomOutput);
            buffer.writeInt(recipe.purifyingTime);
            buffer.writeInt(recipe.waterRequired);
        }
    }
}
