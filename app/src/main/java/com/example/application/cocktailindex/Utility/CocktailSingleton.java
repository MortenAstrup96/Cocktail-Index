package com.example.application.cocktailindex.Utility;

import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;

import java.util.ArrayList;
import java.util.List;
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

    private CocktailSingleton() {
        cocktailList = new ArrayList<>();
        favourites = new ArrayList<>();
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
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

    public List<Cocktail> getIdeas() {
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
                    db.ingredientDBDao().delete(cocktail.ingredients);
                    db.cocktailDBDao().delete(cocktail);

                    db.cocktailDBDao().insertOne(cocktail);
                    db.ingredientDBDao().insertOne(cocktail.ingredients);

                }
            });
        }


    }


}
