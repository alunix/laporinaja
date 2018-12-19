package com.github.wahyuadepratama.laporinaja;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.wahyuadepratama.laporinaja.ApiHelper.BaseApiService;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailReportActivity extends AppCompatActivity {

    ImageView photoDetail;
    ImageView photoDetailLandscape;
    TextView etType, etLocation, etLat, etLang, etOwner, etUpdatedAt, etStatus, etDescription;
    TextView etTypeLandscape, etLocationLandscape, etLatLandscape, etLangLandscape, etOwnerLandscape, etUpdatedAtLandscape, etStatusLandscape, etDescriptionLandscape;

    Context mContext;
    BaseApiService mApiService;
    Integer id;
    ImageView favoriteImage;
    ReportItem report;
    String favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setContentView(R.layout.landscape_activity_detail_report);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            photoDetailLandscape = findViewById(R.id.photoDetailLandscape);
            etTypeLandscape = findViewById(R.id.etTypeDetailLandscape);
            etLocationLandscape = findViewById(R.id.etLocationDetailLandscape);
            etLatLandscape = findViewById(R.id.etLatDetailLandscape);
            etLangLandscape = findViewById(R.id.etLangDetailLandscape);
            etOwnerLandscape = findViewById(R.id.etOwnerDetailLandscape);
            etUpdatedAtLandscape = findViewById(R.id.etUpdateAtDetailLandscape);
            etStatusLandscape = findViewById(R.id.etStatusDetailLandscape);
            etDescriptionLandscape = findViewById(R.id.etDescriptionDetailLandscape);

            Intent intent = getIntent();
            if(intent != null){
                ReportItem report = intent.getParcelableExtra("report");
                id = report.getId();
                etTypeLandscape.setText(report.getType_report());
                etLocationLandscape.setText(String.valueOf(report.getAddress()));
                etLatLandscape.setText(String.valueOf(report.getLat()));
                etLangLandscape.setText(String.valueOf(report.getLang()));
                etOwnerLandscape.setText(String.valueOf(report.getOwner()));
                etUpdatedAtLandscape.setText(String.valueOf(report.getUpdated_at()));
                etStatusLandscape.setText(String.valueOf(report.getStatus()));
                etDescriptionLandscape.setText(report.getDescription());
                favorite = report.getFavorite();

                String url = UtilsApi.BASE_URL_IMAGE + report.getPhoto();
                Glide.with(this)
                        .load(url)
                        .into(photoDetailLandscape);

                getSupportActionBar().setTitle(String.valueOf(report.getType_report()));
            }

        }else{

            setContentView(R.layout.activity_detail_report);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Share this report to another application!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    shareText(view);
                }
            });

            photoDetail = findViewById(R.id.photoDetail);
            etType = findViewById(R.id.etTypeDetail);
            etLocation = findViewById(R.id.etLocationDetail);
            etLat = findViewById(R.id.etLatDetail);
            etLang = findViewById(R.id.etLangDetail);
            etOwner = findViewById(R.id.etOwnerDetail);
            etUpdatedAt = findViewById(R.id.etUpdateAtDetail);
            etStatus = findViewById(R.id.etStatusDetail);
            etDescription = findViewById(R.id.etDescriptionDetail);

            Intent intent = getIntent();
            if(intent != null){
                report = intent.getParcelableExtra("report");
                id = report.getId();
                etType.setText(report.getType_report());
                etLocation.setText(String.valueOf(report.getAddress()));
                etLat.setText(String.valueOf(report.getLat()));
                etLang.setText(String.valueOf(report.getLang()));
                etOwner.setText(String.valueOf(report.getOwner()));
                etUpdatedAt.setText(String.valueOf(report.getUpdated_at()));
                etStatus.setText(String.valueOf(report.getStatus()));
                etDescription.setText(report.getDescription());
                favorite = report.getFavorite();

                String url = UtilsApi.BASE_URL_IMAGE + report.getPhoto();
                Glide.with(this)
                        .load(url)
                        .into(photoDetail);

                getSupportActionBar().setTitle(String.valueOf(report.getType_report()));
            }
        }

        changeIconFavorite();
    }

    public void onBackPressed() {
        Intent intent = new Intent(DetailReportActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeIconFavorite(){
        if(favorite.equals("true")){
            favoriteImage = findViewById(R.id.favoriteImage);
            favoriteImage.setImageResource(R.drawable.favorite);
        }else{
            favoriteImage = findViewById(R.id.favoriteImage);
            favoriteImage.setImageResource(R.drawable.unfavorite);
        }
    }

    public void shareText(View view){
        String dataJenisLaporan = etType.getText().toString();
        String dataDescription = etDescription.getText().toString();
        String dataLocation = etLocation.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Jenis Laporan: " + dataJenisLaporan + "\n"
                + "Deskripsi: " + dataDescription + "\n"
                + "Lokasi: " + dataLocation + "\n"
                + "More info: laporinaja.com/2192123912/11/2018/");
        if (shareIntent.resolveActivity(getPackageManager()) != null){
            startActivity(shareIntent);
        }
    }

    public void addFavoriteToServer(View v){
        mApiService.addFavorite(id).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("status").equals("success")){

                            if(jsonRESULTS.getString("favorite").equals("true")){
                                favoriteImage = findViewById(R.id.favoriteImage);
                                favoriteImage.setImageResource(R.drawable.favorite);
                                Toast.makeText(mContext, "Laporan Ditambahkan ke Favorit!", Toast.LENGTH_LONG).show();
                            }else{
                                favoriteImage = findViewById(R.id.favoriteImage);
                                favoriteImage.setImageResource(R.drawable.unfavorite);
                                Toast.makeText(mContext, "Laporan Dihapus dari Favorit!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            String error_message = jsonRESULTS.getString("error_msg");
                            Toast.makeText(mContext, error_message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext, "Terdapat Masalah!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(mContext, "Masalah Koneksi Jaringan!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Server Sedang Maintenance!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Masalah Koneksi Jaringan!", Toast.LENGTH_SHORT).show();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

}
