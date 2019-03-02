package com.example.mortenastrup.cocktailindex.Database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

public interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    List<Cocktail> getAll();

    @Query("SELECT * FROM cocktail WHERE id IN (:cocktailIds)")
    List<Cocktail> loadAllByIds(int[] cocktailIds);

    @Insert
    void insertAll(Cocktail... cocktails);

    @Insert
    void insertSingle(Cocktail cocktail);

    @Delete
    void delete(Cocktail cocktail);
}
