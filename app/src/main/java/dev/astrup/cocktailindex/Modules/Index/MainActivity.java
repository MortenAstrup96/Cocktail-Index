package dev.astrup.cocktailindex.Modules.Index;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import dev.astrup.cocktailindex.Modules.AppWalkthrough.PopulateDatabase;
import dev.astrup.cocktailindex.Modules.AppWalkthrough.WalkthroughActivity;
import dev.astrup.cocktailindex.Modules.Creation.NewCocktailActivity;
import dev.astrup.cocktailindex.Modules.Various.AboutActivity;
import dev.astrup.cocktailindex.Modules.Various.SettingsActivity;
import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailSingleton;
import dev.astrup.cocktailindex.Utility.GdprHelper;

import java.io.Serializable;

import static dev.astrup.cocktailindex.Modules.Details.CocktailDetailsActivity.UPDATE_COCKTAIL_RECIPE;


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
    private SearchManager searchManager;
    private MenuItem searchItem;

    // Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        GdprHelper gdprHelper = new GdprHelper(this);
        gdprHelper.initialise();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);


        cocktailSingleton = CocktailSingleton.getInstance();
        db = AppDatabase.getDatabase(this);

        SetupViews();

        // Instantiates the 3 base fragments (Index, Favourite & Idea)
        fragmentIndex = new IndexFragment();
        fragmentFavorite = new FavoriteFragment();
        fragmentIdea = new IdeaFragment();

        setCurrentFragment(fragmentIndex);

        // Check if we need to display our OnboardingFragment
        if (!sharedPreferences.getBoolean(
                "WalkthroughCompleted", false)) {
            Intent intent = new Intent(MainActivity.this, WalkthroughActivity.class);
            startActivity(intent);
            sharedPreferences.edit().putBoolean("WalkthroughCompleted", true).apply();
        }

    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuInflater menuInflater = getMenuInflater();
        setupSearch(menu, menuInflater);
        return super.onCreateOptionsMenu(menu);
     }


    private void setupSearch(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search_bar, menu);
        searchItem = menu.findItem(R.id.action_search);

        // https://stackoverflow.com/questions/27378981/how-to-use-searchview-in-toolbar-android <--- Search view
        searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        // Searching and Cocktails references
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search by cocktail or ingredient");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s.isEmpty()) {
                    cocktailSingleton.getIndexList().clear();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(fragmentIndex != null) {
                    if(fragmentIndex.isVisible()) fragmentIndex.searchQuery(s.toLowerCase());
                    if(fragmentFavorite.isVisible()) fragmentFavorite.searchQuery(s.toLowerCase());
                }
                return false;
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

            // Cocktail is retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            cocktailSingleton.addCocktail(cocktail, db);

            updateFragmentLists();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cocktail.name);
            bundle.putString("IngredientSize", "" + cocktail.ingredients.size());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

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

    /**
     * Attempts to update all the fragment lists, some will be null
     */
    public void updateFragmentLists() {
        if(fragmentFavorite != null) fragmentFavorite.updateList();
        if(fragmentIndex != null) fragmentIndex.updateList();
        if(fragmentIdea != null) fragmentIdea.updateList();
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
                searchItem.setVisible(true);
                searchItem.collapseActionView();
                setCurrentFragment(fragmentFavorite);
                fab.show();
                break;
            case R.id.index:
                searchItem.setVisible(true);
                searchItem.collapseActionView();
                setCurrentFragment(fragmentIndex);
                fab.show();
                break;
            case R.id.ideas:
                searchItem.setVisible(false);
                setCurrentFragment(fragmentIdea);
                fab.show();
                break;
            default:
                return false;
        }
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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



