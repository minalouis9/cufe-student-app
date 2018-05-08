package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by ahmed on 5/4/2018.
 */

public class SemesterTermResults_Adapter  extends RecyclerView.Adapter<com.example.minal.studentapp.SemesterTermResults_Adapter.MyViewHolder> {

        //Data firelds:
        private Context context;
        private List<Semester_GradeTerm> cartList;

        //Methods:
        public SemesterTermResults_Adapter(Context context, List<Semester_GradeTerm> cartList)
        {
            this.context = context;
            this.cartList = cartList;
            //this.cartList.clear();
        }

        //internal class MyViewHolder declaration:
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView  CourseName;
            private TextView Term1_Grade;
            private TextView Term2_Grade;
            private TextView TotalGrade;

            View thisView;

            public MyViewHolder(View view)
            {
                super(view);


                CourseName = view.findViewById(R.id.CourseName_Sem);
                Term1_Grade = view.findViewById(R.id.Term_1_Res);
                Term2_Grade = view.findViewById(R.id.Term_2_Res);
                TotalGrade = view.findViewById(R.id.Total_Res);
                this.thisView = view;
            }
        }



        @Override
        public com.example.minal.studentapp.SemesterTermResults_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.semestertermgrade_item_list, parent, false);
            return new com.example.minal.studentapp.SemesterTermResults_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(com.example.minal.studentapp.SemesterTermResults_Adapter.MyViewHolder holder, final int position)
        {
            final Semester_GradeTerm slot = cartList.get(position);

            holder.CourseName.setText( slot.getCourseName());
            holder.Term1_Grade.setText(slot.getTerm1_Grade()+" / "+slot.getTerm1_Grade_Max());
            holder.Term2_Grade.setText(slot.getTerm2_Grade()+" / "+slot.getTerm2_Grade_Max());
            holder.TotalGrade.setText(slot.getTotalGrade()+" / "+slot.getTotalGradeMax());
            // holder.thisView.setBackground(Drawable.createFromPath("@drawable/listbkgrnd.png"));
        }

        @Override
        public int getItemCount()
        {
            return cartList.size();
        }
    }
