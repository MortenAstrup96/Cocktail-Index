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
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Activities.MainActivity;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;

import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder> {


    // The list containing all the objects with the required information for at piece.
    private List<Cocktail> cocktailList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private Context context;

    private CheckBox checkBox;
    private boolean checkboxHack; // A hack to make sure the checkbox.setChecked does not trigger listener

    // Initialises the string list
    public IndexAdapter(List<Cocktail> cocktailList,  OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.cocktailList = cocktailList;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;

    }

    @Override
    @NonNull
    public IndexAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_index, parent, false);

        return new MyViewHolder(itemView, itemClickListener, itemLongClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Cocktail cocktail = cocktailList.get(position);
        String name = cocktail.name;
        String recipe = cocktail.recipe;
        boolean isChecked = cocktail.favourite;

        // Get image from internal storage
        holder.name.setText(name);

        Glide.with(context)
                .load(Uri.parse(cocktail.imagePath))
                .override(200, 200)
                .centerInside()
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);

        checkBox.setChecked(cocktail.favourite);
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
        private OnItemLongClickListener itemLongClickListener;
        private TextView name;
        private ImageView imageView;
        private CardView cardView;


        public MyViewHolder(final View view, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;

            this.setIsRecyclable(false); // TODO: VERY BAD PRACTICE -> Fix: https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022

            name =  view.findViewById(R.id.index_section_header);
            imageView =  view.findViewById(R.id.index_section_image_cocktail);
            cardView = view.findViewById(R.id.index_section_cardview);
            checkBox = view.findViewById(R.id.index_section_favourite);

            view.setOnClickListener(this);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, getAdapterPosition()); // Might be wrong view
                }
            });


            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemLongClickListener.onItemLongClick(view, getAdapterPosition()); // Might be wrong view
                    return false;
                }
            });


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(getAdapterPosition() >= 0 && !checkboxHack) {
                        Cocktail cocktail = cocktailList.get(getAdapterPosition());
                        cocktail.favourite = b;
                        ((MainActivity)context).toggleCocktailFavourite(cocktail.id, cocktail.favourite);
                    }
                }
            });

            view.setOnClickListener(this);

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
