package com.example.application.cocktailindex.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.cocktailindex.Activities.CocktailDetailsActivity;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.RecyclerviewAdapters.FavouriteAdapter;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.R;

import java.util.List;


public class FavoriteFragment extends Fragment {
    OnFragmentInteractionListener mListener;

    private OnItemClickListener listener;

    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private List<Cocktail> cocktailList;

    private FavouriteAdapter mAdapter;

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

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, CocktailDetailsActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", cocktailList.get(position)); // TODO: Switch to using ID instead later
                ActivityCompat.startActivity(activity, intent, options);
            }
        };

        try {
            cocktailList = ((MainActivity) getActivity()).getCocktailList();
        } catch (NullPointerException e) {
            Log.e("ApplicationError", "Nullpointer " + e);
        }
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
        mAdapter = new FavouriteAdapter(cocktailList, listener, getContext());
        recyclerView.setAdapter(mAdapter);
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
