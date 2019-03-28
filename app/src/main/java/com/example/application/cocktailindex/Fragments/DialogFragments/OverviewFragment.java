package com.example.application.cocktailindex.Fragments.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Handlers.AddCocktailHandler;
import com.example.application.cocktailindex.R;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class OverviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;

    private Button finish;

    private AddCocktailHandler addCocktailHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
        addCocktailHandler = ((NewCocktailActivity)getActivity()).getFragmentHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.addcocktail_fragment_overview, container, false);
        finish = view.findViewById(R.id.addcocktail_fragment_overview_button_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCocktailHandler.onPressingOverviewButton(1); // Next
            }
        });

        return view;
    }

    private void blurBackground() {

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
        void onPressingOverviewButton(int button);
    }
}
