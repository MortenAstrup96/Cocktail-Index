package com.cocktail.application.cocktailindex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocktail.application.cocktailindex.Database.AppDatabase;
import com.cocktail.application.cocktailindex.Objects.Cocktail;
import com.cocktail.application.cocktailindex.Objects.Ingredient;
import com.cocktail.application.cocktailindex.R;
import com.cocktail.application.cocktailindex.RecyclerviewAdapters.IngredientDetailsAdapter;
import com.cocktail.application.cocktailindex.Utility.CocktailSingleton;

import java.util.ArrayList;

public class IdeaActivity extends AppCompatActivity {

    public static final int UPDATE_COCKTAIL_RECIPE = 2;
    private Cocktail cocktail;

    private AppDatabase db;
    private CocktailSingleton cocktailSingleton = CocktailSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_idea_details);

        db = AppDatabase.getDatabase(this);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Gets the specific cocktail
        Intent data = getIntent();
        cocktail = (Cocktail) data.getSerializableExtra("cocktail");
        cocktailSingleton.cocktailDetailsCocktail(cocktail);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Sets up all of the basic views
     */
    private void setupViews() {
        //imageView = findViewById(R.id.idea_section_image_cocktail);
        TextView header = findViewById(R.id.idea_section_header);
        RecyclerView recyclerView = findViewById(R.id.idea_section_ingredients);
        TextView recipe = findViewById(R.id.idea_section_recipe);
        TextView comments = findViewById(R.id.idea_section_comments);
        Button buttonEdit = findViewById(R.id.cocktailidea_button_edit);


        ImageView iconIngredients = findViewById(R.id.idea_section_ingredients_icon);
        ImageView iconRecipe = findViewById(R.id.idea_section_recipe_icon);
        ImageView iconComments = findViewById(R.id.idea_section_comments_icon);

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


        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdeaActivity.this, NewCocktailActivity.class);
                intent.putExtra("cocktail", cocktail);
                startActivityForResult(intent, UPDATE_COCKTAIL_RECIPE);
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();//finish your activity
        }
        return super.onOptionsItemSelected(item);
    }
}
