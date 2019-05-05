package com.example.application.cocktailindex.Fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.application.cocktailindex.Activities.CocktailDetailsActivity;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IndexAdapter;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.application.cocktailindex.Activities.CocktailDetailsActivity.UPDATE_COCKTAIL_RECIPE;


public class IndexFragment extends Fragment {
    OnFragmentInteractionListener mListener;


    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private CocktailSingleton cocktailSingleton = CocktailSingleton.getInstance();
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
        savedCocktailList.addAll(cocktailSingleton.getCocktailList());

        // Setup of database
        db = AppDatabase.getDatabase(view.getContext());


        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                java.util.Collections.sort(cocktailSingleton.getCocktailList());

                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, CocktailDetailsActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", cocktailSingleton.getCocktailList().get(position)); // TODO: Switch to using ID instead later
                ActivityCompat.startActivity(activity, intent, options);
            }
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.Index_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IndexAdapter(cocktailSingleton.getCocktailList(), listener, longClickListener, getContext());
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

        java.util.Collections.sort(cocktailSingleton.getCocktailList());
        mAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return view;
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
        Cocktail cocktail = cocktailSingleton.getCocktailList().get(itemPosition);

        if(name.equals("Edit Cocktail")) {
            ((MainActivity)getActivity()).updateSpecificCocktail(cocktail);

        } else if(name.equals("Delete Cocktail")) {
            tempDeletion = cocktailSingleton.getCocktailList().get(itemPosition);
            cocktailSingleton.getCocktailList().remove(itemPosition);
            savedCocktailList.remove(itemPosition);
            mAdapter.notifyDataSetChanged();

            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.ingredientDBDao().delete(tempDeletion.ingredients);
                    db.cocktailDBDao().delete(tempDeletion);
                }
            });


            // Revert deletion
            Snackbar.make(getView(), "Cocktail Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cocktailSingleton.getCocktailList().add(tempDeletion);
                            savedCocktailList.add(tempDeletion);
                            mAdapter.notifyDataSetChanged();

                            Executor myExecutor = Executors.newSingleThreadExecutor();
                            myExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    db.cocktailDBDao().insertOne(tempDeletion);
                                }
                            });
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();

        }
        return super.onContextItemSelected(item);
    }

    public List<Cocktail> searchQuery(String s) {
        cocktailSingleton.getCocktailList().clear();
        cocktailSingleton.getCocktailList().addAll(savedCocktailList);
        List<Cocktail> toRemove = new ArrayList<>();

        for(Cocktail cocktail : cocktailSingleton.getCocktailList()) {
            String name = cocktail.name.toLowerCase();
           // String ingredients = cocktail.ingredients.toLowerCase();

            if( !name.contains(s) /*&&
                    !ingredients.contains(s)*/) {
                toRemove.add(cocktail);
            }
        }

        cocktailSingleton.getCocktailList().removeAll(toRemove);
        mAdapter.notifyDataSetChanged();
        return cocktailSingleton.getCocktailList();
    }

    /** ==== STANDARD FRAGMENT METHODS ==== */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void updateList() {
        if(mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void indexFragmentInteractionListener(String task);
    }


}



