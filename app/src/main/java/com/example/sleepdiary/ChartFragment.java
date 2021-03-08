package com.example.sleepdiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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

import com.example.sleepdiary.adapters.SleepArrayAdapter;
import com.example.sleepdiary.adapters.SleepRatingIconService;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.SleepHabits;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.Rating;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.WeeklySleepHabit;
import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Displays weekly SleepEntries in a bar chart.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
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
        initViews(view);
        initChart(view);
        displayWeek(sleepHabits.getWeek());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.isDirty()) {
            DbConnection db = new DbConnection(getContext());
            GlobalData.update(db);
            db.close();
            getData();
        }
        displayWeek(sleepHabits.getWeek());
    }

    /**
     * Get the colors for barchart from resources.
     */
    private void initColors() {
        if (successColor > 0) return;
        successColor = getResources().getColor(R.color.dark_gray_blue);
        failColor = getResources().getColor(R.color.accent_red);
    }

    /**
     * Get the data for the chart from GlobalData singleton;
     */
    private void getData() {
        sleepHabits = new SleepHabits(GlobalData.getInstance().getWeeklySleepHabits(), savedIndex);
        User user = GlobalData.getInstance().getCurrentUser();
        assert user != null;
        userGoal = user.getSleepGoal();
    }

    /**
     *  Find the textviews and buttons, add event handlers.
     * @param view ChartFragment
     */
    private void initViews(View view) {
        weekHeader = view.findViewById(R.id.sleep_chart_item_week_header_tv);
        dateHeader = view.findViewById(R.id.sleep_chart_item_date_tv);
        updateHeaderText();

        prevBtn = view.findViewById(R.id.sleep_chart_item_btn_prev_week);
        prevBtn.setOnClickListener(this::changeWeek);

        nextBtn = view.findViewById(R.id.sleep_chart_item_btn_next_week);
        nextBtn.setOnClickListener(this::changeWeek);
    }

    /**
     * Change week action
     * @param view Button
     */
    private void changeWeek(View view) {
        int currentWeek = sleepHabits.getIndex();
        if (view.getId() == R.id.sleep_chart_item_btn_next_week)
            sleepHabits.nextWeek();
        else
            sleepHabits.previousWeek();

        if (currentWeek != sleepHabits.getIndex())
            displayWeek(sleepHabits.getWeek());
    }

    /**
     * Display chart and header info for specified week.
     * @param week Week containing SleepEntries
     */
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

    /**
     * Set week and year strings to view
     */
    private void updateHeaderText() {
        int weekNum = sleepHabits.getWeek().getDate().getWeek();
        int year = sleepHabits.getWeek().getDate().getYear();
        weekHeader.setText(getString(R.string.time_week_number, weekNum));
        dateHeader.setText(getString(R.string.time_year, year));
    }

    /**
     * Create data for specified week for CombinedChart.
     * @param week week containing SleepEntries
     * @return Data for the chart.
     */
    private CombinedData createChartData(WeeklySleepHabit week) {
        CombinedData data = new CombinedData();
        data.setData(createBarData(week));
        data.setData(createGoalLineData());

        if (AppSettings.getInstance().getChartTrackCaffeine()) {
            data.setData(createCaffeineData(week));
        }
        return data;
    }

    /**
     * Gets Sleep rating icon and resizes it to match
     * bar entry.
     * Resize is implemented using a workaround detailed in:
     *      https://stackoverflow.com/questions/42395036/resize-vectordrawable-icon-programmatically
     * @param rating user rating
     * @return Drawable bitmap
     */
    private Drawable getResizedIcon(Rating rating) {
        Drawable drawable = SleepRatingIconService.getIconFromRating(getContext(), rating);
        if (drawable == null) return null;

        VectorDrawable vectorDrawable = (VectorDrawable)drawable;
        Bitmap bm = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.setBounds(20, 20,
                canvas.getWidth() - 20, canvas.getHeight() -20);
        vectorDrawable.draw(canvas);
        return new BitmapDrawable(getResources(), bm);
    }

    /**
     * Create bar data for specified week.
     * SleepEntries are divided to two sets, based
     * on user's goal.
     *
     * @param week Week to be displayed to user.
     * @return BarData object for specified week.
     */
    private BarData createBarData(WeeklySleepHabit week) {
        ArrayList<BarEntry> successSet = new ArrayList<>();
        ArrayList<BarEntry> failSet = new ArrayList<>();

        SleepEntry[] weekDays = week.getDays();
        for (int x = 0; x < weekDays.length; x++) {
            float y = weekDays[x].getEndTimestamp() - weekDays[x].getStartTimestamp();
            BarEntry entry = new BarEntry((float) x, y);

            if (AppSettings.getInstance().getChartTrackQualityRating()) {
                entry.setIcon(getResizedIcon(weekDays[x].getQuality()));
                if (entry.getIcon() != null) {
                    // Set icon location
                    entry.getIcon().setBounds(0, 100, 0, 0);
                }
            }
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

    /**
     * Set styling for bar data set.
     * @param barDataSet Bar data set
     * @param color Color of the bars in this set
     */
    private void styleBars(BarDataSet barDataSet, int color) {
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(16f);
        barDataSet.setColor(color);
        barDataSet.setHighLightAlpha(HIGHLIGHT_ALPHA);

        barDataSet.setValueFormatter(new ValueFormatter() {
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

    /**
     * Create the caffeine consumption line data of
     * specified week.
     * @param week week containing SleepEntries
     * @return Caffeine linedata
     */
    private LineData createCaffeineData(WeeklySleepHabit week) {
        List<Integer> caffeine = Arrays.stream(week.getDays())
                .mapToInt(SleepEntry::getCaffeineIntake)
                .boxed()
                .collect(Collectors.toList());

        List<Entry> caffeineEntries = new ArrayList<>();
        for (int i = 0; i < caffeine.size(); i++) {
            caffeineEntries.add(new Entry(i, caffeine.get(i)));
        }

        LineDataSet caffeineSet = new LineDataSet(caffeineEntries, "caffeine");
        caffeineSet.setValueTextSize(20f);
        caffeineSet.setCircleRadius(6f);
        caffeineSet.setCircleColor(Color.BLACK);
        caffeineSet.setDrawCircleHole(false);
        caffeineSet.setLineWidth(3f);
        caffeineSet.setColor(Color.BLACK);

        caffeineSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        return new LineData(caffeineSet);
    }

    /**
     * Create line data based on user goal.
     * (Straight horizontal line)
     * @return LineData object.
     */
    private LineData createGoalLineData() {
        LineDataSet set = new LineDataSet(null, "");
        set.addEntry(new Entry(CHART_X_MIN, userGoal));
        set.addEntry(new Entry(CHART_X_MAX, userGoal));
        styleGoalLineData(set);
        LineData data = new LineData(set);
        data.setHighlightEnabled(false);
        return data;
    }

    /**
     * Set styling for line.
     * @param lineDataSet set of line data.
     */
    private void styleGoalLineData(LineDataSet lineDataSet) {
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleRadius(4f);
    }

    /**
     * Set basic chart styling and settings.
     * @param view ChartFragment.
     */
    private void initChart(View view) {
        chartView = view.findViewById(R.id.sleep_chart_item_combo_chart);
        chartView.setDoubleTapToZoomEnabled(false);
        chartView.getAxisRight().setEnabled(false);
        chartView.getDescription().setEnabled(false);
        chartView.getLegend().setEnabled(false);
        chartView.getXAxis().setDrawGridLines(false);
        chartView.getAxisLeft().setEnabled(false);

        chartView.getAxisRight().setAxisMaximum(10);

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

    /**
     * Set click eventhandlers to bars. When user clicks one of the bars,
     * SleepEntryInspection Activity is launched.
     * @param view ChartFragment.
     */
    private void setBarEntryClicks(View view) {
        chartView.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SleepEntry[] week = sleepHabits.getWeek().getDays();
                int id = week[(int)e.getX()].getId();
                List<SleepEntry> allEntries = GlobalData.getInstance().getSleepEntries();
                for (int i = 0; i < allEntries.size(); i++) {
                    if (allEntries.get(i).getId() == id) {
                        Intent inspection = new Intent(view.getContext(), SleepEntryInspectionActivity.class);
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

    /**
     * Save the current week index to static field.
     */
    private void saveWeek() {
        savedIndex = sleepHabits.getIndex();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statschart, container, false);
    }
}
