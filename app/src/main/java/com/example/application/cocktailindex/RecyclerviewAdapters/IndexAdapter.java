package com.example.application.cocktailindex.RecyclerviewAdapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
import com.example.application.cocktailindex.Database.AppDatabase;
import com.example.application.cocktailindex.Fragments.IndexFragment;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.OnItemClickListener;
import com.example.application.cocktailindex.OnItemLongClickListener;
import com.example.application.cocktailindex.R;
import com.example.application.cocktailindex.Utility.CocktailSingleton;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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

    // Database
    private AppDatabase db;

    // Initialises the string list
    public IndexAdapter(List<Cocktail> cocktailList,  OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.cocktailList = cocktailList;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;

        // Setup of database
        db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name").build();
    }

    @Override
    @NonNull
    public IndexAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_index, parent, false);

        java.util.Collections.sort(cocktailList);


        return new MyViewHolder(itemView, itemClickListener, itemLongClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        java.util.Collections.sort(cocktailList);
        Cocktail cocktail = cocktailList.get(position);
        String name = cocktail.name;
        String recipe = cocktail.recipe;
        boolean isChecked = cocktail.favourite;
        String displayIngredients = "";

        // Get image from internal storage
        holder.name.setText(name);

        Glide.with(context)
                .load(Uri.parse(cocktail.imagePath))
                .override(200, 200)
                .centerInside()
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);

        checkBox.setChecked(cocktail.favourite);

        if(position >= 0) {
            cocktail = cocktailList.get(position);
            if(cocktail.ingredients.size() > 0) {
                displayIngredients = displayIngredients + cocktail.ingredients.get(0).getIngredient();
                for(int i = 1; i < cocktail.ingredients.size(); i++) {
                    displayIngredients = displayIngredients + ", " + cocktail.ingredients.get(i).getIngredient();
                }
            }

            holder.ingredients.setText(displayIngredients);
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
    public class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnCreateContextMenuListener {

        private OnItemClickListener itemClickListener;
        private OnItemLongClickListener itemLongClickListener;
        private TextView name;
        private ImageView imageView;
        private TextView ingredients;


        public MyViewHolder(final View view, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;

            this.setIsRecyclable(false); // TODO: VERY BAD PRACTICE -> Fix: https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022

            name =  view.findViewById(R.id.index_section_header);
            imageView =  view.findViewById(R.id.index_section_image_cocktail);
            checkBox = view.findViewById(R.id.index_section_favourite);
            ingredients = view.findViewById(R.id.index_section_details);

            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);



            cocktailList = CocktailSingleton.getInstance().getCocktailList();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(getAdapterPosition() >= 0) {
                        cocktailList.get(getAdapterPosition()).favourite = b; // Updates local

                        // Updates favourites in db
                        Executor myExecutor = Executors.newSingleThreadExecutor();
                        myExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                db.cocktailDBDao().updateOne(cocktailList.get(getAdapterPosition()));
                            }
                        });


                    }
                }
            });

            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    position = getAdapterPosition();
                    return false;
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




        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            contextMenu.add(0, view.getId(), 0, "Edit Cocktail");
            contextMenu.add(0, view.getId(), 0, "Delete Cocktail");
        }
    }

    public int getPosition() {
        return position;
    }

    private int position;

}
