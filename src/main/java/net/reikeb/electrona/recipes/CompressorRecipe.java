package net.reikeb.electrona.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.contexts.CompressingContext;
import net.reikeb.maxilib.inventory.SingletonInventory;

import javax.annotation.Nullable;
import java.util.Optional;

public class CompressorRecipe implements Recipe<Container> {

    public static final Serializer SERIALIZER = new Serializer();

    private final Ingredient input;
    private final Ingredient input2;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int compressingTime;
    private final int energyRequired;

    public CompressorRecipe(ResourceLocation id, Ingredient input, Ingredient input2, ItemStack output, int compressingTime, int energyRequired) {
        this.id = id;
        this.input = input;
        this.input2 = input2;
        this.output = output;
        this.compressingTime = compressingTime;
        this.energyRequired = energyRequired;

        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        System.out.println("Loaded " + this.toString());
    }

    public static Optional<CompressorRecipe> getRecipe(Level world, @Nullable BlockPos pos, ItemStack stack) {
        return getRecipe(world, new CompressingContext(new SingletonInventory(stack), null, pos != null ? () -> Vec3.atCenterOf(pos) : null, null));
    }

    public static Optional<CompressorRecipe> getRecipe(Level world, CompressingContext ctx) {
        return world.getRecipeManager().getRecipeFor(Electrona.COMPRESSING, ctx, world);
    }

    @Override
    public String toString() {

        // Overriding toString is not required, it's just useful for debugging.
        return "Compressor recipe [input=" + this.input + ", input2=" + this.input2 + ", output=" + this.output + ", id=" + this.id + "]";
    }

    public int getCompressingTime() {
        return this.compressingTime;
    }

    public int getEnergyRequired() {
        return this.energyRequired;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return (this.input.test(inv.getItem(0)) && (this.input2.test(inv.getItem(1))));
    }

    @Override
    public ItemStack assemble(Container inv) {

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

            // This registry name is what people will specify in their json files.
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

            return new CompressorRecipe(recipeId, input, input2, output, compressingTime, energyRequired);
        }

        @Override
        public CompressorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            // Reads a recipe from a packet buffer. This code is called on the client.
            final Ingredient input = Ingredient.fromNetwork(buffer);
            final Ingredient input2 = Ingredient.fromNetwork(buffer);
            final ItemStack output = buffer.readItem();
            final int compressingTime = buffer.readInt();
            final int energyRequired = buffer.readInt();

            return new CompressorRecipe(recipeId, input, input2, output, compressingTime, energyRequired);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CompressorRecipe recipe) {

            // Writes the recipe to a packet buffer. This is called on the server when a player
            // connects or when /reload is used.
            recipe.input.toNetwork(buffer);
            recipe.input2.toNetwork(buffer);
            buffer.writeItemStack(recipe.output, true);
            buffer.writeInt(recipe.compressingTime);
            buffer.writeInt(recipe.energyRequired);
        }
    }
}
