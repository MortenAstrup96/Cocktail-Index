package com.example.application.cocktailindex.Utility;

import com.example.application.cocktailindex.Objects.Cocktail;

import java.util.ArrayList;
import java.util.List;

public class CocktailSingleton {
    private static final CocktailSingleton ourInstance = new CocktailSingleton();

    public static CocktailSingleton getInstance() {
        return ourInstance;
    }
    List<Cocktail> cocktailList;


    private CocktailSingleton() {
        cocktailList = new ArrayList<>();
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }
}
