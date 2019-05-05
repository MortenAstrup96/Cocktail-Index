package com.example.application.cocktailindex.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.application.cocktailindex.Utility.TypeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "cocktail")
public class Cocktail implements Serializable, Comparable {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @Ignore
    public List<Ingredient> ingredients = new ArrayList<>();

    @ColumnInfo(name = "recipe")
    public String recipe;

    @ColumnInfo(name = "comments")
    public String comments;


    @ColumnInfo(name = "imagePath")
    @TypeConverters(TypeConverter.class)
    public ArrayList<String> imagePath;

    @ColumnInfo(name = "favourite")
    public boolean favourite;

    @ColumnInfo(name = "idea")
    public boolean idea;


    public Cocktail(String name, String recipe, String comments, ArrayList<String> imagePath, boolean favourite, boolean idea) {
        id = UUID.randomUUID().hashCode();
        this.name = name;
        this.recipe = recipe;
        this.comments = comments;
        this.imagePath = imagePath;
        this.favourite = favourite;
        this.idea = idea;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.name.compareTo(((Cocktail)o).name);
    }
}

