package dev.astrup.cocktailindex.Utility;

import androidx.fragment.app.Fragment;

public interface FragmentController {
    void instantiateFragments();

    void setCurrentFragment(Fragment fragment);

    Fragment getCurrentFragment();

    void updateFragments();


}
