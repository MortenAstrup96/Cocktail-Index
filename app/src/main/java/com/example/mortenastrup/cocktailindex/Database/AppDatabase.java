package com.example.mortenastrup.cocktailindex.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.mortenastrup.cocktailindex.Objects.Cocktail;

@Database(entities = {Cocktail.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CocktailDBDao cocktailDBDao();
}

