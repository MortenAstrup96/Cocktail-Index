package dev.astrup.cocktailindex.Modules.Index;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.OnItemClickListener;
import dev.astrup.cocktailindex.OnItemLongClickListener;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailSingleton;
import dev.astrup.cocktailindex.Utility.ImageUtilities;

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
    private ImageUtilities imageUtilities = ImageUtilities.getInstance();

    // Database
    private AppDatabase db;

    // Initialises the string list
    public IndexAdapter(List<Cocktail> cocktailList,  OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.cocktailList = cocktailList;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;

        // Setup of database
        db = AppDatabase.getDatabase(context);
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
        String displayIngredients = "";

        // Get image from internal storage
        holder.name.setText(name);

        setImage(holder, cocktail);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(cocktail.favourite);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    cocktail.favourite = b;
                    CocktailSingleton.getInstance().setFavourite(cocktail, db);
                }
            });


        if(position >= 0) {
            if(cocktail.ingredients.size() > 0) {
                displayIngredients = displayIngredients + cocktail.ingredients.get(0).getIngredient();
                for(int i = 1; i < cocktail.ingredients.size(); i++) {
                    displayIngredients = displayIngredients + ", " + cocktail.ingredients.get(i).getIngredient();
                }
            }

            holder.ingredients.setText(displayIngredients);
        }
    }

    private void setImage(MyViewHolder holder, Cocktail cocktail) {
        if(imageUtilities.hasFunctionalImage(cocktail)) {
            Glide.with(context)
                    .load(Uri.parse(cocktail.imagePath.get(0)))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(250, 250)
                    .circleCrop()
                    .into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_nopicture)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
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
        public CheckBox checkBox;

        public MyViewHolder(final View view, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;

            name =  view.findViewById(R.id.index_section_header);
            imageView =  view.findViewById(R.id.index_section_image_cocktail);
            checkBox = view.findViewById(R.id.index_section_favourite);
            ingredients = view.findViewById(R.id.index_section_details);


            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shortVibration();
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


        private void shortVibration() {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(5);
            }
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
