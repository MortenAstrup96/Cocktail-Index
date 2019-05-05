package com.example.application.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        String name = ingredient.getIngredient();
        String amount = ingredient.getAmount();
        String measurement = ingredient.getMeasurement();

        if(measurement.equals("Piece")) measurement = "";   // Piece means no text
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
            name = view.findViewById(R.id.recycler_view_addingredient_show_ingredient);
            amount = view.findViewById(R.id.recycler_view_addingredient_show_amount);



        }
    }
}
