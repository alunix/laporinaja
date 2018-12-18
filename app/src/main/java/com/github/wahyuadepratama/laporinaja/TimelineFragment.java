package com.github.wahyuadepratama.laporinaja;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.wahyuadepratama.laporinaja.Adapter.ReportListAdapter;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

import java.util.ArrayList;


public class TimelineFragment extends Fragment {

    RecyclerView rvReportList;
    ProgressBar progressBarMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        rvReportList = view.findViewById(R.id.rv_report_list);

        ((MainActivity) getActivity()).setActionBarTitle("Timeline Report");
        ((MainActivity) getActivity()).getListReport(rvReportList);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                ((MainActivity) getActivity()).getListReport(rvReportList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
