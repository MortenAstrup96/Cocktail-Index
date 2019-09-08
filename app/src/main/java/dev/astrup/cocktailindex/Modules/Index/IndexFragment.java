package dev.astrup.cocktailindex.Modules.Index;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import dev.astrup.cocktailindex.Modules.Details.CocktailDetailsActivity;
import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;
import dev.astrup.cocktailindex.OnItemClickListener;
import dev.astrup.cocktailindex.OnItemLongClickListener;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailController;
import dev.astrup.cocktailindex.Utility.SearchableFragment;

import java.util.ArrayList;
import java.util.List;


public class IndexFragment extends Fragment implements SearchableFragment {


    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private CocktailController cocktailController = CocktailController.getInstance();
    private List<Cocktail> savedCocktailList;

    private IndexAdapter mAdapter;

    private AppDatabase db;

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
        View view = inflater.inflate(R.layout.fragment_index,container,false);


        savedCocktailList = new ArrayList<>();
        savedCocktailList.addAll(cocktailController.getIndexList());

        // Setup of database
        db = AppDatabase.getDatabase(view.getContext());


        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Scales up the new activity from the cardview clicked
                Intent intent = new Intent(getActivity(), CocktailDetailsActivity.class);
                intent.putExtra("cocktail", savedCocktailList.get(position)); // TODO: Switch to using ID instead later
                startActivity(intent);
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.Index_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IndexAdapter(savedCocktailList, listener, longClickListener, getContext());
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

        mAdapter.notifyDataSetChanged();

        showIconIfEmpty(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void showIconIfEmpty(View view) {
        if(mAdapter.getItemCount() <= 0) {
            view.findViewById(R.id.index_emptylist_text).setVisibility(View.VISIBLE);
            view.findViewById(R.id.index_emptrylist_icon).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.index_emptylist_text).setVisibility(View.GONE);
            view.findViewById(R.id.index_emptrylist_icon).setVisibility(View.GONE);
        }
    }

    /**
     * The method to get the position of tasks and the contextmenu is found on stackoverflow:
     *
     * https://stackoverflow.com/questions/26466877/how-to-create-context-menu-for-recyclerview
     *
     * @param item
     * @return
     */

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String name = item.getTitle().toString();
        int itemPosition = mAdapter.getPosition();
        Cocktail cocktail = cocktailController.getIndexList().get(itemPosition);

        if(name.equals("Edit Cocktail")) {
            ((MainActivity)getActivity()).updateSpecificCocktailForResult(cocktail);

        } else if(name.equals("Delete Cocktail")) {
            tempDeletion = cocktailController.getIndexList().get(itemPosition);
            cocktailController.removeCocktail(tempDeletion, db);
            savedCocktailList.remove(tempDeletion);
            mAdapter.notifyDataSetChanged();

            // Revert deletion
            Snackbar.make(getView(), "Cocktail Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cocktailController.addCocktail(tempDeletion, db);
                            savedCocktailList.add(tempDeletion);
                            mAdapter.notifyDataSetChanged();

                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();

        } else if(name.equals("Share Recipe")) {
            ((MainActivity)getActivity()).shareRecipe(cocktail);
        }
        return super.onContextItemSelected(item);
    }

    public void searchQuery(String s) {
        savedCocktailList.clear();
        for(Cocktail cocktail : cocktailController.getIndexList()) {
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

    /** ==== STANDARD FRAGMENT METHODS ==== */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        showIconIfEmpty(getView());
        super.onResume();
    }

    public void updateList() {
     savedCocktailList.clear();
     savedCocktailList.addAll(cocktailController.getIndexList());
     if(mAdapter != null) mAdapter.notifyDataSetChanged();
    }


}



