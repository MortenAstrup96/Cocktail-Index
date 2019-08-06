package dev.astrup.cocktailindex.Modules.AppWalkthrough;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import dev.astrup.cocktailindex.R;

public class WalkthroughActivity extends AppIntro  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request permission to read storage before loading images
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome to Cocktail Index");
        sliderPage.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
        sliderPage.setImageDrawable(R.drawable.ic_logo_nobackground);
        sliderPage.setDescription("Cocktail Index helps you organize your different cocktail recipes. Mark your favourite recipes and sketch down your different ideas with the idea tab.");
        addSlide(AppIntroFragment.newInstance(sliderPage));


        WalkthroughPreferencesFragment fragment = new WalkthroughPreferencesFragment();
        addSlide(fragment);
        skipButtonEnabled = false;
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment.isPredefinedList()) {
                    new PopulateDatabase().populateDatabase(getApplicationContext(), fragment.isMetricChecked());
                }
                finish();
            }
        });
    }
}
