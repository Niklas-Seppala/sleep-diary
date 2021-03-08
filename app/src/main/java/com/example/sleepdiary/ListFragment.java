package com.example.sleepdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.adapters.SleepArrayAdapter;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.SleepEntry;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This fragment displays all the entries in a listview.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class ListFragment extends Fragment {

    private ListView lv;
    private List<SleepEntry> entries;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get context and make sure it all good.
        Context context = getContext();
        assert context != null;

        lv = view.findViewById(R.id.sleepListView);
        entries = GlobalData.getInstance().getCompletedSleepEntries();
        lv.setAdapter(new SleepArrayAdapter(context, entries));
        setListItemClickHandler(lv, context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.isDirty()) {
            DbConnection db = new DbConnection(getContext());
            GlobalData.update(db);
            db.close();

            entries = GlobalData.getInstance().getCompletedSleepEntries();
            lv.setAdapter(new SleepArrayAdapter(getContext(), entries));
        }

        Log.d("TAG", "onResume: " );
    }

    /**
     * When list item is clicked, open SleepEntryInsepction Activity
     * @param listView listView object.
     * @param context context
     */
    private void setListItemClickHandler(ListView listView, Context context) {
        listView.setOnItemClickListener((a, v, i, l) -> {
            Intent inspection = new Intent(context, SleepEntryInspectionActivity.class);
            inspection.putExtra(SleepEntryInspectionActivity.EXTRA_ENTRY_INDEX, i);
            startActivity(inspection);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statslist, container, false);
    }
}
