package com.example.mortenastrup.cocktailindex.RecyclerviewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mortenastrup.cocktailindex.OnItemClickListener;
import com.example.mortenastrup.cocktailindex.R;

import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {



    // The list containing all the objects with the required information for at piece.
    private List<String> stringList;

    private OnItemClickListener itemClickListener;


    // Initialises the string list
    public FavouriteAdapter(List<String> stringList, OnItemClickListener itemClickListener) {
        this.stringList = stringList;
        this.itemClickListener = itemClickListener;
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
        String stringName = stringList.get(position);
        // holder.name.setText(stringName);
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


        public MyViewHolder(final View view, OnItemClickListener itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
           // name =  view.findViewById(R.id.cocktail_name);

            view.setOnClickListener(this);

            /**
             * Switches checkbox from red to black in (checked/unchecked mode)
             */
            final CheckBox checkBox = view.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                    } else {
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
