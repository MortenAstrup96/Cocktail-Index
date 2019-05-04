package com.example.application.cocktailindex.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.application.cocktailindex.Objects.Cocktail;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CocktailDBDao {

    @Query("SELECT * FROM cocktail")
    List<Cocktail> getAll();

    @Insert
    void insertAll(Cocktail... cocktails);

    @Insert(onConflict = REPLACE)
    void insertOne(Cocktail cocktail);

    @Update
    void updateOne(Cocktail cocktail);

    @Delete
    void delete(Cocktail cocktail);
}
