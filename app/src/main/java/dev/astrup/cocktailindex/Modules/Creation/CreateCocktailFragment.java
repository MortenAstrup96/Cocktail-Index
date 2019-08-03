package dev.astrup.cocktailindex.Modules.Creation;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dev.astrup.cocktailindex.Modules.Index.MainActivity;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailSingleton;
import dev.astrup.cocktailindex.Utility.GdprHelper;
import dev.astrup.cocktailindex.Utility.Measurements;

import java.util.ArrayList;
import java.util.List;


public class CreateCocktailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;
    private ScrollView scrollView;

    private Button next;
    private Button cancel;
    private Button buttonAddIngredient;

    private IngredientsAddAdapter mAdapter;
    private ImageAddAdapter imageAddAdapter;
    private AutoCompleteTextView editIngredients;
    private EditText editAmount;
    private EditText editName;
    private EditText editRecipe;
    private EditText editComments;
    private Button addImageButton;
    private boolean idea;
    private boolean favourite;

    private RadioGroup radioGroup;
    private RadioButton normalRadio;
    private RadioButton ideaRadio;
    private RadioButton favouriteRadio;

    private Cocktail temporaryCocktail;



    private ArrayList<Ingredient> ingredients;
    private ArrayList<Uri> imagePaths;
    private RecyclerView recyclerView;
    private RecyclerView imageRecyclerView;
    private List<String> array = new ArrayList<>();

    private Spinner measureTypeSpinner;

    // Container References for smoothscroll
    private ConstraintLayout constraintIngredients;
    private ConstraintLayout imageLayout;

    // Admob
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_details, container, false);

        serveAd(view);

        temporaryCocktail = ((NewCocktailActivity)getActivity()).getEditableCocktail();

        setupViews(view);
        setupRecyclerView(view);
        setupListeners();

        if(temporaryCocktail != null) {
            fillEditableText();
            if(temporaryCocktail.name == null) getActivity().setTitle("New Cocktail");
            else getActivity().setTitle("Edit Cocktail");
        }



        return view;
    }

    private void serveAd(View view) {
        GdprHelper gdprHelper = new GdprHelper(getContext());
        mAdView = view.findViewById(R.id.admob_addcocktailbanner);
        AdRequest adRequest;
        if(!gdprHelper.isConsent()){
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        }
        else{
            adRequest = new AdRequest.Builder().build();
        }
        mAdView.loadAd(adRequest);
    }

    private void fillEditableText() {
        editName.setText(temporaryCocktail.name);
        editRecipe.setText(temporaryCocktail.recipe);
        editComments.setText(temporaryCocktail.comments);

        // Add ingredients to list
        for(Ingredient ingredient : temporaryCocktail.ingredients) {
            ingredients.add(ingredient);
        }
        mAdapter.notifyDataSetChanged();
    }


    private void setupRecyclerView(View view) {
        ingredients = new ArrayList<>();

        recyclerView = view.findViewById(R.id.addcocktail_fragment_setingredients_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IngredientsAddAdapter(ingredients, getContext());
        recyclerView.setAdapter(mAdapter);

        /** Spinner setup here */
        measureTypeSpinner = view.findViewById(R.id.addCocktail_spinner);

        array.addAll(getMeasurements(false));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        measureTypeSpinner.setAdapter(adapter);

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Double amount = null;
                try {
                    amount = Double.valueOf(charSequence.toString());
                } catch (NumberFormatException e) {

                }

                if(amount != null) {
                    if(amount == 0 || amount > 1) {
                        getArray().clear();
                        getArray().addAll(getMeasurements(true));
                        adapter.notifyDataSetChanged();
                    } else {
                        getArray().clear();
                        getArray().addAll(getMeasurements(false));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    getArray().clear();
                    getArray().addAll(getMeasurements(false));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // Image Adapter
        imagePaths = new ArrayList<>();
        if(temporaryCocktail.imagePath != null && !temporaryCocktail.imagePath.isEmpty() && !temporaryCocktail.imagePath.get(0).isEmpty()) {
            for(String s : temporaryCocktail.imagePath) {
                imagePaths.add(Uri.parse(s));
            }
        }



        imageRecyclerView = view.findViewById(R.id.addCocktail_recyclerview_images);
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(imageLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageAddAdapter= new ImageAddAdapter(imagePaths, getContext());
        imageRecyclerView.setAdapter(imageAddAdapter);

    }

    private List<String> getMeasurements(boolean plural) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean metric = prefs.getBoolean("metric", false);
        Measurements measurements = new Measurements(metric);
        return measurements.getMeasurements(plural);
    }

    private List<String> getArray() {
        return array;
    }

    private void setupListeners() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPressingNameButton(2);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please specify a name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(ingredients.isEmpty()) {
                    Toast.makeText(getActivity(), "Add at least one ingredient",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(temporaryCocktail != null) {
                    updateExistingCocktail();
                } else {
                    createNewCocktailFromInformation();
                }

                mListener.onPressingNameButton(1);

            }
        });


        // Autocomplete
        String[] ingredientsArray = CocktailSingleton.getInstance().getIngredientsArrayWithAmount();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, ingredientsArray);

        editIngredients.setThreshold(1);//will start working from first character
        editIngredients.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        editIngredients.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    scrollView.smoothScrollTo(0, constraintIngredients.getTop());
                }

            }
        });

        buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initial checks for completeness
                String ingredientString = editIngredients.getText().toString();
                String amountString = editAmount.getText().toString();
                String quantityString = measureTypeSpinner.getSelectedItem().toString();


                if(ingredientString.isEmpty() || amountString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an ingredient & amount",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(ingredients.size() >= 20) {
                    Toast.makeText(getActivity(), "Ingredient limit reached",
                            Toast.LENGTH_SHORT).show();
                    return;
                }




                Ingredient ingredientToAdd = new Ingredient(ingredientString, amountString, quantityString);
                ingredients.add(ingredientToAdd);
                mAdapter.notifyDataSetChanged();

                editIngredients.setText("");
                editAmount.setText("");
                editIngredients.requestFocus();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingNameButton(3);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == ideaRadio.getId()) {
                    imageLayout.setVisibility(View.GONE);
                    imagePaths = null;
                } else {
                    imageLayout.setVisibility(View.VISIBLE);
                }
            }
        });





    }

    private void updateExistingCocktail() {
        favourite = false;
        idea = false;
        int id = radioGroup.getCheckedRadioButtonId();
        if(id == favouriteRadio.getId()) {
            favourite = true;
        }
        if(id == ideaRadio.getId()) {
            idea = true;
        }

        temporaryCocktail.name = editName.getText().toString();
        temporaryCocktail.recipe = editRecipe.getText().toString();
        temporaryCocktail.comments = editComments.getText().toString();
        temporaryCocktail.favourite = favourite;
        temporaryCocktail.idea = idea;

        temporaryCocktail.setIngredients(ingredients);
        for(Ingredient i : ingredients) {
            i.setCocktailIdFk(temporaryCocktail.id);
        }

        temporaryCocktail.imagePath = new ArrayList<>();
        if(imagePaths != null) {
            for(Uri uri : imagePaths) {
                temporaryCocktail.imagePath.add(uri.toString());
            }
        }




        // Sets the unique number of the ingredient for future order
        for(int i = 0 ; i < temporaryCocktail.ingredients.size(); i++) {
            temporaryCocktail.ingredients.get(i).setNumber(i);
        }
    }

    private void createNewCocktailFromInformation() {
        favourite = false;
        idea = false;
        int id = radioGroup.getCheckedRadioButtonId();
        if(id == favouriteRadio.getId()) {
            favourite = true;
        }
        if(id == ideaRadio.getId()) {
            idea = true;
        }

        temporaryCocktail = new Cocktail(
                editName.getText().toString(),
                editRecipe.getText().toString(),
                editComments.getText().toString(),
                new ArrayList<String>(),
                favourite,
                idea
        );

        temporaryCocktail.setIngredients(ingredients);
        for(Ingredient i : ingredients) {
            i.setCocktailIdFk(temporaryCocktail.id);
        }

        for(Uri uri : imagePaths) {
            temporaryCocktail.imagePath.add(uri.toString());
        }

        // Sets the unique number of the ingredient for future order
        for(int i = 0 ; i < temporaryCocktail.ingredients.size(); i++) {
            temporaryCocktail.ingredients.get(i).setNumber(i);
        }
    }

    public Cocktail getTemporaryCocktail() {
        return temporaryCocktail;
    }

    private void setupViews(View view) {
        imageLayout = view.findViewById(R.id.ChooseImage_container);

        next = view.findViewById(R.id.addCocktail_button_finish);
        cancel = view.findViewById(R.id.addCocktail_button_cancel);
        scrollView = view.findViewById(R.id.newCocktail_scroll);
        addImageButton = view.findViewById(R.id.addCocktail_button_addimage);

        // Edittexts
        editIngredients = view.findViewById(R.id.addCocktail_edittext_ingredient);
        editAmount = view.findViewById(R.id.addCocktail_edittext_quantity);
        editName = view.findViewById(R.id.addCocktail_edittext_name);
        editRecipe = view.findViewById(R.id.addCocktail_edittext_recipe);
        editComments = view.findViewById(R.id.addCocktail_edittext_comments);

        radioGroup = view.findViewById(R.id.radioGroup3);
        normalRadio = view.findViewById(R.id.radioButton);
        favouriteRadio = view.findViewById(R.id.radioButton2);
        ideaRadio = view.findViewById(R.id.radioButton3);


        // Constraint Layouts
        constraintIngredients = view.findViewById(R.id.ChooseIngredients_container);

        // Buttons
        buttonAddIngredient = view.findViewById(R.id.addCocktail_addIngredient_button);

        idea = false;
        favourite = false;
    }

    public void addImageToList(Uri image) {
        imagePaths.add(image);
        imageAddAdapter.notifyDataSetChanged();
    }

    public void removeImageFromList(int position) {
        imagePaths.remove(position);
        imageAddAdapter.notifyDataSetChanged();
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

    // 1 = next, 2 = cancel
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPressingNameButton(int button);
    }
}
