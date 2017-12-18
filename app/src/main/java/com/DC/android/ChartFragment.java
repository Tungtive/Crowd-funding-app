package com.DC.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by XZJ on 2017/10/20.
 */

public class ChartFragment extends android.support.v4.app.Fragment {
    LineChart myLineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_chart,container,false);
        myLineChart =(LineChart) view.findViewById(R.id.chart1);
        myLineChart.setDescription("爱心值");
        return view;
    }
    ;
}
