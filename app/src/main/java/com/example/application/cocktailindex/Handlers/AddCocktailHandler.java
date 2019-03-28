package com.example.application.cocktailindex.Handlers;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Fragments.DialogFragments.OverviewFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.SelectCommentsFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.SelectImageFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.SelectIngredientsFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.SelectNameFragment;
import com.example.application.cocktailindex.Fragments.DialogFragments.SelectRecipeFragment;
import com.example.application.cocktailindex.R;

public class AddCocktailHandler implements
        SelectImageFragment.OnFragmentInteractionListener,
        SelectIngredientsFragment.OnFragmentInteractionListener,
        SelectRecipeFragment.OnFragmentInteractionListener,
        SelectCommentsFragment.OnFragmentInteractionListener,
        OverviewFragment.OnFragmentInteractionListener,
        SelectNameFragment.OnFragmentInteractionListener {

    private SelectNameFragment fragmentName;
    private SelectImageFragment fragmentImage;
    private SelectIngredientsFragment fragmentIngredients;
    private SelectRecipeFragment fragmentRecipe;
    private SelectCommentsFragment fragmentComments;
    private OverviewFragment fragmentOverview;

    private NewCocktailActivity cocktailActivity;  // Important reference to MainActivity

    private Fragment currentFragment;

    public AddCocktailHandler(NewCocktailActivity cocktailActivity) {
        this.cocktailActivity = cocktailActivity;
        fragmentName = new SelectNameFragment();
        fragmentImage = new SelectImageFragment();
        fragmentIngredients = new SelectIngredientsFragment();
        fragmentRecipe = new SelectRecipeFragment();
        fragmentComments = new SelectCommentsFragment();
        fragmentOverview = new OverviewFragment();
    }
    

    public void beginAddCocktail() {
        replaceNameFragment();

    }

    private void endAndSave() {
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(currentFragment);
        fragmentTransaction.commit();
    }


    /** Methods to replace the current fragment with the desired fragment */
    private void replaceNameFragment() {
        currentFragment = fragmentName;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainactivity_toplayer, currentFragment);
        fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void replaceImageFragment() {
        currentFragment = fragmentImage;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        performTransaction(ft);
    }

    private void replaceIngredientsFragment() {
        currentFragment = fragmentIngredients;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        performTransaction(ft);
    }

    private void replaceRecipeFragment() {
        currentFragment = fragmentRecipe;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        performTransaction(ft);
    }

    private void replaceCommentFragment() {
        currentFragment = fragmentComments;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        performTransaction(ft);
    }

    private void replaceOverviewFragment() {
        currentFragment = fragmentOverview;
        FragmentManager fragmentManager = cocktailActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        performTransaction(ft);
    }

    private void performTransaction(FragmentTransaction ft) {
        ft.replace(R.id.mainactivity_toplayer, currentFragment)
                .addToBackStack(null)
                .commit();
    }




    /** On Fragment Interaction Methods*/

    @Override
    public void onPressingNameButton(int button) {
        replaceImageFragment();
    }

    @Override
    public void onPressingImageButton(int button) {
        replaceIngredientsFragment();
    }

    @Override
    public void onPressingIngredientsButton(int button) {
        replaceRecipeFragment();
    }

    @Override
    public void onPressingRecipeButton(int button) {
        replaceCommentFragment();
    }

    @Override
    public void onPressingCommentsButton(int button) {
        replaceOverviewFragment();
    }

    @Override
    public void onPressingOverviewButton(int button) {
        endAndSave();
    }
}

/**

    @Override
    public void onPressingImageButton(int button) {

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
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentIngredients);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragmentImage);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onPressingIngredientsButton(int button) {
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
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentRecipe);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentRecipe);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onPressingCommentsButton(int button) {
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
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentOverview);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentOverview);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onPressingRecipeButton(int button) {
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
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentComments);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentComments);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onPressingOverviewButton(int button) {
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
                finish();
                break;
            case 4:

                break;
        }
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
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentImage);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_new_cocktail, fragmentImage);
                fragmentTransaction.commit();
                break;
        }
    } */