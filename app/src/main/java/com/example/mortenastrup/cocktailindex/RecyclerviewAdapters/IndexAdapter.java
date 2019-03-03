package com.example.mortenastrup.cocktailindex.RecyclerviewAdapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mortenastrup.cocktailindex.CocktailIndex;
import com.example.mortenastrup.cocktailindex.ImageLoader;
import com.example.mortenastrup.cocktailindex.MainActivity;
import com.example.mortenastrup.cocktailindex.Objects.Cocktail;
import com.example.mortenastrup.cocktailindex.OnItemClickListener;
import com.example.mortenastrup.cocktailindex.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
    public IndexAdapter(List<Cocktail> cocktailList, Map<Integer, Bitmap> imageMap, OnItemClickListener itemClickListener) {
        this.cocktailList = cocktailList;
        this.imageMap = imageMap;
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

        String[] array = new String[2];
        array[0] = cocktail.imagePath;
        array[1] = ""+cocktail.id;
        if(holder.imageView != null) {
            new ImageLoader(holder.imageView).execute(array);
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


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
