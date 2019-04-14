package com.example.application.cocktailindex.Fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
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
import android.widget.AdapterView;

import com.example.application.cocktailindex.Activities.CocktailDetailsActivity;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IndexAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class IndexFragment extends Fragment {
    OnFragmentInteractionListener mListener;

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private List<Cocktail> cocktailList;
    private List<Cocktail> savedCocktailList;

    private IndexAdapter mAdapter;

    private AppDatabase db;


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

        cocktailList = ((MainActivity) getActivity()).getCocktailList();
        savedCocktailList = new ArrayList<>();
        savedCocktailList.addAll(cocktailList);

        // Setup of database
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").build();

        longClickListener = new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final Cocktail removingCocktail = savedCocktailList.get(position);

                cocktailList.remove(position);
                savedCocktailList.remove(position);
                mAdapter.notifyDataSetChanged();

                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.cocktailDBDao().delete(removingCocktail);
                    }
                });
            }
        };

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                java.util.Collections.sort(cocktailList);

                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, CocktailDetailsActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", cocktailList.get(position)); // TODO: Switch to using ID instead later
                ActivityCompat.startActivity(activity, intent, options);
            }
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.Index_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IndexAdapter(cocktailList, listener, longClickListener, getContext());
        recyclerView.setAdapter(mAdapter);

        java.util.Collections.sort(cocktailList);
        mAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return view;
    }


    public List<Cocktail> searchQuery(String s) {
        cocktailList.clear();
        cocktailList.addAll(savedCocktailList);
        List<Cocktail> toRemove = new ArrayList<>();

        for(Cocktail cocktail : cocktailList) {
            String name = cocktail.name.toLowerCase();
            String ingredients = cocktail.ingredients.toLowerCase();

            if( !name.contains(s) &&
                    !ingredients.contains(s)) {
                toRemove.add(cocktail);
            }
        }

        cocktailList.removeAll(toRemove);
        mAdapter.notifyDataSetChanged();
        return cocktailList;
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



