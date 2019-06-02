package com.example.application.cocktailindex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.application.cocktailindex.Fragments.CreateCocktailFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

import java.io.Serializable;

public class NewCocktailActivity extends AppCompatActivity implements
        Serializable,
        CreateCocktailFragment.OnFragmentInteractionListener{

    // On Activity result codes
    public static final int GET_IMAGE = 3;

    private Uri outputFileUri;
    private Uri selectedImage;
    private CreateCocktailFragment fragmentName;

    private Cocktail temporaryCocktail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

        setupFragments();
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();

        if(data != null) temporaryCocktail = (Cocktail) data.getSerializable("cocktail");
        if(temporaryCocktail == null) temporaryCocktail = new Cocktail(null, null, null, null, false, false);
    }

    private void setupFragments() {
        fragmentName = new CreateCocktailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentName);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_IMAGE) {
                selectedImage = data.getData();
                if(selectedImage != null) fragmentName.addImageToList(selectedImage);
            }
        }

    }

    private void openImageIntent() {
        selectedImage = null;

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(galleryIntent, GET_IMAGE);
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
        switch (button) {
            case 1:
                // First fragment finished
                temporaryCocktail = fragmentName.getTemporaryCocktail();
                Intent intent = new Intent();
                intent.putExtra("cocktail", temporaryCocktail);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case 2:
                finish();
                break;
            case 3:
                openImageIntent();
                break;
        }
    }

    public CreateCocktailFragment getFragmentName() {
        return fragmentName;
    }



    public Cocktail getEditableCocktail() {
        return temporaryCocktail;
    }
}

/**
    private void activateCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        selectedImage = getPhotoFile(getApplicationContext());
        camera.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        camera.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        camera.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);


    }
*/