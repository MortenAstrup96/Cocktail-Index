package com.example.application.cocktailindex.Fragments.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.R;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class SelectRecipeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;

    private Button skip;
    private Button next;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_setrecipe, container, false);
        skip = view.findViewById(R.id.addcocktail_fragment_setRecipe_button_skip);
        next = view.findViewById(R.id.addcocktail_fragment_setRecipe_button_next);
        setupListeners();

        return view;
    }

    private void setupListeners() {
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingRecipeButton(4);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingRecipeButton(3);
            }
        });
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPressingRecipeButton(int button);
    }
}
