package net.reikeb.electrona.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.items.Hammer;
import net.reikeb.electrona.misc.Keys;

import java.util.Map;

public class ShapedHammerRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {

    public static final Serializer SERIALIZER = new Serializer();

    final int width;
    final int height;
    final NonNullList<Ingredient> recipeItems;
    final ItemStack result;
    private final ResourceLocation id;
    final String group;

    public ShapedHammerRecipe(ResourceLocation resourceLocation, String name, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = resourceLocation;
        this.group = name;
        this.width = width;
        this.height = height;
        this.recipeItems = ingredients;
        this.result = result;
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
        return this.recipeItems;
    }

    public boolean canCraftInDimensions(int dim1, int dim2) {
        return dim1 >= this.width && dim2 >= this.height;
    }

    public boolean matches(CraftingContainer container, Level level) {
        for (int i = 0; i <= container.getWidth() - this.width; ++i) {
            for (int j = 0; j <= container.getHeight() - this.height; ++j) {
                if (this.matches(container, i, j, true)) {
                    return true;
                }

                if (this.matches(container, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(CraftingContainer container, int width, int height, boolean matches) {
        for (int i = 0; i < container.getWidth(); ++i) {
            for (int j = 0; j < container.getHeight(); ++j) {
                int k = i - width;
                int l = j - height;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (matches) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(container.getItem(i + j * container.getWidth()))) {
                    return false;
                }
            }
        }

        return checkHammer(container);
    }

    static boolean checkHammer(CraftingContainer container) {
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack1 = container.getItem(i);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.is(ItemInit.HAMMER.get())) {
                    if (!itemStack.isEmpty()) {
                        return false;
                    }
                    itemStack = itemStack1;
                }
            }
        }
        return !itemStack.isEmpty();
    }

    public ItemStack assemble(CraftingContainer container) {
        return this.getResultItem().copy();
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return getRemainingHammer(container);
    }

    public static NonNullList<ItemStack> getRemainingHammer(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.getItem() instanceof Hammer) {
                ItemStack itemStack1 = itemStack.copy();
                itemStack1.setDamageValue(itemStack1.getDamageValue() + 10);
                itemStack1.setCount(1);
                if (itemStack1.getDamageValue() > 0) {
                    list.set(i, itemStack1);
                }
                break;
            }
        }
        return list;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> list = this.getIngredients();
        return list.isEmpty() || list.stream().filter((p_151277_) -> {
            return !p_151277_.isEmpty();
        }).anyMatch((p_151273_) -> {
            return p_151273_.getItems().length == 0;
        });
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShapedHammerRecipe> {

        Serializer() {
            this.setRegistryName(Keys.SHAPED_HAMMER);
        }

        public ShapedHammerRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
            String[] astring = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> list = ShapedRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new ShapedHammerRecipe(resourceLocation, s, i, j, list, itemstack);
        }

        public ShapedHammerRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            int i = friendlyByteBuf.readVarInt();
            int j = friendlyByteBuf.readVarInt();
            String s = friendlyByteBuf.readUtf();
            NonNullList<Ingredient> list = NonNullList.withSize(i * j, Ingredient.EMPTY);

            list.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            ItemStack itemStack = friendlyByteBuf.readItem();
            return new ShapedHammerRecipe(resourceLocation, s, i, j, list, itemStack);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ShapedHammerRecipe recipe) {
            friendlyByteBuf.writeVarInt(recipe.width);
            friendlyByteBuf.writeVarInt(recipe.height);
            friendlyByteBuf.writeUtf(recipe.group);

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(recipe.result);
        }
    }
}
