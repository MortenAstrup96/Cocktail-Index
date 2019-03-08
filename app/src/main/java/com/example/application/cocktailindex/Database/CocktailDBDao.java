package com.example.application.cocktailindex.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.application.cocktailindex.Objects.Cocktail;

import java.util.List;

@Dao
public interface CocktailDBDao {

    @Query("SELECT * FROM cocktail")
    List<Cocktail> getAll();

    @Query("SELECT * FROM cocktail WHERE id IN (:cocktailIds)")
    List<Cocktail> loadAllByIds(int[] cocktailIds);


    @Insert
    void insertAll(Cocktail... cocktails);

    @Insert
    void insertOne(Cocktail cocktail);

    @Delete
    void delete(Cocktail cocktail);
}
