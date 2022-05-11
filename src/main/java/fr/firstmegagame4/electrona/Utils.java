package fr.firstmegagame4.electrona;

import net.minecraft.util.Identifier;

public class Utils {

    public static final String modIdentifier = "electrona";

    public static Identifier ELIdentifier(String path) {
        return new Identifier(modIdentifier, path);
    }

}
