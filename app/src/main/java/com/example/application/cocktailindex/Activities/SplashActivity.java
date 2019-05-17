package com.example.application.cocktailindex.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class SplashActivity extends AppCompatActivity {
    private AppDatabase db;
    private CocktailSingleton cocktailSingleton = CocktailSingleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        db = AppDatabase.getDatabase(this);

        // Request permission to read storage before loading images
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        } else {
            new PreloadDatabase().execute(this);
        }
    }

    private class PreloadDatabase extends AsyncTask<Context, Void, String> {
        @Override
        protected String doInBackground(Context... contexts) {
            cocktailSingleton.setCocktailList(db.cocktailDBDao().getAll());

            try {
                for(Cocktail c : cocktailSingleton.getCocktailList()) {
                    c.setIngredients((ArrayList<Ingredient>)db.ingredientDBDao().findById(c.id));
                }
            } catch (ConcurrentModificationException e) {
                Log.e("Concurrent", "Concurrent Error!!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            finish();
        }
    }
}



