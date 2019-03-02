package com.example.mortenastrup.cocktailindex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mortenastrup.cocktailindex.Database.Cocktail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class NewCocktailActivity extends AppCompatActivity implements Serializable {

    // On Activity result codes
    public static final int GET_FROM_GALLERY = 3;   // Getting images from gallery

    private Bitmap bitmap;
    private Uri selectedImage;

    // Different views in the activity
    private ImageView thumbnail;
    private EditText cocktailNameView;
    private EditText recipeEditText;

    private Button selectImageButton;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

        initializeViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        // When clicking selectImageButton the users gallery is opened to select an image.
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, GET_FROM_GALLERY); // Result is a Uri code for image
            }
        });

        // When finishing a cocktail object is made with the information and sent to MainActivity
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cocktailNameView.getText().toString();
                String recipe = recipeEditText.getText().toString();


                Cocktail cocktail = new Cocktail(name, recipe, null, null, false, false);
                Intent intent = new Intent();
                intent.putExtra("cocktail", cocktail);
                intent.putExtra("image", selectedImage.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initializeViews() {
        thumbnail = findViewById(R.id.newCocktail_imageView_thumbnail);
        cocktailNameView = findViewById(R.id.newCocktail_edit_name);
        recipeEditText = findViewById(R.id.newCocktail_edit_recipe);

        selectImageButton = findViewById(R.id.newCocktail_button_chooseImage);
        finishButton = findViewById(R.id.newCocktail_button_finish);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            this.selectedImage = selectedImage;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            thumbnail.setImageBitmap(bitmap);
        }
    }
}
