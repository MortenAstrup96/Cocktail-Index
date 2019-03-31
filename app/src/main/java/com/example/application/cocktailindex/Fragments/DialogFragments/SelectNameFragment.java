package com.example.application.cocktailindex.Fragments.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.RecyclerviewAdapters.IngredientsAddAdapter;

import java.util.ArrayList;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class SelectNameFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;
    private ScrollView scrollView;

    private Button next;
    private Button buttonAddIngredient;

    private IngredientsAddAdapter mAdapter;
    private EditText editIngredients;
    private EditText editAmount;
    private Spinner measurementSpinner;
    private ArrayList<Ingredient> ingredients;
    private RecyclerView recyclerView;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_setname, container, false);
        next = view.findViewById(R.id.addcocktail_fragment_setname_button_next);
        measurementSpinner = view.findViewById(R.id.setName_measurement_picker);
        scrollView = view.findViewById(R.id.newCocktail_scroll);
        populateSpinner(view);
        setupRecyclerView(view);
        setupListeners();

        /**
        editIngredients.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Toast.makeText(getActivity(), "Top: " + editIngredients.getBottom(),
                            Toast.LENGTH_SHORT).show();
                    scrollView.smoothScrollTo(0, editIngredients.getBottom());
                }
            }
        });
*/

        return view;
    }

    private void setupRecyclerView(View view) {
        ingredients = new ArrayList<>();


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

        buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initial checks for completeness
                String ingredientString = editIngredients.getText().toString();
                String amountString = editAmount.getText().toString();
                String measurementString = measurementSpinner.getSelectedItem().toString();

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


                Ingredient ingredientToAdd = new Ingredient(ingredientString, amountString, measurementString);
                ingredients.add(ingredientToAdd);
                mAdapter.notifyDataSetChanged();

                editIngredients.setText("");
                editAmount.setText("");
                editIngredients.requestFocus();
            }
        });
    }

    private void populateSpinner(View view) {
        final String[] measurements = {
                "Oz.",
                "Tsp.",
                "Dash",
                "Teaspoon",
                "Cup"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, measurements);
        measurementSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingNameButton(3);
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
        void onPressingNameButton(int button);
    }
}
