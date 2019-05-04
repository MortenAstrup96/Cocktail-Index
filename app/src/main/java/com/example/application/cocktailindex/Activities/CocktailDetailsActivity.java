package com.example.application.cocktailindex.Activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IngredientDetailsAdapter;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CocktailDetailsActivity extends AppCompatActivity {

    public static final int UPDATE_COCKTAIL_RECIPE = 2;
    private Cocktail cocktail;

    private ImageView imageView;

    private ImageView iconIngredients;
    private ImageView iconRecipe;
    private ImageView iconComments;

    private Button buttonEdit;

    private IngredientDetailsAdapter mAdapter;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_details);
        setStatusBarTranslucent(true);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        // Gets the specific cocktail
        Intent data = getIntent();
        cocktail = (Cocktail) data.getSerializableExtra("cocktail");

        setupViews();

        // Set the image view to the decired picture
        Glide.with(getApplicationContext())
                .load(Uri.parse(cocktail.imagePath))
                .into(imageView);
    }

    /**
     * Sets up all of the basic views
     */
    private void setupViews() {
        imageView = findViewById(R.id.details_section_image_cocktail);
        TextView header = findViewById(R.id.details_section_header);
        RecyclerView recyclerView = findViewById(R.id.details_section_ingredients);
        TextView recipe = findViewById(R.id.details_section_recipe);
        TextView comments = findViewById(R.id.details_section_comments);
        buttonEdit = findViewById(R.id.cocktaildetails_button_edit);

        iconIngredients = findViewById(R.id.details_section_ingredients_icon);
        iconRecipe = findViewById(R.id.details_section_recipe_icon);
        iconComments = findViewById(R.id.details_section_comments_icon);

        header.setText(cocktail.name);
        recipe.setText(cocktail.recipe);
        comments.setText(cocktail.comments);

        // Recyclerview to show ingredients here
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IngredientDetailsAdapter((ArrayList<Ingredient>)cocktail.ingredients, this);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_COCKTAIL_RECIPE && resultCode == Activity.RESULT_OK) {
            // Object and Uri are retrieved from NewCocktailActivity
            cocktail = (Cocktail) data.getSerializableExtra("cocktail");
            Uri selectedImage = Uri.parse(data.getStringExtra("image"));
            cocktail.imagePath = selectedImage.toString();

            // Database Query
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.ingredientDBDao().insertOne(cocktail.ingredients);
                    db.cocktailDBDao().updateOne(cocktail);
                }
            });
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
