package com.example.mortenastrup.cocktailindex;

import android.app.Application;

import com.example.mortenastrup.cocktailindex.Database.Cocktail;

import java.util.ArrayList;
import java.util.List;

public class CocktailIndex extends Application {
    private List<Cocktail> cocktailList = new ArrayList<>();

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public void addCocktail(Cocktail newCocktail) {
        cocktailList.add(newCocktail);
    }

    public void removeCocktail(Cocktail deleteCocktail) {
        cocktailList.remove(deleteCocktail);
    }
}
