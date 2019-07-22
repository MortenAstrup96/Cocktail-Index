package dev.astrup.cocktailindex.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import dev.astrup.cocktailindex.Objects.Cocktail;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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
