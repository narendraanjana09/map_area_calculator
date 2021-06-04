package com.nsa.mapsspeech.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.nsa.mapsspeech.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nsa.mapsspeech.Activities.AddOnMap.deleteFromList;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {


    static Context context;
    ArrayList<String> list ;
   boolean getFromNet;
    private LayoutInflater mInflater;


      public ViewPagerAdapter(Context context, ArrayList<String> list,boolean getFromNet) {
          this.mInflater = LayoutInflater.from(context);
          this.context = context;
          this.list = list;
          this.getFromNet=getFromNet;

    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public void setGetFromNet(boolean getFromNet) {
        this.getFromNet = getFromNet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.images_pager_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Uri uri= Uri.parse(list.get(position));
        if(getFromNet){
            loadImage(holder.imageView,uri);
            holder.delete.setImageBitmap(null);
            holder.delete.setVisibility(View.INVISIBLE);
        }else{
            holder.imageView.setImageURI(uri);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteFromList(position);
                }
            });

        }

    }

    public void loadImage(ImageView imageView, Uri uri){
        Picasso
                .get()
                .load(uri)
                .noFade()
                .into(imageView);
    }
    @Override
    public int getItemCount() {

        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,delete;

        ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            delete=itemView.findViewById(R.id.deleteImageView);

        }
    }

}
