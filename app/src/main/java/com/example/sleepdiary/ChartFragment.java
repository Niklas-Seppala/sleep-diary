package com.example.sleepdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.sleepdiary.data.models.SleepEntry;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private static final float CHART_X_MAX = 6.5f;
    private static final float CHART_X_MIN = -0.5f;
    private static final float CHART_Y_MIN = 0f;
    private static final int ANIM_DUR_MILLIS = 600;
    private static final int HIGHLIGHT_ALPHA = 0x2e;
    private static final float DISABLED_BTN_ALPHA = 0.5f;
    private static int savedIndex = 0;
    private static int successColor = -1;
    private static int failColor = -1;
    private float userGoal;
    private SleepHabits sleepHabits;

    private CombinedChart chartView;
    private TextView weekHeader;
    private TextView dateHeader;
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
        if (successColor > 0) return;
        successColor = getResources().getColor(R.color.accent_dark);
        failColor = getResources().getColor(R.color.accent_red);
    }

    private void getData() {
        sleepHabits = new SleepHabits(GlobalData.getInstance().getWeeklySleepHabits(), savedIndex);
        userGoal = GlobalData.getInstance().getCurrentUser().getGoal();
    }

    private void initHeaderViews(View view) {
        weekHeader = view.findViewById(R.id.sleep_chart_item_week_header_tv);
        dateHeader = view.findViewById(R.id.sleep_chart_item_date_tv);
        updateHeaderText();

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
            updateHeaderText();
            chartView.setData(createChartData(week));
            chartView.notifyDataSetChanged();
            chartView.animateY(ANIM_DUR_MILLIS, Easing.EaseOutCubic);
            nextBtn.setAlpha(sleepHabits.hasNextWeek() ? 1f : DISABLED_BTN_ALPHA);
            prevBtn.setAlpha(sleepHabits.hasPrevWeek() ? 1f : DISABLED_BTN_ALPHA);
        }
    }

    private void updateHeaderText() {
        int weekNum = sleepHabits.getWeek().getDate().getWeek();
        int year = sleepHabits.getWeek().getDate().getYear();
        weekHeader.setText(getString(R.string.time_week_number, weekNum));
        dateHeader.setText(getString(R.string.time_year, year));
    }

    private CombinedData createChartData(WeeklySleepHabit week) {
        CombinedData data = new CombinedData();
        data.setData(createBarData(week));
        data.setData(createLineData());
        return data;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private BarData createBarData(WeeklySleepHabit week) {
        ArrayList<BarEntry> successSet = new ArrayList<>();
        ArrayList<BarEntry> failSet = new ArrayList<>();

        SleepEntry[] weekDays = week.getDays();
        for (int x = 0; x < weekDays.length; x++) {
            float y = weekDays[x].getEndTimestamp() - weekDays[x].getStartTimestamp();
            BarEntry entry = new BarEntry((float) x, y);
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
                int intVal = (int)value;
                if (intVal <= 0) return "";
                return getString(R.string.time_h_min_dur_extra_short,
                        DateTime.getHoursFromSeconds(intVal),
                        DateTime.getMinutesFromSeconds(intVal));
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
        set.setColor(Color.BLACK);
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
                getResources().getStringArray(R.array.time_week_days_en)));

        chartView.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE
        });
        setBarEntryClicks(view);
    }

    private void setBarEntryClicks(View view) {
        chartView.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SleepEntry[] week = sleepHabits.getWeek().getDays();
                int id = week[(int)e.getX()].getId();
                List<SleepEntry> allEntries = GlobalData.getInstance().getSleepEntries();
                for (int i = 0; i < allEntries.size(); i++) {
                    if (allEntries.get(i).getId() == id) {
                        Intent inspection = new Intent(view.getContext(),
                                SleepEntryInspectionActivity.class);
                        inspection.putExtra(SleepEntryInspectionActivity.EXTRA_ENTRY_INDEX, i);
                        startActivity(inspection);
                    }
                }
            }

            @Override
            public void onNothingSelected() { }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        saveWeek();
    }

    private void saveWeek() {
        savedIndex = sleepHabits.getIndex();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statschart, container, false);
    }
}
