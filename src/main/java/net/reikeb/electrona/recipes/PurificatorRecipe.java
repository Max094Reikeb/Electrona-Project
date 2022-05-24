package net.reikeb.electrona.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Keys;

public class PurificatorRecipe implements Recipe<Container> {

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
    }

    @Override
    public String toString() {
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

    @Override
    public boolean matches(Container inv, Level level) {
        return ItemStack.matches(this.input, inv.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.output.copy(); // This method is ignored by the system. getRecipeOutput().copy() is used instead.
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true; // Unnecessary, just needs to be overriden to true.
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
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return Electrona.PURIFYING;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.PURIFICATOR.get());
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PurificatorRecipe> {
        Serializer() {
            this.setRegistryName(Keys.PURIFYING);
        }

        @Override
        public PurificatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            // Reads a recipe from json.

            // Reads the inputs. The common utility method in ShapedRecipe is what all vanilla
            // recipe classes use for this.
            final ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));


            // Reads the output. The common utility method in ShapedRecipe is what all vanilla
            // recipe classes use for this.
            final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            final boolean randomOutput = GsonHelper.getAsBoolean(json, "random_output", false);

            final int purifyingTime = GsonHelper.getAsInt(json, "time", 20);
            final int waterRequired = GsonHelper.getAsInt(json, "water", 20);

            return new PurificatorRecipe(recipeId, input, output, randomOutput, purifyingTime, waterRequired);
        }

        @Override
        public PurificatorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            // Reads a recipe from a packet buffer. This code is called on the client.
            final ItemStack input = buffer.readItem();
            final ItemStack output = buffer.readItem();
            final boolean randomOutput = buffer.readBoolean();
            final int purifyingTime = buffer.readInt();
            final int waterRequired = buffer.readInt();

            return new PurificatorRecipe(recipeId, input, output, randomOutput, purifyingTime, waterRequired);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PurificatorRecipe recipe) {

            // Writes the recipe to a packet buffer. This is called on the server when a player
            // connects or when /reload is used.
            buffer.writeItemStack(recipe.input, true);
            buffer.writeItemStack(recipe.output, true);
            buffer.writeBoolean(recipe.randomOutput);
            buffer.writeInt(recipe.purifyingTime);
            buffer.writeInt(recipe.waterRequired);
        }
    }

    public static class PurificatorRecipeType implements RecipeType<PurificatorRecipe> {
        @Override
        public String toString() {
            return Keys.PURIFYING.toString();
        }
    }
}
