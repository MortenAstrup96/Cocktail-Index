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
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import dev.astrup.cocktailindex.Utility.SearchableFragment;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        Serializable {

    // On Activity result codes
    public static final int NEW_COCKTAIL_RECIPE = 1;   // Created new cocktail from NewCocktailActivity
    public static final int PRE_POPULATE = 0;
    public static final int UPDATE_COCKTAIL_RECIPE = 2;


    private CocktailSingleton cocktailSingleton;
    private AppDatabase db;

    MainFragmentController fragmentController;

    private FloatingActionButton fab;
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


        cocktailSingleton = CocktailSingleton.getInstance();
        db = AppDatabase.getDatabase(this);

        SetupViews();

        fragmentController = new MainFragmentController(getSupportFragmentManager());

        // Check if we need to display our OnboardingFragment
        if (!prefs.getBoolean(
                "WalkthroughCompleted", false)) {
            Intent intent = new Intent(MainActivity.this, WalkthroughActivity.class);
            startActivityForResult(intent, PRE_POPULATE);
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
                ((SearchableFragment)fragmentController.getCurrentFragment()).searchQuery(s.toLowerCase());
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
        if (requestCode == PRE_POPULATE) {
            if(resultCode == Activity.RESULT_OK) {
                prePopulateDatabase(data);

            } else {
                Intent intent = new Intent(MainActivity.this, WalkthroughActivity.class);
                startActivityForResult(intent, PRE_POPULATE);
                Toast.makeText(this, "Please complete the introduction",
                        Toast.LENGTH_SHORT).show();
            }
            fragmentController.updateFragments();

        }
        if (requestCode == NEW_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {

            // Cocktail is retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            cocktailSingleton.addCocktail(cocktail, db);

            fragmentController.updateFragments();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cocktail.name);
            bundle.putString("IngredientSize", "" + cocktail.ingredients.size());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        } else if(requestCode == UPDATE_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {             // Object and Uri are retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");

            cocktailSingleton.updateCocktail(cocktail, db);
        }

    }

    private void prePopulateDatabase(Intent data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("WalkthroughCompleted", true).apply();

        boolean isMetric = data.getBooleanExtra("isMetric", false);
        boolean shouldPrePopulate = data.getBooleanExtra("shouldPrePopulate", false);

        if(shouldPrePopulate) {
            new PopulateDatabase().populateDatabase(getApplicationContext(), isMetric);
        }
        prefs.edit().putBoolean("metric", isMetric).apply();
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

    public void setFabVisibility(boolean isVisible) {
        if(isVisible) fab.show();
        else fab.hide();
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
                fragmentController.showFavouriteFragment();
                fab.show();
                break;
            case R.id.index:
                searchItem.setVisible(true);
                fragmentController.showIndexFragment();
                fab.show();
                break;
            case R.id.ideas:
                searchItem.setVisible(false);
                fragmentController.showIdeaFragment();
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

}



