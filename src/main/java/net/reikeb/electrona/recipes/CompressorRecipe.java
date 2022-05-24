package net.reikeb.electrona.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.Couple;

public class CompressorRecipe implements Recipe<Container> {

    public static final Serializer SERIALIZER = new Serializer();

    private final Couple<Ingredient, Ingredient> inputs;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int compressingTime;
    private final int energyRequired;

    public CompressorRecipe(ResourceLocation id, Couple<Ingredient, Ingredient> inputs, ItemStack output, int compressingTime, int energyRequired) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.compressingTime = compressingTime;
        this.energyRequired = energyRequired;
    }

    @Override
    public String toString() {
        return "Compressor recipe [input=" + this.inputs.part1() + ", input2=" + this.inputs.part2() + ", output=" + this.output + ", id=" + this.id + "]";
    }

    public int getCompressingTime() {
        return this.compressingTime;
    }

    public int getEnergyRequired() {
        return this.energyRequired;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return (this.inputs.part1().test(inv.getItem(0)) && (this.inputs.part2().test(inv.getItem(1))));
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
        return Electrona.COMPRESSING;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.COMPRESSOR.get());
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CompressorRecipe> {
        Serializer() {
            this.setRegistryName(Keys.COMPRESSING);
        }

        @Override
        public CompressorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            // Reads a recipe from json.

            // Reads the inputs. Accepts items, tags, and anything else that
            // Ingredient.deserialize can understand.
            final JsonElement inputElement = GsonHelper.isArrayNode(json, "input") ? GsonHelper.getAsJsonArray(json, "input") : GsonHelper.getAsJsonObject(json, "input");
            final Ingredient input = Ingredient.fromJson(inputElement);

            final JsonElement inputElement2 = GsonHelper.isArrayNode(json, "input2") ? GsonHelper.getAsJsonArray(json, "input2") : GsonHelper.getAsJsonObject(json, "input2");
            final Ingredient input2 = Ingredient.fromJson(inputElement2);

            // Reads the output. The common utility method in ShapedRecipe is what all vanilla
            // recipe classes use for this.
            final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            final int compressingTime = GsonHelper.getAsInt(json, "time", 20);
            final int energyRequired = GsonHelper.getAsInt(json, "energy", 20);

            return new CompressorRecipe(recipeId, new Couple<>(input, input2), output, compressingTime, energyRequired);
        }

        @Override
        public CompressorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            // Reads a recipe from a packet buffer. This code is called on the client.
            final Ingredient input = Ingredient.fromNetwork(buffer);
            final Ingredient input2 = Ingredient.fromNetwork(buffer);
            final ItemStack output = buffer.readItem();
            final int compressingTime = buffer.readInt();
            final int energyRequired = buffer.readInt();

            return new CompressorRecipe(recipeId, new Couple<>(input, input2), output, compressingTime, energyRequired);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CompressorRecipe recipe) {

            // Writes the recipe to a packet buffer. This is called on the server when a player
            // connects or when /reload is used.
            recipe.inputs.part1().toNetwork(buffer);
            recipe.inputs.part2().toNetwork(buffer);
            buffer.writeItemStack(recipe.output, true);
            buffer.writeInt(recipe.compressingTime);
            buffer.writeInt(recipe.energyRequired);
        }
    }

    public static class CompressorRecipeType implements RecipeType<CompressorRecipe> {
        @Override
        public String toString() {
            return Keys.COMPRESSING.toString();
        }
    }
}
