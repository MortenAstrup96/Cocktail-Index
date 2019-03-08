package com.example.application.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.R;

import java.util.ArrayList;
import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    // The list containing all the objects with the required information for at piece.
    private List<Cocktail> cocktailList;
    private OnItemClickListener itemClickListener;
    private Context context;

    // Initialises the string list
    public FavouriteAdapter(List<Cocktail> cocktailList, OnItemClickListener itemClickListener, Context context) {
        this.cocktailList = cocktailList;


        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @Override
    @NonNull
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_favourites, parent, false);

        return new MyViewHolder(itemView, itemClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("PositionHolder", "Position: " + position);

        Cocktail cocktail = cocktailList.get(position);
        String name = cocktail.name;
        String ingredients = cocktail.ingredients;

        // Get image from internal storage
        holder.name.setText(name);
        holder.ingredients.setText(ingredients);



        Glide.with(context)
                .load(Uri.parse(cocktail.imagePath))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(cocktailList != null) {
            return cocktailList.size();
        }
        return 0;
    }

    /**
     * Inner class for the specific views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener itemClickListener;
        private TextView name;
        private TextView ingredients;
        private ImageView imageView;
        private CardView cardView;
        private CheckBox favourite;


        public MyViewHolder(final View view, final OnItemClickListener itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            name =  view.findViewById(R.id.favourites_section_header);
            ingredients =  view.findViewById(R.id.favourites_section_ingredients);
            imageView =  view.findViewById(R.id.favourites_section_image_cocktail);
            cardView = view.findViewById(R.id.favourites_section_cardview);


            view.setOnClickListener(this);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, getAdapterPosition()); // Might be wrong view
                }
            });

            /**
             * Switches checkbox from red to black in (checked/unchecked mode)
             */
            final CheckBox checkBox = view.findViewById(R.id.favourites_section_favourite);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Cocktail cocktail = cocktailList.get(getAdapterPosition());
                    cocktail.favourite = b;

                    if(b) {
                        cocktailList.add(cocktail);
                        cocktailList.add(cocktail);
                    } else {
                        cocktailList.remove(cocktail);
                        cocktailList.remove(cocktail);
                    }
                }
            });
        }

        /**
         * Method to be invoked when clicking on a certain element
         * @param view The specific element in the RecyclerView
         */
        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
