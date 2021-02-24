package com.example.sleepdiary;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChartFragment extends Fragment {
    private static final Calendar calendar = Calendar.getInstance();

    private enum WeekDay {
        MON(0),
        TUE(1),
        WED(2),
        THU(3),
        FRI(4),
        SAT(5),
        SUN(6);

        private final int value;
        WeekDay(int val){
            this.value = val;
        }

        public static WeekDay fromUnixTime(int unix) {
            calendar.setTimeInMillis((long)unix * 1000);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:    return MON;
                case Calendar.TUESDAY:   return TUE;
                case Calendar.WEDNESDAY: return WED;
                case Calendar.THURSDAY:  return THU;
                case Calendar.FRIDAY:    return FRI;
                case Calendar.SATURDAY:  return SAT;
                case Calendar.SUNDAY:    return SUN;
                default: return SUN; // Never runs
            }
        }

        public int getInt() {
            return value;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<SleepModel> models = GlobalData.getInstance().getSleepModels();
        SleepModel last = models.get(models.size()-1);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(last.getStartTimestamp() * 1000);
        int thisYear = c.get(Calendar.YEAR);
        int thisWeek = c.get(Calendar.WEEK_OF_YEAR);

        SleepModel[] weekAsArr = new SleepModel[7];
        models.stream()
                .filter(m -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(m.getStartTimestamp() * 1000);
                    return cal.get(Calendar.WEEK_OF_YEAR) == thisWeek  &&
                           cal.get(Calendar.YEAR) == thisYear; })
                .forEach(m -> weekAsArr[WeekDay.fromUnixTime(m.getStartTimestamp()).getInt()] = m);
        List<SleepModel> week = Arrays.stream(weekAsArr)
                .map(m -> m == null ? new SleepModel() : m)
                .collect(Collectors.toList());

        CombinedChart chart = view.findViewById(R.id.sleep_chart_item_combo_chart);

        initChart(chart, week);
        chart.setData(createChartData(week));

        final int HOUR_IN_SECONDS = 3600;
        final int yMax = (int)Math.ceil(chart.getYMax() / HOUR_IN_SECONDS) +1;

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(yMax * HOUR_IN_SECONDS);
        chart.getAxisLeft().setEnabled(false);
    }

    private CombinedData createChartData(List<SleepModel> models) {
        CombinedData data = new CombinedData();
        BarData bars = createBarData(models);
        data.setData(createBarData(models));

        final int HOUR_IN_SECONDS = 3600;
        data.setData(createLineData(7 * HOUR_IN_SECONDS, bars.getXMax()));
        return data;
    }

    private BarData createBarData(List<SleepModel> models) {
        ArrayList<BarEntry> defaultSet = new ArrayList<>();
        ArrayList<BarEntry> failSet = new ArrayList<>();

        final int HOUR_IN_SECONDS = 3600;

        for (int x = 0; x < models.size(); x++) {
            // Y-axis is time difference between start and end
            int y = models.get(x).getEndTimestamp() - models.get(x).getStartTimestamp();

            BarEntry entry = new BarEntry(x, y);
            if (y < 7 * HOUR_IN_SECONDS) {
                failSet.add(entry);
            } else {
                defaultSet.add(entry);
            }
        }
        BarDataSet successSet = new BarDataSet(defaultSet, "unused");
        BarDataSet failSet2 = new BarDataSet(failSet, "unused");

        successSet.setDrawValues(false);
        successSet.setColor(0x85E168, 0xff); // FIXME: this is not the way
        successSet.setHighLightAlpha(0x2e);

        failSet2.setDrawValues(false);
        failSet2.setColor(0xE44646, 0xff); // FIXME: this is not the way
        failSet2.setHighLightAlpha(0x2e);

        BarData resultData = new BarData();

        resultData.addDataSet(successSet);
        resultData.addDataSet(failSet2);

        return resultData;
    }

    private LineData createLineData(float y, float x) {
        ArrayList<Entry> lineSet = new ArrayList<>();

        lineSet.add(new Entry(-0.5f, y));
        lineSet.add(new Entry(x + 0.5f, y));

        LineDataSet set = new LineDataSet(lineSet, "asd");
        set.setColor(0xba1c1c, 0xff);
        set.setLineWidth(3.5f);
        set.setDrawValues(false);
        set.setDrawCircles(false);

        LineData data = new LineData(set);
        data.setValueTextSize(16f);
        data.setHighlightEnabled(false);
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initChart(CombinedChart chart, List<SleepModel> models) {

        // Styling, who knows
        chart.setDoubleTapToZoomEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setAxisMaximum(models.size() - 0.5f);
        chart.getXAxis().setAxisMinimum(-0.5f);
        chart.getXAxis().setTextSize(14f);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.getXAxis().setTextColor(Color.BLACK);
        chart.getXAxis().setTextSize(16f);

        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun",};

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));

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
