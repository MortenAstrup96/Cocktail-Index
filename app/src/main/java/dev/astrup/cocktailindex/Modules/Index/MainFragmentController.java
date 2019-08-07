package dev.astrup.cocktailindex.Modules.Index;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.FragmentController;

public class MainFragmentController implements FragmentController {
    private IndexFragment indexFragment;
    private FavoriteFragment favouriteFragment;
    private IdeaFragment ideaFragment;
    private Fragment currentFragment;

    private FragmentManager fragmentManager;

    public MainFragmentController(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        instantiateFragments();
        setCurrentFragment(indexFragment);
    }

    @Override
    public void instantiateFragments() {
        this.indexFragment = new IndexFragment();
        this.favouriteFragment = new FavoriteFragment();
        this.ideaFragment = new IdeaFragment();
    }

    @Override
    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void updateFragments() {
        if(favouriteFragment != null) favouriteFragment.updateList();
        if(indexFragment != null) indexFragment.updateList();
        if(ideaFragment != null) ideaFragment.updateList();
    }

    public void showIndexFragment() {
        setCurrentFragment(indexFragment);
    }
    public void showFavouriteFragment() {
        setCurrentFragment(favouriteFragment);
    }
    public void showIdeaFragment() {
        setCurrentFragment(ideaFragment);
    }
}
