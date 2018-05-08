package com.example.minal.studentapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class YearData extends AppCompatActivity {

    final static String TAG = "kala";
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private yearData_Adapter mAdapter;
    TextView yearName;

    public static Year cartList;
    public static String thisYearName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_data);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recycler_view03);

        yearName = (TextView)findViewById(R.id.Current_Year);

        yearName.setText(thisYearName);
        mAdapter = new yearData_Adapter(YearData.this, cartList.getYearsGrades()); //Seif: change hre
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mShimmerViewContainer.stopShimmerAnimation();
        recyclerView.addItemDecoration(new MyDividerItemDecoration(YearData.this, LinearLayoutManager.VERTICAL, 16));



        mAdapter.notifyDataSetChanged();
        // stop animating Shimmer and hide the layout
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);

    }
}
