package com.example.mortenastrup.cocktailindex;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mortenastrup.cocktailindex.Database.AppDatabase;
import com.example.mortenastrup.cocktailindex.Fragments.FavoriteFragment;
import com.example.mortenastrup.cocktailindex.Fragments.IdeaFragment;
import com.example.mortenastrup.cocktailindex.Fragments.IndexFragment;
import com.example.mortenastrup.cocktailindex.Objects.Cocktail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.System.in;


public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        IndexFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener,
        IdeaFragment.OnFragmentInteractionListener,
        Serializable {

    // On Activity result codes
    public static final int NEW_COCKTAIL_RECIPE = 1;   // Created new cocktail from NewCocktailActivity

    // Navigation in the bottom
    private BottomNavigationView bottomNavigationView;

    // Fragments
    private Fragment fragmentIndex;
    private Fragment fragmentFavorite;
    private Fragment fragmentIdea;

    private List<Cocktail> cocktailList;
    private Map<Integer, Bitmap> imageMap;

    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get lists from global application class
        cocktailList = ((CocktailIndex)getApplication()).getCocktailList();
        imageMap = ((CocktailIndex)getApplication()).getImageMap();

        SetupViews();   // FAB & BNV

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();



        // Instantiates the 3 base fragments (Index, Favourite & Idea)
        fragmentIndex = new IndexFragment();
        fragmentFavorite = new FavoriteFragment();
        fragmentIdea = new IdeaFragment();

        // Sets starting fragment TODO: Have user decide the starting fragment, or possibly start at Favourites
        setCurrentFragment(fragmentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public Map<Integer, Bitmap> getCocktailImages() {
        return imageMap;
    }

    /**
     * OnActivityResult has 1 type of result; New Cocktail Recipe.
     * The cocktail object is send fram NewCocktailActivity (Image = null) & the Image is sent
     * seperately as a Uri. The image is found using Uri and set in the object.
     *
     * The object is then added to the global Cocktail List (CocktailIndex).
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Cocktail cocktail;

        //Detects request codes
        if(requestCode==NEW_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {

            // Object and Uri are retrieved from NewCocktailActivity
            cocktail = (Cocktail)data.getSerializableExtra("cocktail");
            Uri selectedImage = Uri.parse(data.getStringExtra("image"));


            try {
                // Decodes the image and sets the cocktail object to this.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                String path = saveToInternalStorage(bitmap, cocktail.id);
                saveToThumbnailInternalStorage(bitmap, cocktail.id);    // Saves a smaller version
                cocktail.imagePath = path;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.cocktailDBDao().insertOne(cocktail);
                }
            });

            cocktailList.add(cocktail);
        }
    }


    /**
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     * @param bitmapImage
     * @return
     */
    private String saveToInternalStorage(Bitmap bitmapImage, int id){
        // Decrease size
        Bitmap scaledImage;

        Double height = (double) bitmapImage.getHeight();
        Double width = (double) bitmapImage.getWidth();
        Double difference = height/width;

        width = 800.0;
        height = width*difference;
        int heightInt = height.intValue();
        int widthInt = width.intValue();
        scaledImage = Bitmap.createScaledBitmap(bitmapImage, widthInt, heightInt, false);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"image_" + id + ".jpeg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            scaledImage.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    /**
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     * @param bitmapImage
     * @return
     */
    private String saveToThumbnailInternalStorage(Bitmap bitmapImage, int id){
        // Decrease size
        Bitmap scaledImage;

        Double height = (double) bitmapImage.getHeight();
        Double width = (double) bitmapImage.getWidth();
        Double difference = height/width;

        width = 200.0;
        height = width*difference;
        int heightInt = height.intValue();
        int widthInt = width.intValue();
        scaledImage = Bitmap.createScaledBitmap(bitmapImage, widthInt, heightInt, false);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"thumbnail_" + id + ".jpeg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            scaledImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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

    /**
     * Switches the current fragment with the parameter fragment
     * @param fragment  The fragment to be switched to
     */
    private void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Sets up the different views when creating the activity
     *
     *  - Bottom Navigation Bar
     *  - Floating Action Button
     */
    private void SetupViews() {
        // Setup of BottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);


        // Setup of FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCocktailActivity.class);
                startActivityForResult(intent, NEW_COCKTAIL_RECIPE);
            }
        });
    }

    /**
     * Bottom Navigation Bar
     *
     * This activity decides what to do when selecting the various buttons in BottomNavigationBar.
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favorites:
                setCurrentFragment(fragmentFavorite);
                break;
            case R.id.index:
                setCurrentFragment(fragmentIndex);
                break;
            case R.id.ideas:
                setCurrentFragment(fragmentIdea);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** The following methods can be called from fragments, doing specific tasks (not in use)*/
    @Override
    public void indexFragmentInteractionListener(String task) {

    }

    @Override
    public void favoriteFragmentInteractionListener(String task) {

    }

    @Override
    public void ideaFragmentInteractionListener(String task) {

    }

}
