package com.example.sleepdiary;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.SleepModel;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChartFragment extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<SleepModel> models = GlobalData.getInstance().getSleepModels();

        CombinedChart chart = view.findViewById(R.id.sleep_chart_item_combo_chart);

        initChart(chart, models);
        chart.setData(createChartData(models));
//        finishChart(chart);
        chart.getAxisLeft().setAxisMinimum(0f);

        final int HOUR_IN_SECONDS = 3600;
        chart.getAxisLeft().setLabelCount(Math.round(chart.getYMax()) / (HOUR_IN_SECONDS*2));
    }

    private CombinedData createChartData(List<SleepModel> models) {
        CombinedData data = new CombinedData();
        data.setData(createBarData(models));
        return data;
    }

    private BarData createBarData(List<SleepModel> models) {
        ArrayList<BarEntry> defaultSet = new ArrayList<>();
        for (int x = 0; x < models.size(); x++) {
            // Y-axis is time difference between start and end
            int y = models.get(x).getEndTimestamp() - models.get(x).getStartTimestamp();
            BarEntry entry = new BarEntry(x, y);
            defaultSet.add(entry);
        }
        BarDataSet dataSet = new BarDataSet(defaultSet, "unused");

        dataSet.setDrawValues(false);
        dataSet.setColor(0x6238a6, 0xff); // FIXME: this is not the way
        return new BarData(dataSet);
    }

    private LineData createLineData() {
        return null;
    }

    private void finishChart(CombinedChart chart) {
        float smallest = chart.getYMin();
        if (smallest < 4 * 3600) {
            chart.getAxisLeft().setAxisMinimum(0f);
            chart.getAxisLeft().setLabelCount(Math.round(chart.getYMax() / 3600));
        } else {
            chart.getAxisLeft().setAxisMinimum(4f * 3600);
            chart.getAxisLeft().setLabelCount((int)Math.floor((double)chart.getYMax() / 3600) - 4);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initChart(CombinedChart chart, List<SleepModel> models) {

        chart.setDoubleTapToZoomEnabled(false);

        // Styling, who knows
        chart.getAxisLeft().setTextSize(16f);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setAxisMaximum(models.size() - 0.5f);
        chart.getXAxis().setAxisMinimum(-0.5f);
        chart.getXAxis().setTextSize(14f);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd.MM");
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(models.stream()
                .map(SleepModel::getStartTimestamp)
                .map(unix -> dateFormat.format(new Date((long)unix * 1000)))
                .collect(Collectors.toList())));

        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.format("%dh", Math.round(value) / 3600);
            }
        });

        chart.setDrawOrder(new CombinedChart.DrawOrder[] {
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.LINE
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statschart, container, false);
    }
}
