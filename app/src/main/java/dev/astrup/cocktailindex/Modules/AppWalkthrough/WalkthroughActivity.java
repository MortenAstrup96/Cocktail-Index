package dev.astrup.cocktailindex.Modules.AppWalkthrough;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import dev.astrup.cocktailindex.R;

public class WalkthroughActivity extends AppIntro  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome to Cocktail Index");
        sliderPage.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
        sliderPage.setImageDrawable(R.drawable.ic_logo_nobackground);
        sliderPage.setDescription("Cocktail Index helps you organize your different cocktail recipes. Mark your favourite recipes and sketch down your different ideas with the idea tab.");
        addSlide(AppIntroFragment.newInstance(sliderPage));

        addSlide(new WalkthroughPreferencesFragment());
    }
}
