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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.cocktailindex.Activities.CocktailDetailsActivity;
import com.example.application.cocktailindex.Activities.IdeaActivity;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IdeaAdapter;
import com.example.application.cocktailindex.RecyclerviewAdapters.IndexAdapter;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.util.ArrayList;
import java.util.List;


public class IdeaFragment extends Fragment {
    OnFragmentInteractionListener mListener;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    private IdeaAdapter mAdapter;
    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private CocktailSingleton cocktailSingleton = CocktailSingleton.getInstance();
    private List<Cocktail> ideaList = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.fragment_idea,container,false);

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                java.util.Collections.sort(cocktailSingleton.getCocktailList());

                // Scales up the new activity from the cardview clicked
                Activity activity = getActivity();
                Intent intent = new Intent(activity, IdeaActivity.class);
                Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                intent.putExtra("cocktail", cocktailSingleton.getIdeas().get(position)); // TODO: Switch to using ID instead later
                ActivityCompat.startActivity(getActivity(), intent, options);
            }
        };


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.idea_fragment_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IdeaAdapter(cocktailSingleton.getIdeas(), listener, longClickListener, getContext());
        recyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return view;
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
        void ideaFragmentInteractionListener(String task);
    }
}
