package com.github.wahyuadepratama.laporinaja.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;
import com.github.wahyuadepratama.laporinaja.R;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;

import java.util.ArrayList;

/**
 * Created by wahyu on 02/12/18.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportHolder>{

    ArrayList<ReportItem> listReport = new ArrayList<>();
    OnReportItemClicked clickHandler;

    public void setListReport(ArrayList<ReportItem> reports){
        //daftarFilm.clear();
        listReport = reports;
        notifyDataSetChanged();
    }

    public void setClickHandler(OnReportItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public ReportListAdapter.ReportHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.report_row, viewGroup, false);
        return new ReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        ReportItem reportItem = listReport.get(position);
        holder.etType.setText(reportItem.getType_report());
        holder.etAddress.setText(reportItem.getAddress());
        holder.etDescription.setText(reportItem.getDescription());
        holder.etUpdatedAt.setText(reportItem.getUpdated_at());

        String url = UtilsApi.BASE_URL_IMAGE + reportItem.getPhoto();
        Glide.with(holder.itemView).load(url).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        if(listReport != null){
            return listReport.size();
        }
        return 0;
    }

    //View Holder untuk Report

    public interface OnReportItemClicked{
        void reportItemClicked(ReportItem m);
    }

    public class ReportHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        TextView etType;
        TextView etAddress;
        TextView etDescription;
        TextView etUpdatedAt;

        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            etType = itemView.findViewById(R.id.etType);
            etAddress = itemView.findViewById(R.id.etAddress);
            etDescription = itemView.findViewById(R.id.etDescription);
            etUpdatedAt = itemView.findViewById(R.id.etUpdatedAt);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ReportItem m = listReport.get(position);
                    clickHandler.reportItemClicked(m);
                }
            });
        }
    }
}
