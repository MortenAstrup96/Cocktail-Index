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
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.RecyclerviewAdapters.FavouriteAdapter;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.R;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    OnFragmentInteractionListener mListener;

    private OnItemClickListener listener;

    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private List<Cocktail> cocktailList;
    private List<Cocktail> favouriteList;

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
        favouriteList = new ArrayList<>();

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, CocktailDetailsActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", favouriteList.get(position)); // TODO: Switch to using ID instead later
                ActivityCompat.startActivity(activity, intent, options);
            }
        };

        try {
            cocktailList = ((MainActivity) getActivity()).getCocktailList();
        } catch (NullPointerException e) {
            Log.e("ApplicationError", "Nullpointer " + e);
        }

        for(Cocktail cocktail : cocktailList) {
            if(cocktail.favourite) {
                favouriteList.add(cocktail);
            }
        }
        java.util.Collections.sort(favouriteList);

        // Gets the cocktailList
        setupRecyclerView(view);

        return view;
    }


    private void setupRecyclerView(View view) {
        // Recyclerview setup
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.Favorite_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FavouriteAdapter(favouriteList, listener, getContext());
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

        if(name.equals("Edit Cocktail")) {

        } else if(name.equals("Delete Cocktail")) {
            tempDeletion = cocktailList.get(itemPosition);
            cocktailList.remove(itemPosition);
            mAdapter.notifyDataSetChanged();
            Snackbar.make(getView(), "Cocktail Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cocktailList.add(tempDeletion);
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
    }

    /** ==== STANDARD FRAGMENT METHODS ==== */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void updateList() {
        if(mAdapter != null) mAdapter.notifyDataSetChanged();

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
        void favoriteFragmentInteractionListener(String task);
    }
}
