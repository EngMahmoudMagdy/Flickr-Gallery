package com.magdy.flickrgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    LayoutInflater layoutInflater ;
    Context c ;
    List<String>links ;



    public ViewPagerAdapter(Context context, List<String> links  )
    {
        c = context ;
        this.links = links;


    }
    /*void setBitmaps (ArrayList<Bitmap> bitmap)
    {
        this.bitmaps = bitmap;
    }*/

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.view_pager_image,container,false);


        final ImageView imageView= v.findViewById(R.id.image);
        Picasso.with(c).load(links.get(position)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        container.addView(v);
        return v;
    }


    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
