package dev.astrup.cocktailindex.Modules.Various;


import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.GdprHelper;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        Preference button = findPreference(getString(R.string.resetPrivacy));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), "Privacy Settings Reset", Toast.LENGTH_LONG).show();
                GdprHelper gdprHelper = new GdprHelper(getContext());
                gdprHelper.resetConsent();

                gdprHelper.initialise();
                return true;
            }
        });
    }


}
