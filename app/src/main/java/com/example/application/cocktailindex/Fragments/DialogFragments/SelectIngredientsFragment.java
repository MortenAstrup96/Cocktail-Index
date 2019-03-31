package com.example.application.cocktailindex.Fragments.DialogFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IndexAdapter;
import com.example.application.cocktailindex.RecyclerviewAdapters.IngredientsAddAdapter;

import java.util.ArrayList;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class SelectIngredientsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;

    private RecyclerView recyclerView;
    private IngredientsAddAdapter mAdapter;
    private ArrayList<Ingredient> ingredients;

    private Button buttonAddIngredient;
    private EditText editIngredients;
    private EditText editAmount;

    private Button buttonNext;
    private Button buttonSkip;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_setingredients, container, false);


        recyclerView = view.findViewById(R.id.addcocktail_fragment_setingredients_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IngredientsAddAdapter(ingredients, getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        buttonAddIngredient = view.findViewById(R.id.addcocktail_fragment_setingredients_button_add);
        editIngredients = view.findViewById(R.id.addcocktail_fragment_setingredients_edit_ingredient);
        editAmount = view.findViewById(R.id.addcocktail_fragment_setingredients_edit_amount);
        buttonNext = view.findViewById(R.id.addcocktail_fragment_setingredients_button_next);
        buttonSkip = view.findViewById(R.id.addcocktail_fragment_setingredients_button_skip);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingIngredientsButton(3);
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingIngredientsButton(4);
            }
        });
        buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initial checks for completeness
                String ingredientString = editIngredients.getText().toString();
                String amountString = editAmount.getText().toString();

                if(ingredientString.isEmpty() || amountString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an ingredient & amount",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if(ingredients.size() >= 8) {
                    Toast.makeText(getActivity(), "Ingredient limit reached",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                mAdapter.notifyDataSetChanged();

                editIngredients.setText("");
                editAmount.setText("");
                editIngredients.requestFocus();
            }
        });


        return view;
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
        void onPressingIngredientsButton(int button);
    }
}
