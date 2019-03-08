package com.example.application.cocktailindex.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "cocktail")
public class Cocktail implements Serializable {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "ingredients")
    public String ingredients;

    @ColumnInfo(name = "recipe")
    public String recipe;

    @ColumnInfo(name = "comments")
    public String comments;

    @ColumnInfo(name = "imagePath")
    public String imagePath;

    @ColumnInfo(name = "favourite")
    public boolean favourite;

    @ColumnInfo(name = "idea")
    public boolean idea;

    public Cocktail(String name, String ingredients, String recipe, String comments, String imagePath, boolean favourite, boolean idea) {
        id = UUID.randomUUID().hashCode();
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.comments = comments;
        this.imagePath = imagePath;
        this.favourite = favourite;
        this.idea = idea;
    }
}
