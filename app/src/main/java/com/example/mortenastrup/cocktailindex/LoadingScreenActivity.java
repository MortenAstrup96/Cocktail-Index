package com.example.mortenastrup.cocktailindex;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mortenastrup.cocktailindex.Database.AppDatabase;
import com.example.mortenastrup.cocktailindex.Objects.Cocktail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadingScreenActivity extends AppCompatActivity {

    private List<Cocktail> cocktailList;
    private Map<Integer, Bitmap> imageMap;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        cocktailList = new ArrayList<>();
        imageMap = new HashMap<>();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {

                cocktailList = db.cocktailDBDao().getAll();
                imageMap = new HashMap<>(); // TODO: Possibly use SparseArray - Less memory, but slower

                for(Cocktail cocktail : cocktailList) {
                    String path = cocktail.imagePath;
                    int id = cocktail.id;

                    Log.d("LoadImages", "Id, Path: " + id + " - " + path);

                    Bitmap image = retrieveImageFromDirectory(path, id);
                    Log.d("LoadImages", "Height: " + image.getHeight());

                    // Put the image in the imageMap - Else put custom drawable in place
                    if(image != null) {
                        imageMap.put(id, image);
                    } else {
                        image = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_launcher_background);
                        imageMap.put(id, image);
                    }
                }

                // Sets global Application lists to the loaded images
                ((CocktailIndex)getApplication()).setCocktailList(cocktailList);
                ((CocktailIndex)getApplication()).setImageMap(imageMap);


                Intent myIntent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes loading screen from stack
                LoadingScreenActivity.this.startActivity(myIntent);
            }
        });


    }

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public Map<Integer, Bitmap> getImageMap() {
        return imageMap;
    }

    /**
     * Takes the path and unique ID from an image saved in the database and retrieves the
     * actual image from internal storage.
     *
     * @param path Path of the image
     * @param id The image ID
     * @return  Returns a Bitmap with the desired picture
     */
    private Bitmap retrieveImageFromDirectory(String path, int id) {
        try {
            Log.d("File", "Loading from directory: " + path);

            File file = new File(path, "image_" + id + ".jpeg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
