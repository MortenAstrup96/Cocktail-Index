package com.example.application.cocktailindex.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.application.cocktailindex.BuildConfig;
import com.example.application.cocktailindex.Fragments.DialogFragments.FillCocktailDetailsFragment;
import com.example.application.cocktailindex.R;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class NewCocktailActivity extends AppCompatActivity implements Serializable,
        FillCocktailDetailsFragment.OnFragmentInteractionListener {

    // On Activity result codes
    public static final int GET_FROM_GALLERY = 3;   // Getting images from gallery
    public static final int ACTION_IMAGE_CAPTURE = 4;   // Getting images from gallery

    private Uri selectedImage;

    // Different views in the activity

    private FillCocktailDetailsFragment fragmentName;


    private boolean pictureSelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

       initializeViews();
       // setOnClickListeners();

        fragmentName = new FillCocktailDetailsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentName);
        fragmentTransaction.commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Resultcode", resultCode + "");

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            this.selectedImage = selectedImage;



        } else if(requestCode==ACTION_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


        }

        if(resultCode == Activity.RESULT_OK) {
            pictureSelected = true;
        }
    }

    private void activateCamera() {
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


    private void activateGallery() {
        // Sets an intent to find and open the desired image
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_FROM_GALLERY); // Result is a Uri code for image
    }


    private void initializeViews() {
        //thumbnail = findViewById(R.id.newCocktail_imageView_thumbnail);
        /**
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

         Glide.with(this)
         .load(R.drawable.ic_nopicture)
         .override(400, 400)
         .centerInside()
         .apply(RequestOptions.circleCropTransform())
         .into(thumbnail);
         */
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }
    public static Uri getPhotoFile(Context context) {
        File file = new File(context.getExternalFilesDir("images/cocktails"), new Date().getTime() + "cocktail.jpeg");
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".my.package.name.provider", file);
    }


    @Override
    public void onPressingNameButton(int button) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (button) {
            case 1:
                activateCamera();
                break;
            case 2:
                activateGallery();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.commit();
                break;
        }
    }
}
