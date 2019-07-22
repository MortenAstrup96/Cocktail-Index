package dev.astrup.cocktailindex.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import dev.astrup.cocktailindex.Objects.Ingredient;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDBDao {

    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getAll();


    @Query("SELECT * FROM Ingredient WHERE cocktail_fk=:id ")
    List<Ingredient> findById(Integer id);


    @Insert(onConflict = REPLACE)
    void insertOne(List<Ingredient> ingredients);

    @Update(onConflict = REPLACE)
    void updateOne(List<Ingredient> ingredients);

    @Delete
    void delete(List<Ingredient> ingredients);
}
