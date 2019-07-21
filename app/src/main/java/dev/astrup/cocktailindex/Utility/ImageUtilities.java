package dev.astrup.cocktailindex.Utility;

import dev.astrup.cocktailindex.Objects.Cocktail;

public class ImageUtilities {
    private static final ImageUtilities ourInstance = new ImageUtilities();

    public static ImageUtilities getInstance() {
        return ourInstance;
    }

    private ImageUtilities() {

    }

    public boolean hasFunctionalImage(Cocktail cocktail) {
        if(     cocktail.imagePath == null               ||
                cocktail.imagePath.isEmpty()        ||
                cocktail.imagePath.get(0).isEmpty()) {
            return false;
        }
        return true;
    }
}
