package com.example.application.cocktailindex.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
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
import android.widget.ImageView;

import com.example.application.cocktailindex.BuildConfig;
import com.example.application.cocktailindex.Fragments.DialogFragments.FillCocktailDetailsFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class NewCocktailActivity extends AppCompatActivity implements
        Serializable,
        FillCocktailDetailsFragment.OnFragmentInteractionListener{

    // On Activity result codes
    public static final int GET_FROM_GALLERY = 3;   // Getting images from gallery
    public static final int ACTION_IMAGE_CAPTURE = 4;   // Getting images from gallery

    private Uri selectedImage;
    private Uri outputFileUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cocktails/");

    // Different views in the activity

    private ImageView testImage;


    private FillCocktailDetailsFragment fragmentName;


    private boolean pictureSelected = false;
    private Cocktail temporaryCocktail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cocktail);

        fragmentName = new FillCocktailDetailsFragment();
        testImage = findViewById(R.id.recycler_view_add_image_imageview);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentName);
        fragmentTransaction.commit();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {

        }
        Bundle data = getIntent().getExtras();

        try {
            temporaryCocktail = (Cocktail) data.getSerializable("cocktail");
        } catch (NullPointerException e) {

        }

        if(temporaryCocktail == null) {
            temporaryCocktail = new Cocktail(null, null, null, null, false, false);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Resultcode", resultCode + "");

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            this.selectedImage = data.getData();
            pictureSelected = true;
            fragmentName.addImageToList(selectedImage);
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
        /**
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        */

        //startActivityForResult(intent, GET_FROM_GALLERY); // Result is a Uri code for image

        openImageIntent();
    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "Cocktail_#" + temporaryCocktail.id;
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, GET_FROM_GALLERY);
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
                activateGallery();
                break;
        }
    }

    public FillCocktailDetailsFragment getFragmentName() {
        return fragmentName;
    }



    public Cocktail getEditableCocktail() {
        return temporaryCocktail;
    }
}
