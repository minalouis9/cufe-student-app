package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by ahmed on 5/5/2018.
 */

public class semestertranscript1_Adapter   extends RecyclerView.Adapter<com.example.minal.studentapp.semestertranscript1_Adapter.MyViewHolder> {

        //Data firelds:
        private Context context;
        private List<String> cartList;
        Map<String,Year> yearData;

        //Methods:
        public semestertranscript1_Adapter(Context context, List<String> cartList, Map<String,Year> yearData)
        {
            this.context = context;
            this.cartList = cartList;
            this.yearData = yearData;
        }

        //internal class MyViewHolder declaration:
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            private TextView yearName;
            View thisView;

            public MyViewHolder(View view)
            {
                super(view);
                yearName = view.findViewById(R.id.CourseName_transcript);
                this.thisView = view;
            }
        }



        @Override
        public com.example.minal.studentapp.semestertranscript1_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.semestername, parent, false);
            return new com.example.minal.studentapp.semestertranscript1_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(com.example.minal.studentapp.semestertranscript1_Adapter.MyViewHolder holder, final int position)
        {
            final String slot = cartList.get(position);

            holder.yearName.setText(slot);

            holder.yearName.setOnClickListener(
                  new  View.OnClickListener()
                  {


                      @Override
                      public void onClick(View v) {

                          YearData.cartList = yearData.get(slot);
                          YearData.thisYearName = slot;
                          Intent To_SemesterTermResult = new Intent(context, YearData.class);
                          context.startActivity(To_SemesterTermResult);
                      }
                  }
            );
            // holder.thisView.setBackground(Drawable.createFromPath("@drawable/listbkgrnd.png"));
        }

        @Override
        public int getItemCount()
        {
            return cartList.size();
        }
    }