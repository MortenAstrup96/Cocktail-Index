package com.example.mortenastrup.cocktailindex.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.mortenastrup.cocktailindex.Objects.Cocktail;

import java.util.List;

@Dao
public interface CocktailDBDao {

    @Query("SELECT * FROM cocktail")
    List<Cocktail> getAll();

    @Insert
    void insertAll(Cocktail... cocktails);

    @Insert
    void insertOne(Cocktail cocktail);

    @Delete
    void delete(Cocktail cocktail);
}
