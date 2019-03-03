package com.example.mortenastrup.cocktailindex;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.mortenastrup.cocktailindex.Objects.Cocktail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CocktailIndex extends Application {

    private List<Cocktail> cocktailList = new ArrayList<>();
    private Map<Integer, Bitmap> imageMap = new HashMap<>();

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }

    public Map<Integer, Bitmap> getImageMap() {
        return imageMap;
    }

    public void setImageMap(Map<Integer, Bitmap> imageMap) {
        this.imageMap = imageMap;
    }
}
