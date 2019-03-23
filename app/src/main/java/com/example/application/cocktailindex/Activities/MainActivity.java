package com.example.application.cocktailindex.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Fragments.FavoriteFragment;
import com.example.application.cocktailindex.Fragments.IdeaFragment;
import com.example.application.cocktailindex.Fragments.IndexFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
    private IndexFragment fragmentIndex;
    private FavoriteFragment fragmentFavorite;
    private IdeaFragment fragmentIdea;

    // Searching and Cocktails references
    private SearchView searchView;
    private List<Cocktail> cocktailList;
    private List<Cocktail> savedCocktailList;

    // Database
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetupViews();   // FAB & BNV

        // Setup of database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        // Request permission to read storage before loading images
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        } else {
            loadData();
        }

        // Instantiates the 3 base fragments (Index, Favourite & Idea)
        fragmentIndex = new IndexFragment();
        fragmentFavorite = new FavoriteFragment();
        fragmentIdea = new IdeaFragment();

        java.util.Collections.sort(cocktailList);
        // Sets starting fragment TODO: Have user decide the starting fragment, or possibly start at Favourites
        setCurrentFragment(fragmentIndex);

    }

    /**
     * Loads all data from database and adds to the listviews
     */
    private void loadData() {
        // SavedCocktailList is a static list -> CocktalList dynamically changes (removes & adds)
        cocktailList = new ArrayList<>();
        savedCocktailList = new ArrayList<>();

        // Loads all images into the two lists from database
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                savedCocktailList = db.cocktailDBDao().getAll();
                cocktailList.addAll(savedCocktailList);
                java.util.Collections.sort(cocktailList);
            }
        });
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
        if (requestCode == NEW_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {

            // Object and Uri are retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            Uri selectedImage = Uri.parse(data.getStringExtra("image"));
            cocktail.imagePath = selectedImage.toString();

            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.cocktailDBDao().insertOne(cocktail);
                }
            });

            savedCocktailList.add(cocktail);
            cocktailList = savedCocktailList;
        }
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

    public void toggleCocktailFavourite(int id, boolean favourite) {
        boolean found = false;
        try {
            for(final Cocktail cocktail : cocktailList) {
                if(cocktail.id == id) {
                    cocktail.favourite = favourite; // Update whether the cocktail is favourite

                    // Updates the cocktail in the database
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.cocktailDBDao().updateOne(cocktail);
                        }
                    });
                    found = true;
                }
            }
        } catch (ConcurrentModificationException e) {
            Toast.makeText(getApplicationContext(), "Error saving to Database", Toast.LENGTH_SHORT).show();
        }

        if(found) {
            refreshCocktailList();
        }

    }


    public void refreshCocktailList() {
        // Updates the cocktailList
        cocktailList.clear();
        cocktailList.addAll(savedCocktailList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_bar, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        // https://stackoverflow.com/questions/27378981/how-to-use-searchview-in-toolbar-android <--- Search view
        final SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Search","Query Submitted: " + s);
                if(s.isEmpty()) {
                    cocktailList.clear();
                    cocktailList.addAll(savedCocktailList);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("Search","SearchOnQueryTextChanged: " + s);
                Log.d("Search","Main: " + cocktailList.size() + "");

                if(fragmentIndex != null) {
                    fragmentIndex.searchQuery(s.toLowerCase());
                }


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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


    public List<Cocktail> getCocktailList() {
        return cocktailList;
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
