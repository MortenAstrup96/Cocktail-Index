package com.example.application.cocktailindex.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.CustomPagerAdapter;
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IngredientDetailsAdapter;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.util.ArrayList;

public class CocktailDetailsActivity extends AppCompatActivity {

    public static final int UPDATE_COCKTAIL_RECIPE = 2;
    private Cocktail cocktail;

    CustomPagerAdapter pagerAdapter;

    private AppDatabase db;
    private CocktailSingleton cocktailSingleton = CocktailSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_details);
        setStatusBarTranslucent(true);


        db = AppDatabase.getDatabase(this);

        // Gets the specific cocktail
        Intent data = getIntent();
        cocktail = (Cocktail) data.getSerializableExtra("cocktail");
        cocktailSingleton.cocktailDetailsCocktail(cocktail);

        setupViews();


        if(cocktail.imagePath != null) {
            ViewPager pager = findViewById(R.id.photos_viewpager);
            pagerAdapter = new CustomPagerAdapter(getApplicationContext(), cocktail.imagePath);
            pager.setAdapter(pagerAdapter);
            if(pagerAdapter.getCount() > 1) {
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
                tabLayout.setupWithViewPager(pager, true);
                tabLayout.setClickable(false);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pagerAdapter == null) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.ic_nopicture)
                    .centerCrop()
                    .into(((ImageView)findViewById(R.id.details_section_imageview_placeholder)));
        } else {
            Glide.with(getApplicationContext())
                    .clear(((ImageView)findViewById(R.id.details_section_imageview_placeholder)));
        }
    }

    /**
     * Sets up all of the basic views
     */
    private void setupViews() {
        //imageView = findViewById(R.id.details_section_image_cocktail);
        TextView header = findViewById(R.id.details_section_header);
        RecyclerView recyclerView = findViewById(R.id.details_section_ingredients);
        TextView recipe = findViewById(R.id.details_section_recipe);
        TextView comments = findViewById(R.id.details_section_comments);
        Button buttonEdit = findViewById(R.id.cocktaildetails_button_edit);
        CheckBox favourite = findViewById(R.id.details_section_image_favourite);

        favourite.setChecked(cocktail.favourite);
        favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                shortVibration();

                cocktailSingleton.setFavouriteById(cocktail.id, db, b);
            }
        });

        ImageView iconIngredients = findViewById(R.id.details_section_ingredients_icon);
        ImageView iconRecipe = findViewById(R.id.details_section_recipe_icon);
        ImageView iconComments = findViewById(R.id.details_section_comments_icon);

        header.setText(cocktail.name);
        recipe.setText(cocktail.recipe);
        comments.setText(cocktail.comments);

        // Recyclerview to show ingredients here
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        IngredientDetailsAdapter mAdapter = new IngredientDetailsAdapter((ArrayList<Ingredient>) cocktail.ingredients, this);
        recyclerView.setAdapter(mAdapter);


        // Ternary operators - If empty = INVISIBLE else VISIBLE

        iconIngredients.setVisibility(cocktail.ingredients.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        iconRecipe.setVisibility(cocktail.recipe.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        iconComments.setVisibility(cocktail.comments.isEmpty() ? View.INVISIBLE : View.VISIBLE);


        ImageButton exitButton = findViewById(R.id.details_section_imagebutton_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CocktailDetailsActivity.this, NewCocktailActivity.class);
                intent.putExtra("cocktail", cocktail);
                startActivityForResult(intent, UPDATE_COCKTAIL_RECIPE);
            }
        });

        final ImageButton spinnerButton = findViewById(R.id.index_section_spinner);
        spinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(CocktailDetailsActivity.this, spinnerButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_details, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    private void shortVibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(5);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            cocktailSingleton.updateCocktail(cocktail, db);
            setupViews();
        }
    }

    /**
     * When pressing back button, finish the activity with a fade out animation
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.fade_out);

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }



}
