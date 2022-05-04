package fr.firstmegagame4.electrona;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Utils {

    public static final String modIdentifier = "electrona";

    public static Identifier electronaIdentifier(String path) {
        return new Identifier(modIdentifier, path);
    }

    public static void registerBlock(String blockId, Block block, BlockItem blockItem) {
        Registry.register(Registry.BLOCK, electronaIdentifier(blockId), block);
        Registry.register(Registry.ITEM, electronaIdentifier(blockId), blockItem);
    }

}
