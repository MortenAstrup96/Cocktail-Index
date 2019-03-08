package com.example.application.cocktailindex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

public class CocktailDetailsActivity extends AppCompatActivity {
    private Cocktail cocktail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_details);
        setStatusBarTranslucent(true);


        Intent data = getIntent();
        cocktail = (Cocktail) data.getSerializableExtra("cocktail");

        ImageView imageView = findViewById(R.id.details_section_image_cocktail);
        TextView header = findViewById(R.id.details_section_header);
        TextView ingredients = findViewById(R.id.details_section_ingredients);

        header.setText(cocktail.name);

        ImageButton exitButton = findViewById(R.id.details_section_imagebutton_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        Glide.with(getApplicationContext())
                .load(Uri.parse(cocktail.imagePath))
                .into(imageView);


    }

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
