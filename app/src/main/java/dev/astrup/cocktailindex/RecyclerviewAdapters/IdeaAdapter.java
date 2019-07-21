package dev.astrup.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.OnItemClickListener;
import dev.astrup.cocktailindex.OnItemLongClickListener;
import com.astrup.cocktailindex.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailSingleton;

import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.MyViewHolder> {


    // The list containing all the objects with the required information for at piece.
    private List<Cocktail> cocktailList;

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private Context context;


    // Database
    private AppDatabase db;

    // Initialises the string list
    public IdeaAdapter(List<Cocktail> cocktailList, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.cocktailList = cocktailList;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;

        // Setup of database
        db = AppDatabase.getDatabase(context);
    }

    @Override
    @NonNull
    public IdeaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_idea, parent, false);

        java.util.Collections.sort(cocktailList);

        return new MyViewHolder(itemView, itemClickListener, itemLongClickListener);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        java.util.Collections.sort(cocktailList);
        Cocktail cocktail = cocktailList.get(position);
        String name = cocktail.name;
        String displayIngredients = "";

        // Get image from internal storage
        holder.name.setText(name);


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
        private TextView ingredients;


        public MyViewHolder(final View view, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;

            this.setIsRecyclable(true); // TODO: VERY BAD PRACTICE -> Fix: https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022

            name =  view.findViewById(R.id.idea_recycler_view_header);
            ingredients = view.findViewById(R.id.idea_recycler_view_ingredients);


            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);

            cocktailList = CocktailSingleton.getInstance().getIdeas();

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
