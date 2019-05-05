package com.example.application.cocktailindex.RecyclerviewAdapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Fragments.DialogFragments.FillCocktailDetailsFragment;
import com.example.application.cocktailindex.Objects.Ingredient;
import com.example.application.cocktailindex.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * The string Adapter is created to be used in SelectCourseFragment, but can be used for all
 * RecyclerView which displays only a single string.
 *
 * @author Morten Astrup
 */
public class ImageAddAdapter extends RecyclerView.Adapter<ImageAddAdapter.MyViewHolder> {

    private Context context;
    private List<Uri> imageList;

    // Initialises the string list
    public ImageAddAdapter(List<Uri> imageList, Context context) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    @NonNull
    public ImageAddAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_chooseimage, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Uri uri = null;
        if(position >= 0) {
            uri = imageList.get(position);
        }

        // Loads image into new imageview
        if(uri != null) {
            Glide.with(context)
                    .load(uri)
                    .override(200, 200)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            Toast.makeText(context, "Error loading image",
                    Toast.LENGTH_SHORT).show();
        }

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((NewCocktailActivity)context).getFragmentName().removeImageFromList(position);
                } catch (NullPointerException e) {
                    Toast.makeText(context, "Error removing image!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    /**
     * Inner class for the specific views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton deleteImageButton;

        public MyViewHolder(final View view) {
            super(view);
            imageView = view.findViewById(R.id.recycler_view_add_image_imageview);
            deleteImageButton = view.findViewById(R.id.recycler_view_add_imagebutton_delete);

        }
    }
}
