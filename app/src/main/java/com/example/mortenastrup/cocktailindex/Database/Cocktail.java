package com.example.mortenastrup.cocktailindex.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Cocktail implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "recipe")
    private String recipe;

    @ColumnInfo(name = "ingredients")
    private ArrayList<String> ingredients;

    @ColumnInfo(name = "image")
    private Bitmap image;

    @ColumnInfo(name = "favourite")
    private boolean favourite;

    @ColumnInfo(name = "idea")
    private boolean idea;

    public Cocktail(String name, String recipe, ArrayList<String> ingredients, Bitmap image, boolean favourite, boolean idea) {
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.image = image;
        this.favourite = favourite;
        this.idea = idea;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
