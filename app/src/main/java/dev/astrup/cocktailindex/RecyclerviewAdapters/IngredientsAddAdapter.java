package dev.astrup.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
public class IngredientsAddAdapter extends RecyclerView.Adapter<IngredientsAddAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;



    // Initialises the string list
    public IngredientsAddAdapter(ArrayList<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    @NonNull
    public IngredientsAddAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_addingredients_show, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Ingredient ingredient = ingredients.get(position);
        String name = ingredient.getIngredient();
        String amount = ingredient.getAmount();
        String measurement = ingredient.getMeasurement();

        if(measurement.equals("Piece")) measurement = "";   // Piece means no text
        holder.name.setText(name);
        holder.amount.setText(amount + " " + measurement + " ");

        holder.deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.remove(position);
                notifyDataSetChanged();
            }
        });


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
        private ImageButton deleteIngredientButton;

        public MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.recycler_view_addingredient_show_ingredient);
            amount = view.findViewById(R.id.recycler_view_addingredient_show_amount);
            deleteIngredientButton = view.findViewById(R.id.recyclerview_addingredients_delete);





        }
    }
}
