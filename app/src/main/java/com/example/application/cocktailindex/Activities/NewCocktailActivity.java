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
import android.view.MenuItem;

import com.example.application.cocktailindex.BuildConfig;
import com.example.application.cocktailindex.Fragments.DialogFragments.FillCocktailDetailsFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.FinaliseCocktailDetailsFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class NewCocktailActivity extends AppCompatActivity implements
        Serializable,
        FillCocktailDetailsFragment.OnFragmentInteractionListener,
        FinaliseCocktailDetailsFragment.OnFragmentInteractionListener {

    // On Activity result codes
    public static final int GET_FROM_GALLERY = 3;   // Getting images from gallery
    public static final int ACTION_IMAGE_CAPTURE = 4;   // Getting images from gallery

    private Uri selectedImage;

    // Different views in the activity

    private FillCocktailDetailsFragment fragmentName;
    private FinaliseCocktailDetailsFragment fragmentFinalise;


    private boolean pictureSelected = false;
    private Cocktail temporaryCocktail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

        fragmentName = new FillCocktailDetailsFragment();
        fragmentFinalise = new FinaliseCocktailDetailsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentName);
        fragmentTransaction.commit();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {

        }



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
            fragmentFinalise.setThumbnail(getSelectedImage());
            temporaryCocktail.imagePath = selectedImage.toString();
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



    public Uri getSelectedImage() {
        return selectedImage;
    }
    public static Uri getPhotoFile(Context context) {
        File file = new File(context.getExternalFilesDir("images/cocktails"), new Date().getTime() + "cocktail.jpeg");
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".my.package.name.provider", file);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();//finish your activity
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPressingNameButton(int button) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;


        switch (button) {
            case 1:
                // First fragment finished
                temporaryCocktail = fragmentName.getTemporaryCocktail();

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentFinalise);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case 2:
                finish();
                break;

        }
    }

    @Override
    public void onPressingImageButton(int button) {
        switch (button) {
            case 1:
                Intent intent = new Intent();
                intent.putExtra("cocktail", temporaryCocktail); // TODO: Might be causing problems due to serialization
                intent.putExtra("image", getSelectedImage().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case 2:
                activateCamera();
                break;
            case 3:
                activateGallery();
                break;
        }
    }
}
