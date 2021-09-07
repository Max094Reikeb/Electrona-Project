package net.reikeb.electrona.jei;

import java.util.ArrayList;

public class CompressorBlockRecipeWrapper {

    private final ArrayList input;
    public final ArrayList output;

    public CompressorBlockRecipeWrapper(ArrayList input, ArrayList output) {
        this.input = input;
        this.output = output;
    }

    public ArrayList getInput() {
        return input;
    }

    public ArrayList getOutput() {
        return output;
    }
}
