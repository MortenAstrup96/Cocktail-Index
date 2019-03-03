package com.example.mortenastrup.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mortenastrup.cocktailindex.Objects.Cocktail;
import com.example.mortenastrup.cocktailindex.OnItemClickListener;
import com.example.mortenastrup.cocktailindex.R;

import java.util.List;
import java.util.Map;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder> {
    private Context mContext;



    // The list containing all the objects with the required information for at piece.
    private List<Cocktail> cocktailList;
    private Map<Integer, Bitmap> imageMap;

    private OnItemClickListener itemClickListener;


    // Initialises the string list
    public IndexAdapter(List<Cocktail> cocktailList, Map<Integer, Bitmap> thumbnailMap, OnItemClickListener itemClickListener) {
        this.cocktailList = cocktailList;
        this.imageMap = thumbnailMap;
        this.itemClickListener = itemClickListener;

    }

    @Override
    @NonNull
    public IndexAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_index, parent, false);

        return new MyViewHolder(itemView, itemClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {

            final Cocktail cocktail = cocktailList.get(position);
            String name = cocktail.name;
            String recipe = cocktail.recipe;

            // Get image from internal storage
            holder.name.setText(name);

        if(holder.imageView.getDrawable() == null) {
            Bitmap image = imageMap.get(cocktail.id);
            holder.imageView.setImageBitmap(image);
        }

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
        private ImageView imageView;


        public MyViewHolder(final View view, OnItemClickListener itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;


            name =  view.findViewById(R.id.index_section_header);
            imageView =  view.findViewById(R.id.index_section_image_cocktail);
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
