package dev.astrup.cocktailindex.Fragments;


import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import dev.astrup.cocktailindex.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }


}
