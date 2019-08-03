package dev.astrup.cocktailindex.Utility;

import android.util.Log;

import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CocktailSingleton {
    private static final CocktailSingleton ourInstance = new CocktailSingleton();

    public static CocktailSingleton getInstance() {
        return ourInstance;
    }
    private List<Cocktail> cocktailList;
    private List<Cocktail> favourites;
    private List<Cocktail> ideas;
    private List<Cocktail> indexList;
    private List<Ingredient> ingredients;
    private Map<String, Integer> ingredientMap;

    private Cocktail tempCocktailDetailsCocktail;

    private CocktailSingleton() {
        cocktailList = new ArrayList<>();
        favourites = new ArrayList<>();
        ideas = new ArrayList<>();
        indexList = new ArrayList<>();
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

    public List<Cocktail> getIndexList() {
        indexList.clear();

        for(Cocktail cocktail : cocktailList) {
            if(!cocktail.idea) {
                indexList.add(cocktail);
            }
        }
        java.util.Collections.sort(indexList);
        return indexList;
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
        updateLists();
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
        updateLists();
    }

    public void removeCocktail(final Cocktail cocktail, final AppDatabase db) {
        cocktailList.remove(cocktail);
        // Insertion into Database
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.ingredientDBDao().delete(cocktail.ingredients);
                db.cocktailDBDao().delete(cocktail);
            }
        });
        updateLists();
    }

    private void updateLists() {
        getIndexList();
        getFavourites();
        getIdeas();
    }

    public void updateCocktail(final Cocktail cocktail, final AppDatabase db) {
        // Removes prevous cocktail object and replaces with ned
        Cocktail removeCocktail = null;
        for(Cocktail c : getCocktailList()) {
            if(c.id == cocktail.id) {
                removeCocktail = c;
            }
        }
        final Cocktail theCocktail = removeCocktail;
        if(theCocktail != null)  {
            getCocktailList().remove(removeCocktail);
            getCocktailList().add(cocktail);
            // Database Query
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.ingredientDBDao().delete(theCocktail.ingredients);
                    db.cocktailDBDao().updateOne(cocktail);
                    db.ingredientDBDao().insertOne(cocktail.ingredients);

                }
            });
        }
        updateLists();
    }

    public void cocktailDetailsCocktail(Cocktail cocktail) {
        this.tempCocktailDetailsCocktail = cocktail;
    }


}
