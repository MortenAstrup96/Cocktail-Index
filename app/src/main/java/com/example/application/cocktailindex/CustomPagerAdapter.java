package com.example.application.cocktailindex;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Utility.ImageUtilities;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mResources;
    private ImageView imageView;
    private ImageUtilities imageUtilities = ImageUtilities.getInstance();

    public CustomPagerAdapter(Context context, List<String> images) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mResources = images;
    }

    @Override
    public int getCount() {
        return this.mResources.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Get the image
        Uri image = Uri.parse(mResources.get(position));
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = mLayoutInflater.inflate(R.layout.inflatable_imageview, container, false);

        imageView = layout.findViewById(R.id.details_section_image_cocktail);
        Glide.with(layout)
                .load(image)
                .override(1200, 1200)
                .centerCrop()
                .into(imageView);

        ((ViewPager)container).addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View)view);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}