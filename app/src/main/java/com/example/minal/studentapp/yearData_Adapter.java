package com.example.minal.studentapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahmed on 5/5/2018.
 */

public class yearData_Adapter  extends RecyclerView.Adapter<com.example.minal.studentapp.yearData_Adapter.MyViewHolder> {

        //Data firelds:
        private Context context;
        private List<Semester_GradeTerm> cartList;

        //Methods:
        public yearData_Adapter(Context context, List<Semester_GradeTerm> cartList)
        {
            this.context = context;
            this.cartList = cartList;

            //this.cartList.clear();
        }

        //internal class MyViewHolder declaration:
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView CourseName;
            private TextView Term1_Grade;
            private TextView Term2_Grade;
            private TextView TotalGrade;

            View thisView;

            public MyViewHolder(View view)
            {
                super(view);


                CourseName = view.findViewById(R.id.CourseName_Trans);
                Term1_Grade = view.findViewById(R.id.Term_1_Res_Trans);
                Term2_Grade = view.findViewById(R.id.Term_2_Res_Trans);
                TotalGrade = view.findViewById(R.id.Total_Res_Trans);
                this.thisView = view;
            }
        }



        @Override
        public com.example.minal.studentapp.yearData_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.semester_yeardetails_dialogue, parent, false);
            return new com.example.minal.studentapp.yearData_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(com.example.minal.studentapp.yearData_Adapter.MyViewHolder holder, final int position)
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
