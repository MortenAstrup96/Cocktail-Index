package com.example.mortenastrup.cocktailindex;

import java.util.ArrayList;

public class Cocktail {
    private int id;
    private String name;
    private String recipe;
    private ArrayList<String> ingredients;

    public Cocktail(String name, String recipe, ArrayList<String> ingredients) {
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
