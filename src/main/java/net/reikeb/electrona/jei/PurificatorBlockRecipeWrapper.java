package net.reikeb.electrona.jei;

import java.util.ArrayList;

public record PurificatorBlockRecipeWrapper(ArrayList input, ArrayList output) {

    public ArrayList getInput() {
        return input;
    }

    public ArrayList getOutput() {
        return output;
    }
}
