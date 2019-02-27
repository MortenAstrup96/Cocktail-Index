package com.example.mortenastrup.cocktailindex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.MyViewHolder> {



    // The list containing all the objects with the required information for at piece.
    private List<String> stringList;

    private OnItemClickListener itemClickListener;


    // Initialises the string list
    public CocktailAdapter(List<String> stringList, OnItemClickListener itemClickListener) {
        this.stringList = stringList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NonNull
    public CocktailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_list, parent, false);

        return new MyViewHolder(itemView, itemClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String stringName = stringList.get(position);
        holder.name.setText(stringName);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    /**
     * Inner class for the specific views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener itemClickListener;
        private TextView name;


        public MyViewHolder(View view, OnItemClickListener itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            name =  view.findViewById(R.id.books_name);

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
