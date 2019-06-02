package com.example.application.cocktailindex.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.application.cocktailindex.Activities.CocktailDetailsActivity;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IndexAdapter;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
        savedCocktailList.addAll(cocktailSingleton.getIndexList());

        // Setup of database
        db = AppDatabase.getDatabase(view.getContext());


        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                java.util.Collections.sort(cocktailSingleton.getIndexList());

                // Scales up the new activity from the cardview clicked
                Intent intent = new Intent(getActivity(), CocktailDetailsActivity.class);
                intent.putExtra("cocktail", cocktailSingleton.getIndexList().get(position)); // TODO: Switch to using ID instead later
                startActivity(intent);
            }
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.Index_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IndexAdapter(cocktailSingleton.getIndexList(), listener, longClickListener, getContext());
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

        java.util.Collections.sort(cocktailSingleton.getIndexList());
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
        Cocktail cocktail = cocktailSingleton.getIndexList().get(itemPosition);

        if(name.equals("Edit Cocktail")) {
            ((MainActivity)getActivity()).updateSpecificCocktailForResult(cocktail);

        } else if(name.equals("Delete Cocktail")) {
            tempDeletion = cocktailSingleton.getIndexList().get(itemPosition);
            cocktailSingleton.getIndexList().remove(itemPosition);
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
                            cocktailSingleton.getIndexList().add(tempDeletion);
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
        cocktailSingleton.getIndexList().clear();
        cocktailSingleton.getIndexList().addAll(savedCocktailList);
        List<Cocktail> toRemove = new ArrayList<>();

        for(Cocktail cocktail : cocktailSingleton.getIndexList()) {
            String name = cocktail.name.toLowerCase();
           // String ingredients = cocktail.ingredients.toLowerCase();

            if( !name.contains(s) /*&&
                    !ingredients.contains(s)*/) {
                toRemove.add(cocktail);
            }
        }

        cocktailSingleton.getIndexList().removeAll(toRemove);
        mAdapter.notifyDataSetChanged();
        return cocktailSingleton.getIndexList();
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



