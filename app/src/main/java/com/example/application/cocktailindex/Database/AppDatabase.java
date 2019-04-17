package com.example.application.cocktailindex.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;

@Database(entities = {Cocktail.class, Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CocktailDBDao cocktailDBDao();
    public abstract IngredientDBDao ingredientDBDao();
}

