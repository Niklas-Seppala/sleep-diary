package com.example.sleepdiary;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.SleepHabits;
import com.example.sleepdiary.data.SleepModel;
import com.example.sleepdiary.data.WeeklySleepHabit;
import com.example.sleepdiary.time.DateTime;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private static int savedIndex = 0;

    private static final float CHART_X_MAX = 6.5f;
    private static final float CHART_X_MIN = -0.5f;
    private static final float CHART_Y_MIN = 0f;
    private static final int ANIM_DUR_MILLIS = 600;

    private static int successColor = -1;
    private static int failColor = -1;
    private static final int HIGHLIGHT_ALPHA = 0x2e;
    private static final float DISABLED_BTN_ALPHA = 0.5f;

    private float userGoal;
    private SleepHabits sleepHabits;

    private CombinedChart chartView;
    private TextView weekHeader;
    private ImageButton nextBtn;
    private ImageButton prevBtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initColors();
        getData();
        initHeaderViews(view);
        initChart(view);
        displayWeek(sleepHabits.getWeek());
    }

    private void initColors() {
        // Only run once per app lifetime
        if (successColor > 0) return;

        successColor = getResources().getColor(R.color.teal_200);
        failColor = getResources().getColor(R.color.teal_700);
    }

    private void getData() {
        sleepHabits = new SleepHabits(GlobalData.getInstance().getSleepModelsByWeeks(), savedIndex);
        userGoal = GlobalData.getInstance().getCurrentUser().getGoal();
    }

    private void initHeaderViews(View view) {
        weekHeader = view.findViewById(R.id.sleep_chart_item_week_header_tv);
        weekHeader.setText(getString(R.string.week_header, sleepHabits.getWeek().getWeekNum()));

        prevBtn = view.findViewById(R.id.sleep_chart_item_btn_prev_week);
        prevBtn.setOnClickListener(this::changeWeek);

        nextBtn = view.findViewById(R.id.sleep_chart_item_btn_next_week);
        nextBtn.setOnClickListener(this::changeWeek);
    }

    private void changeWeek(View view) {
        int currentWeek = sleepHabits.getIndex();
        if (view.getId() == R.id.sleep_chart_item_btn_next_week)
            sleepHabits.nextWeek();
        else
            sleepHabits.previousWeek();

        if (currentWeek != sleepHabits.getIndex())
            displayWeek(sleepHabits.getWeek());
    }

    private void displayWeek(WeeklySleepHabit week) {
        if (week != null) {
            chartView.setData(createChartData(week));
            weekHeader.setText(getString(R.string.week_header, sleepHabits.getWeek().getWeekNum()));

            chartView.notifyDataSetChanged();
            chartView.animateY(ANIM_DUR_MILLIS, Easing.EaseOutCubic);
            nextBtn.setAlpha(sleepHabits.hasNextWeek() ? 1f : DISABLED_BTN_ALPHA);
            prevBtn.setAlpha(sleepHabits.hasPrevWeek() ? 1f : DISABLED_BTN_ALPHA);
        }
    }

    private CombinedData createChartData(WeeklySleepHabit week) {
        CombinedData data = new CombinedData();
        data.setData(createBarData(week));
        data.setData(createLineData());
        return data;
    }

    private BarData createBarData(WeeklySleepHabit week) {
        ArrayList<BarEntry> successSet = new ArrayList<>();
        ArrayList<BarEntry> failSet = new ArrayList<>();

        SleepModel[] weekDays = week.getDays();
        for (int x = 0; x < weekDays.length; x++) {
            float y = weekDays[x].getEndTimestamp() - weekDays[x].getStartTimestamp();
            BarEntry entry = new BarEntry((float)x, y);
            List<BarEntry> set = y < userGoal ? failSet : successSet;
            set.add(entry);
        }

        BarDataSet successEntries = new BarDataSet(successSet, "");
        BarDataSet failedEntries = new BarDataSet(failSet, "");

        styleBars(successEntries, successColor);
        styleBars(failedEntries, failColor);

        BarData resultData = new BarData();
        resultData.addDataSet(successEntries);
        resultData.addDataSet(failedEntries);
        return resultData;
    }

    private void styleBars(BarDataSet set, int color) {
        set.setDrawValues(true);
        set.setValueTextSize(16f);
        set.setColor(color);
        set.setHighLightAlpha(HIGHLIGHT_ALPHA);

        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int)value <= 0) ? "" : DateTime.secondsToTimeString("%dh %dm", (int)value);
            }
        });
    }

    private LineData createLineData() {
        LineDataSet set = new LineDataSet(null, "");
        set.addEntry(new Entry(CHART_X_MIN, userGoal));
        set.addEntry(new Entry(CHART_X_MAX, userGoal));
        styleLineData(set);
        LineData data = new LineData(set);
        data.setHighlightEnabled(false);
        return data;
    }

    private void styleLineData(LineDataSet set) {
        set.setColor(Color.BLACK, 0x80);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(3f);
        set.setDrawValues(false);
        set.setDrawCircleHole(false);
        set.setCircleRadius(4f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initChart(View view) {
        chartView = view.findViewById(R.id.sleep_chart_item_combo_chart);
        chartView.setDoubleTapToZoomEnabled(false);
        chartView.getAxisRight().setEnabled(false);
        chartView.getDescription().setEnabled(false);
        chartView.getLegend().setEnabled(false);
        chartView.getXAxis().setDrawGridLines(false);
        chartView.getAxisLeft().setEnabled(false);

        // Chart axis range
        chartView.getXAxis().setAxisMaximum(CHART_X_MAX);
        chartView.getXAxis().setAxisMinimum(CHART_X_MIN);
        chartView.getAxisLeft().setAxisMinimum(CHART_Y_MIN);

        // Text styling on bar entries
        chartView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chartView.getXAxis().setTextColor(Color.BLACK);
        chartView.getXAxis().setTextSize(16f);
        chartView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(
                getResources().getStringArray(R.array.week_days_en)));

        chartView.setDrawOrder(new CombinedChart.DrawOrder[] {
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.LINE
        });
    }

    @Override
    public void onStop() {
        savedIndex = sleepHabits.getIndex();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statschart, container, false);
    }
}
