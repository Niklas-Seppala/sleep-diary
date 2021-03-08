package com.example.sleepdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.sleepdiary.data.models.SleepEntry;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This fragment displays all the entries in a listview.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class ListFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get context and make sure it all good.
        Context context = getContext();
        assert context != null;

        ListView listView = view.findViewById(R.id.sleepListView);
        List<SleepEntry> entryList = GlobalData.getInstance().getCompletedSleepEntries();
        listView.setAdapter(new SleepArrayAdapter(context, entryList));
        setListItemClickHandler(listView, context);
    }

    /**
     * When list item is clicked, open SleepEntryInsepction Activity
     * @param listView listView object.
     * @param context context
     */
    private void setListItemClickHandler(ListView listView, Context context) { // TODO: context ??
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
