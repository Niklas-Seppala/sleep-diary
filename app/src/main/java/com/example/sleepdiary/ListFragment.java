package com.example.sleepdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.adapters.SleepArrayAdapter;
import com.example.sleepdiary.data.GlobalData;

public class ListFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.sleepListView);
        assert getContext() != null;
        listView.setAdapter(new SleepArrayAdapter(getContext(),
                GlobalData.getInstance().getSleepEntries()
        ));

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent inspection = new Intent(view.getContext(), SleepEntryInspectionActivity.class);
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
