package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


/**
 * Created by ahmed on 4/15/2018.
 */


public class news_Rss_Adapter extends RecyclerView.Adapter<news_Rss_Adapter.MyViewHolder> {

    //Data firelds:
    private Context context;
    private List<FeedItem> cartList;

    //Methods:

    //internal class MyViewHolder declaration:
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView TitleNews,DescriptionOfNews,PublicationDate;
        ImageView News_Image;
        View thisView;

        public MyViewHolder(View view)
        {
            super(view);

            TitleNews = view.findViewById(R.id.TitleNews);
            DescriptionOfNews = view.findViewById(R.id.DescriptionOfNews);
            PublicationDate = view.findViewById(R.id.PublicationDate);
            News_Image = view.findViewById(R.id.News_Image);
            this.thisView = view;
        }
    }

    public news_Rss_Adapter(Context context, List<FeedItem> cartList)
    {
        this.context = context;
        this.cartList = cartList;
        //this.cartList.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_site_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final FeedItem slot = cartList.get(position);

        holder.TitleNews.setText( slot.getTitle());
        holder.DescriptionOfNews.setText(slot.getDescription());
        holder.PublicationDate.setText(slot.getPubDate());

        if(slot.getThumbnailUrl()!=null && slot.getThumbnailUrl().length()!= 0) {
            try {
                holder.News_Image.setImageBitmap(currentImage(slot.getThumbnailUrl()));
            }
            catch(Exception e)
            {
                e.printStackTrace();
                holder.News_Image.setImageResource(R.drawable.go2);
            }
        }
        else
        {
            holder.News_Image.setImageResource(R.drawable.go2);
        }


       // holder.thisView.setBackground(Drawable.createFromPath("@drawable/listbkgrnd.png"));

        holder.News_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent to_Site_Credit = new Intent(context, News.class);
                context.startActivity(to_Site_Credit);
                return ;
            }
            });
    }

    @Override
    public int getItemCount()
    {
        return cartList.size();
    }


    Bitmap currentImage(String urls)
    {
        String urldisplay = urls;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
}
