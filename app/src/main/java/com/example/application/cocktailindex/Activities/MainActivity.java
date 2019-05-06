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

import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Fragments.FavoriteFragment;
import com.example.application.cocktailindex.Fragments.IdeaFragment;
import com.example.application.cocktailindex.Fragments.IndexFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.application.cocktailindex.Activities.CocktailDetailsActivity.UPDATE_COCKTAIL_RECIPE;


public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        IndexFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener,
        IdeaFragment.OnFragmentInteractionListener,
        Serializable {

    // On Activity result codes
    public static final int NEW_COCKTAIL_RECIPE = 1;   // Created new cocktail from NewCocktailActivity

    private CocktailSingleton cocktailSingleton;
    private AppDatabase db;

    // Fragments
    private IndexFragment fragmentIndex;
    private FavoriteFragment fragmentFavorite;
    private IdeaFragment fragmentIdea;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cocktailSingleton = CocktailSingleton.getInstance();

        SetupViews();
        db = AppDatabase.getDatabase(this);

        // Request permission to read storage before loading images
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        } else {
            updateFromDatabase();
        }

        // Instantiates the 3 base fragments (Index, Favourite & Idea)
        fragmentIndex = new IndexFragment();
        fragmentFavorite = new FavoriteFragment();
        fragmentIdea = new IdeaFragment();

        setCurrentFragment(fragmentIndex);
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

            // Cocktail is retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            cocktailSingleton.addCocktail(cocktail, db);

            updateFragmentLists();

        } else if(requestCode == UPDATE_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {             // Object and Uri are retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");

            cocktailSingleton.updateCocktail(cocktail, db);
        }
    }

    public void updateSpecificCocktailForResult(Cocktail cocktail) {
        Intent intent = new Intent(this, NewCocktailActivity.class);
        intent.putExtra("cocktail", cocktail);
        startActivityForResult(intent, UPDATE_COCKTAIL_RECIPE);
    }

    /**
     * Sets up the different views when creating the activity
     *
     *  - Bottom Navigation Bar
     *  - Floating Action Button
     */
    private void SetupViews() {
        // Navigation in the bottom
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        // Setup of FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCocktailActivity.class);
                startActivityForResult(intent, NEW_COCKTAIL_RECIPE);
            }
        });
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

    public void setFabVisibility(boolean isVisible) {
        if(isVisible) fab.show();
        else fab.hide();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateFragmentLists();
    }

    private void updateFromDatabase() {
        // Loads all images into the two lists from database
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cocktailSingleton.setCocktailList(db.cocktailDBDao().getAll());

                try {
                    for(Cocktail c : cocktailSingleton.getCocktailList()) {
                        c.setIngredients((ArrayList<Ingredient>)db.ingredientDBDao().findById(c.id));
                    }
                } catch (ConcurrentModificationException e) {
                    Log.e("Concurrent", "Concurrent Error!!");
                }

            }
        });
    }


    /**
     * Attempts to update all the fragment lists, some will be null
     */
    public void updateFragmentLists() {
        if(fragmentFavorite != null) fragmentFavorite.updateList();
        if(fragmentIndex != null) fragmentIndex.updateList();
        if(fragmentIdea != null) /*fragmentIdea.updateList()*/;
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

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_bar, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        // https://stackoverflow.com/questions/27378981/how-to-use-searchview-in-toolbar-android <--- Search view
        final SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        // Searching and Cocktails references
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Search","Query Submitted: " + s);
                if(s.isEmpty()) {
                    cocktailSingleton.getCocktailList().clear();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
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
        else if(id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_feedback) {
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"cocktail@astrup.dev"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback on CocktailIndex");

            /* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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
