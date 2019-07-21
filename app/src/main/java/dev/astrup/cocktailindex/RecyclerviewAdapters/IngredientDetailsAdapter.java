package dev.astrup.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.astrup.cocktailindex.Objects.Ingredient;
import dev.astrup.cocktailindex.R;

import java.util.ArrayList;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class IngredientDetailsAdapter extends RecyclerView.Adapter<IngredientDetailsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;


    // Initialises the string list
    public IngredientDetailsAdapter(ArrayList<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    @NonNull
    public IngredientDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_showingredientdetails, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        String name = ingredient.getIngredient();
        String amount = ingredient.getAmount();
        String measurement = ingredient.getMeasurement();


        holder.name.setText(name);
        holder.amount.setText(amount + " " + measurement + " ");
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    /**
     * Inner class for the specific views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView amount;

        public MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.recyclerview_showingredientdetails_ingredient);
            amount = view.findViewById(R.id.recyclerview_showingredientdetails_amount);


        }
    }
}
