package dev.astrup.cocktailindex.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;

@Database(entities = {Cocktail.class, Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CocktailDBDao cocktailDBDao();
    public abstract IngredientDBDao ingredientDBDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "database")
                                    .allowMainThreadQueries()
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }

        return INSTANCE;

    }
}
