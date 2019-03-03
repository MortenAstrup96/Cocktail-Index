package com.example.mortenastrup.cocktailindex.RecyclerviewAdapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mortenastrup.cocktailindex.ImageLoader;
import com.example.mortenastrup.cocktailindex.Objects.Cocktail;
import com.example.mortenastrup.cocktailindex.OnItemClickListener;
import com.example.mortenastrup.cocktailindex.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


    // Initialises the string list
    public FavouriteAdapter(List<Cocktail> cocktailList, OnItemClickListener itemClickListener) {
        this.cocktailList = cocktailList;
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
        Cocktail cocktail = cocktailList.get(position);

        String name = cocktail.name;
        String recipe = cocktail.recipe;

        // Get image from internal storage
        holder.name.setText(name);
        holder.description.setText(recipe);

        String[] array = new String[2];
        array[0] = cocktail.imagePath;
        array[1] = ""+cocktail.id;
        if(holder.imageView != null) {
            new ImageLoader(holder.imageView).execute(array);
        }
    }

    /**
     * Takes the path and unique ID from an image saved in the database and retrieves the
     * actual image from internal storage.
     *
     * @param path Path of the image
     * @param id The image ID
     * @return  Returns a Bitmap with the desired picture
     */
    private Bitmap retrieveImageFromDirectory(String path, int id) {
        try {
            Log.d("File", "Loading from directory: " + path);

            File file = new File(path, "image_" + id + ".jpeg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
        private TextView description;
        private ImageView imageView;


        public MyViewHolder(final View view, OnItemClickListener itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            name =  view.findViewById(R.id.favourites_section_header);
            description =  view.findViewById(R.id.favourites_section_description);
            imageView =  view.findViewById(R.id.favourites_section_image_cocktail);

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
