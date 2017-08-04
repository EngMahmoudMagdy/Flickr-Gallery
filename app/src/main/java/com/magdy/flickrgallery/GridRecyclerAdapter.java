package com.magdy.flickrgallery;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;


 class GridRecyclerAdapter  extends RecyclerView.Adapter<GridRecyclerAdapter.SimpleViewHolder> {

    private List<String> images;
    private Context context;

    GridInfoListener fListener;
    GridRecyclerAdapter(Context context , List<String>images , GridInfoListener fl)
    {
        this.images = images ;
        this.context = context ;
        fListener = fl ;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        final String image= images.get(position);

        Picasso.with(context).load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.headImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fListener.setSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

     class SimpleViewHolder extends RecyclerView.ViewHolder{

         ImageView  headImage ;
         SimpleViewHolder(View itemView) {
            super(itemView);

             headImage = (ImageView) itemView.findViewById(R.id.head_image);

        }
    }
}
