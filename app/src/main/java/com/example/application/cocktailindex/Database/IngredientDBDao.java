package com.example.application.cocktailindex.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDBDao {

    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getAll();


    @Query("SELECT * FROM Ingredient WHERE cocktail_fk=:id ")
    List<Ingredient> findById(Integer id);


    @Insert(onConflict = REPLACE)
    void insertOne(List<Ingredient> ingredients);

    @Update
    void updateOne(List<Ingredient> ingredients);

    @Delete
    void delete(List<Ingredient> ingredients);
}
