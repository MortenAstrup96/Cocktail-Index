package dev.astrup.cocktailindex.Modules.Index;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dev.astrup.cocktailindex.Modules.Details.CocktailDetailsActivity;
import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;
import dev.astrup.cocktailindex.OnItemClickListener;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailController;
import dev.astrup.cocktailindex.Utility.SearchableFragment;


public class FavoriteFragment extends Fragment implements SearchableFragment {
    private OnItemClickListener listener;
    private OnItemClickListener checkboxClickListener;

    private CocktailController cocktailController = CocktailController.getInstance();
    private ArrayList<Cocktail> savedCocktailList;
    private FavouriteAdapter mAdapter;
    private Cocktail tempDeletion;

    /**
     * Creates the Fragment and sets up listeners
     *
     * @param inflater              Inflator to inflate the layout
     * @param container             The container in which this view will be inflated
     * @param savedInstanceState    Previous saved instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);
        savedCocktailList = new ArrayList<>();
        savedCocktailList.addAll(cocktailController.updateAndGetFavourites());

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, CocktailDetailsActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", cocktailController.getFavourites().get(position));
                ActivityCompat.startActivity(activity, intent, options);
            }
        };

        checkboxClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // updateList();
            }
        };

        // Gets the cocktailList
        setupRecyclerView(view);

        showIconIfEmpty(view);

        return view;
    }

    private void showIconIfEmpty(View view) {
        if(mAdapter.getItemCount() <= 0) {
            view.findViewById(R.id.favorite_emptylist_text).setVisibility(View.VISIBLE);
            view.findViewById(R.id.favorite_emptrylist_icon).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.favorite_emptylist_text).setVisibility(View.GONE);
            view.findViewById(R.id.favorite_emptrylist_icon).setVisibility(View.GONE);
        }
    }


    private void setupRecyclerView(View view) {
        // Recyclerview setup
        RecyclerView recyclerView = view.findViewById(R.id.Favorite_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FavouriteAdapter(savedCocktailList, listener, checkboxClickListener, getContext());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                // Checks if the up/down scroll speed is over/under 10 and hides/shows the FAB
                // A method from MainActivity will be called by type-casting getActivity.
                // TODO: Create a while loop and count the dx/dy so scrolling slowly will also remove FAB
                if(dy > 10) {
                    try {
                        ((MainActivity)getActivity()).setFabVisibility(false);
                    } catch (NullPointerException e) {
                        Log.d("FAB", "Error getting Main Activity when scrolling up/down " + e);
                    }
                }else if(dy < -10) {
                    try {
                        ((MainActivity)getActivity()).setFabVisibility(true);
                    } catch (NullPointerException e) {
                        Log.d("FAB", "Error getting Main Activity when scrolling up/down " + e);
                    }
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String name = item.getTitle().toString();
        int itemPosition = mAdapter.getPosition();
        final Cocktail cocktail = cocktailController.getFavourites().get(itemPosition);

        if(name.equals("Edit Cocktail")) {
            ((MainActivity)getActivity()).updateSpecificCocktailForResult(cocktail);

        } else if(name.equals("Delete Cocktail")) {
            tempDeletion = cocktailController.getFavourites().get(itemPosition);
            cocktailController.removeByID(tempDeletion.id, AppDatabase.getDatabase(getContext()));
            savedCocktailList.remove(itemPosition);
            mAdapter.notifyDataSetChanged();
            Snackbar.make(getView(), "Cocktail Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cocktailController.addCocktail(tempDeletion, AppDatabase.getDatabase(getContext()));
                            cocktailController.updateAndGetFavourites().add(tempDeletion);
                            savedCocktailList.add(tempDeletion);
                            java.util.Collections.sort(savedCocktailList);
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();

        }else if(name.equals("Share Recipe")) {
            ((MainActivity)getActivity()).shareRecipe(cocktail);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        showIconIfEmpty(getView());
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    /** ==== STANDARD FRAGMENT METHODS ==== */

    public void updateList() {
        if(savedCocktailList != null) {
            savedCocktailList.clear();
            savedCocktailList.addAll(cocktailController.getFavourites());
            mAdapter.notifyDataSetChanged();
        }

    }

    public void searchQuery(String s) {
        savedCocktailList.clear();
        for(Cocktail cocktail : cocktailController.getFavourites()) {
            boolean addCocktail = false;
            String name = cocktail.name.toLowerCase();

            for(Ingredient i : cocktail.ingredients) {
                if(i.getIngredient().toLowerCase().contains(s)) addCocktail = true;
            }
            if(name.contains(s)) addCocktail = true;
            if(addCocktail) savedCocktailList.add(cocktail);
        }

        mAdapter.notifyDataSetChanged();
    }
}
