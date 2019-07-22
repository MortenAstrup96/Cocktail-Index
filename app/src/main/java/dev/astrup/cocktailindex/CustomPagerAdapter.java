package dev.astrup.cocktailindex;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import dev.astrup.cocktailindex.Utility.ImageUtilities;

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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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