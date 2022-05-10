package net.reikeb.electrona.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.misc.Keys;

public class ShapelessHammerRecipe implements CraftingRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation id;
    final String group;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public ShapelessHammerRecipe(ResourceLocation resourceLocation, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        StackedContents stackedContents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemStack = container.getItem(j);
            if (!itemStack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedContents.accountStack(itemStack, 1);
                else inputs.add(itemStack);
            }
        }

        return ShapedHammerRecipe.checkHammer(container) && i == this.ingredients.size() && (isSimple ? stackedContents.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
    }

    public ItemStack assemble(CraftingContainer container) {
        return this.result.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return ShapedHammerRecipe.getRemainingHammer(container);
    }

    public boolean canCraftInDimensions(int dim1, int dim2) {
        return dim1 * dim2 >= this.ingredients.size();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShapelessHammerRecipe> {

        Serializer() {
            this.setRegistryName(Keys.SHAPELESS_HAMMER);
        }

        public ShapelessHammerRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            NonNullList<Ingredient> list = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (list.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else {
                try {
                    if (list.size() > ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_WIDTH").getInt(null) * ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_HEIGHT").getInt(null)) {
                        throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + (ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_WIDTH").getInt(null) * ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_HEIGHT").getInt(null)));
                    } else {
                        ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                        return new ShapelessHammerRecipe(resourceLocation, s, itemStack, list);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> list = NonNullList.create();

            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    list.add(ingredient);
                }
            }

            return list;
        }

        public ShapelessHammerRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String s = friendlyByteBuf.readUtf();
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> list = NonNullList.withSize(i, Ingredient.EMPTY);

            list.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            ItemStack itemStack = friendlyByteBuf.readItem();
            return new ShapelessHammerRecipe(resourceLocation, s, itemStack, list);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ShapelessHammerRecipe recipe) {
            friendlyByteBuf.writeUtf(recipe.group);
            friendlyByteBuf.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(recipe.result);
        }
    }
}
