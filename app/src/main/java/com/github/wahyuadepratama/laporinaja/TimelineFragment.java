package com.github.wahyuadepratama.laporinaja;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wahyuadepratama.laporinaja.Adapter.ReportListAdapter;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

import java.util.ArrayList;


public class TimelineFragment extends Fragment {

    RecyclerView rvReportList;
    ReportListAdapter reportListAdapter;
    ArrayList<ReportItem> listReport = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        reportListAdapter = new ReportListAdapter();
        reportListAdapter.setListReport(listReport);
//        reportListAdapter.setClickHandler(this);

        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        rvReportList = rootView.findViewById(R.id.rv_report_list);
        rvReportList.setAdapter(reportListAdapter);

        ((MainActivity) getActivity()).setActionBarTitle("Timeline Report");
        ((MainActivity) getActivity()).getListReport();
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

}
