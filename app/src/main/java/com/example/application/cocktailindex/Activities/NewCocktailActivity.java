package com.example.application.cocktailindex.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.BuildConfig;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class NewCocktailActivity extends AppCompatActivity implements Serializable {

    // On Activity result codes
    public static final int GET_FROM_GALLERY = 3;   // Getting images from gallery
    public static final int ACTION_IMAGE_CAPTURE = 4;   // Getting images from gallery

    private Uri selectedImage;

    // Different views in the activity
    private ImageView thumbnail;
    private EditText cocktailNameView;
    private EditText recipeEditText;

    private EditText ingredient1EditText;
    private EditText ingredient2EditText;
    private EditText amount1EditText;
    private EditText amount2EditText;

    private ImageButton selectImageButton;
    private ImageButton takeImageButton;
    private Button finishButton;
    private Button cropButton;

    private boolean pictureSelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

        initializeViews();
        setOnClickListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Resultcode", resultCode + "");

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            this.selectedImage = selectedImage;

            Glide.with(this)
                    .load(selectedImage)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);

        } else if(requestCode==ACTION_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Glide.with(this)
                    .load(selectedImage)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);
        }

        if(resultCode == Activity.RESULT_OK) {
            pictureSelected = true;
        }
    }

    /**
     * Sets onClickListeners for choosing gallery or taking an image with the camera app
     */
    private void setOnClickListeners() {
        // When clicking selectImageButton the users gallery is opened to select an image.
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sets an intent to find and open the desired image
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_FROM_GALLERY); // Result is a Uri code for image
            }
        });

        // Takes an image with the camera instead of choosing one TODO: After requesting permission app needs to restart!
        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                selectedImage = getPhotoFile(getApplicationContext());
                camera.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                camera.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                camera.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(NewCocktailActivity.this, new String[] {Manifest.permission.CAMERA}, ACTION_IMAGE_CAPTURE);
                }else {
                    startActivityForResult(camera, ACTION_IMAGE_CAPTURE);
                }
            }
        });



        // When finishing a cocktail object is made with the information and sent to MainActivity
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cocktailNameView.getText().toString();
                String recipe = recipeEditText.getText().toString();

                String ingredients = buildIngredientList();

                Cocktail cocktail = new Cocktail(name, ingredients, recipe, "Comments", null, false, false);

                Intent intent = new Intent();
                intent.putExtra("cocktail", cocktail); // TODO: Might be causing problems due to serialization
                intent.putExtra("image", selectedImage.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pictureSelected) {
                    // TODO: Add https://github.com/igreenwood/SimpleCropView to CROP images!!
                }
            }
        });
    }

    /**
     * Takes the String Array and builds it into a single string TODO BuildIngredients not yet fully functional!
     * @return
     */
    private String buildIngredientList() {
        String ingredient1 = ingredient1EditText.getText().toString();
        String ingredient2 = ingredient2EditText.getText().toString();
        String amount1 = amount1EditText.getText().toString();
        String amount2 = amount2EditText.getText().toString();

        String ingredientCondensed =
                amount1 + "oz. " + ingredient1 + "\n" +
                        amount2 + "oz. " + ingredient2;

        return ingredientCondensed;
    }

    private void initializeViews() {
        thumbnail = findViewById(R.id.newCocktail_imageView_thumbnail);
        cocktailNameView = findViewById(R.id.newCocktail_edit_name);
        recipeEditText = findViewById(R.id.newCocktail_edit_recipe);

        ingredient1EditText = findViewById(R.id.newCocktail_edit_ingredient);
        amount1EditText = findViewById(R.id.newCocktail_edit_amount);
        ingredient2EditText  = findViewById(R.id.newCocktail_edit_ingredient3);
        amount2EditText  = findViewById(R.id.newCocktail_edit_amount2);

        selectImageButton = findViewById(R.id.newCocktail_imagebutton_chooseImage);
        takeImageButton = findViewById(R.id.newCocktail_imagebutton_takeImage);
        finishButton = findViewById(R.id.newCocktail_button_finish);
        cropButton = findViewById(R.id.newCocktail_button_crop);
    }

    public static Uri getPhotoFile(Context context) {
        File file = new File(context.getExternalFilesDir("images/cocktails"), new Date().getTime() + "cocktail.jpeg");
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".my.package.name.provider", file);
    }

}
