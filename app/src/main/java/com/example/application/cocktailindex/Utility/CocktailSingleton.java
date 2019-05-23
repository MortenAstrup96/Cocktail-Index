package com.example.application.cocktailindex.Utility;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CocktailSingleton {
    private static final CocktailSingleton ourInstance = new CocktailSingleton();

    public static CocktailSingleton getInstance() {
        return ourInstance;
    }
    private List<Cocktail> cocktailList;
    private List<Cocktail> favourites;
    private List<Cocktail> ideas;
    private List<Ingredient> ingredients;
    private Map<String, Integer> ingredientMap;

    private Cocktail tempCocktailDetailsCocktail;

    private CocktailSingleton() {
        cocktailList = new ArrayList<>();
        favourites = new ArrayList<>();
        ideas = new ArrayList<>();
        ingredients = new ArrayList<>();
        ingredientMap = new HashMap<>();
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }

    public void setIngredientList(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        for (Ingredient i : ingredients) {
            if (ingredientMap.containsKey(i.getIngredient())) {
                ingredientMap.put(i.getIngredient(), ingredientMap.get(i.getIngredient())+1);
            } else {
                ingredientMap.put(i.getIngredient(), 1);
            }
        }

        for(String key : ingredientMap.keySet())  {
            Log.d("Ingredients", "Key: " + key);
            Log.d("Ingredients", "Value: " + ingredientMap.get(key) + "\n");
        }
    }

    public Map<String, Integer> getIngredientMap() {
        return ingredientMap;
    }

    public String[] getIngredientsArrayWithAmount() {
        String[] array = new String[ingredientMap.size()];

        int count = 0;
        for (String s : ingredientMap.keySet()) {
            array[count] = s;
            count++;
        }

        return array;
    }

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public List<Cocktail> getFavourites() {
        favourites.clear();

        for(Cocktail cocktail : cocktailList) {
            if(cocktail.favourite) {
                favourites.add(cocktail);
            }
        }
        java.util.Collections.sort(favourites);
        return favourites;
    }

    public void setFavourite(final Cocktail cocktail, final AppDatabase db) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.cocktailDBDao().updateOne(cocktail);
            }
        });
    }

    public void setFavouriteById(int id, final AppDatabase db, boolean b) {
        for(Cocktail c : cocktailList) {
            if(c.id == id) {
                c.favourite = b;
                setFavourite(c, db);
            }
        }
    }

    public void removeByID(int id, final AppDatabase db) {
        final Cocktail deletion;
        for(Cocktail c : cocktailList) {
            if(c.id == id) {
                deletion = c;
                favourites.remove(c);
                cocktailList.remove(c);

                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.ingredientDBDao().delete(deletion.ingredients);
                        db.cocktailDBDao().delete(deletion);
                    }
                });
                return;
            }
        }
    }

    public List<Cocktail> getIdeas() {
        ideas.clear();

        for(Cocktail cocktail : cocktailList) {
            if(cocktail.idea) {
                ideas.add(cocktail);
            }
        }
        java.util.Collections.sort(ideas);
        return ideas;
    }

    public void setIdeas(List<Cocktail> ideas) {
        this.ideas = ideas;
    }

    public void addCocktail(final Cocktail cocktail, final AppDatabase db) {
        cocktailList.add(cocktail);
        // Insertion into Database
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.cocktailDBDao().insertOne(cocktail);
                db.ingredientDBDao().insertOne(cocktail.ingredients);
            }
        });
    }

    public void updateCocktail(final Cocktail cocktail, final AppDatabase db) {
        // Removes prevous cocktail object and replaces with ned
        Cocktail removeCocktail = null;
        for(Cocktail c : getCocktailList()) {
            if(c.id == cocktail.id) {
                removeCocktail = c;
            }
        }
        if(removeCocktail != null)  {
            getCocktailList().remove(removeCocktail);
            getCocktailList().add(cocktail);
            // Database Query
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.ingredientDBDao().delete(tempCocktailDetailsCocktail.ingredients);

                    db.cocktailDBDao().updateOne(cocktail);
                    db.ingredientDBDao().insertOne(cocktail.ingredients);

                }
            });
        }


    }

    public void cocktailDetailsCocktail(Cocktail cocktail) {
        this.tempCocktailDetailsCocktail = cocktail;
    }


}
