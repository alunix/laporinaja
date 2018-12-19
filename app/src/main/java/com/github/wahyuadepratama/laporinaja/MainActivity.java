package com.github.wahyuadepratama.laporinaja;


import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.wahyuadepratama.laporinaja.Adapter.ReportListAdapter;
import com.github.wahyuadepratama.laporinaja.ApiHelper.BaseApiService;
import com.github.wahyuadepratama.laporinaja.Database.AppDatabase;
import com.github.wahyuadepratama.laporinaja.Database.Favorite;
import com.github.wahyuadepratama.laporinaja.Database.Report;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;
import com.github.wahyuadepratama.laporinaja.Model.ReportList;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static com.github.wahyuadepratama.laporinaja.LoginActivity.ID;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.NAME;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.EMAIL;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.PHONE;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.SESSION_STATUS;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.STATUS;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.UPDATED_AT;

public class MainActivity extends AppCompatActivity implements ReportListAdapter.OnReportItemClicked {

    private static final String TAG = "MainActivity";
    private ProfileFragment profileFragment = new ProfileFragment();
    private TimelineFragment timelineFragment = new TimelineFragment();
    private NewReportFragment newReportFragment = new NewReportFragment();

    public SharedPreferences sharedpreferences;
    public String id, name, email, phone, status, updated_at;

    Bitmap imageBitmap;
    ImageView imgView;

    ReportListAdapter reportListAdapter;
    ArrayList<ReportItem> listReport = new ArrayList<>();

    BaseApiService client;
    ProgressBar progressBarMain;

    private static int REQUEST_IMAGE_CAPTURE = 1;

    String imagebase64string;
    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;

    AppDatabase mDb;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_placeholder, profileFragment)
                            .commit();
                    return true;
                case R.id.navigation_timeline:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_placeholder, timelineFragment)
                            .commit();
                    return true;
                case R.id.navigation_new_report:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_placeholder, newReportFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.activity_main);

        progressBarMain = findViewById(R.id.progressBarMain);

        if (getSupportActionBar().getTitle().toString().equals("Profile")){
            progressBarMain.setVisibility(View.INVISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, profileFragment)
                    .commit();
        }else if (getSupportActionBar().getTitle().toString().equals("Timeline Report")){
            progressBarMain.setVisibility(View.INVISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, timelineFragment)
                    .commit();
        }else if (getSupportActionBar().getTitle().toString().equals("New Report")){
            progressBarMain.setVisibility(View.INVISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, newReportFragment)
                    .commit();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarMain.setVisibility(View.INVISIBLE);
                if (getSupportActionBar().getTitle().toString().equals("Laporin Aja")) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_placeholder, timelineFragment)
                            .commit();
                }
            }
        }, 1000);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDb = Room.databaseBuilder(this, AppDatabase.class, "report.db")
                .allowMainThreadQueries()
                .build();

        getSession();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void getSession() {
        //ambil data dari session
        sharedpreferences = getSharedPreferences(LoginActivity.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(ID);
        name = getIntent().getStringExtra(NAME);
        email = getIntent().getStringExtra(EMAIL);
        phone = getIntent().getStringExtra(PHONE);
        status = getIntent().getStringExtra(STATUS);
        updated_at = getIntent().getStringExtra(UPDATED_AT);
    }

    public void setProfile(TextView emailProfile, TextView nameProfile, TextView phoneProfile, TextView updatedAtProfile, TextView statusProfile){
        sharedpreferences = getSharedPreferences(LoginActivity.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(ID);
        name = getIntent().getStringExtra(NAME);
        email = getIntent().getStringExtra(EMAIL);
        phone = getIntent().getStringExtra(PHONE);
        status = getIntent().getStringExtra(STATUS);
        updated_at = getIntent().getStringExtra(UPDATED_AT);

        emailProfile.setText(email);
        nameProfile.setText(name);
        phoneProfile.setText(phone);
        updatedAtProfile.setText("Bergabung sejak "+updated_at);
        statusProfile.setText("Status: " +status);
    }

    public void logout() {
        // update login session ke FALSE dan mengosongkan nilai id dan username
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SESSION_STATUS, false);
        editor.putString(ID, null);
        editor.putString(NAME, null);
        editor.putString(EMAIL, null);
        editor.putString(PHONE, null);
        editor.putString(STATUS, null);
        editor.putString(UPDATED_AT, null);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finish();

        Toast.makeText(MainActivity.this, "Berhasil Logout!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void saveReportToDb(List<ReportItem> reportList){

        for(ReportItem m : reportList){
            Report report = new Report();
            report.id = m.getId();
            report.address = m.getAddress();
            report.description = m.getDescription();
            report.lat = m.getLat();
            report.lang = m.getLang();
            report.photo = m.getPhoto();
            report.updated_at = m.getUpdated_at();
            report.status = m.getStatus();
            report.favorite = m.getFavorite();
            report.owner = m.getOwner();
            report.type_report = m.getType_report();

            mDb.reportDao().insertReport(report);
        }
    }

    public void saveFavoriteToDb(List<ReportItem> reportList){

        for(ReportItem m : reportList){
            Favorite report = new Favorite();
            report.id = m.getId();
            report.address = m.getAddress();
            report.description = m.getDescription();
            report.lat = m.getLat();
            report.lang = m.getLang();
            report.photo = m.getPhoto();
            report.updated_at = m.getUpdated_at();
            report.status = m.getStatus();
            report.favorite = m.getFavorite();
            report.owner = m.getOwner();
            report.type_report = m.getType_report();

            mDb.favoriteDao().insertFavorite(report);
        }
    }

    public void getListReport(RecyclerView rvReportList){

        progressBarMain.setVisibility(View.VISIBLE);
        setActionBarTitle("Timeline Report");

        reportListAdapter = new ReportListAdapter();
        reportListAdapter.setListReport(listReport);
        reportListAdapter.setClickHandler(this);

        rvReportList.setAdapter(reportListAdapter);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            rvReportList.setLayoutManager(new LinearLayoutManager(this));
        }else{
            rvReportList.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if(isConnected()) {
            String API_BASE_URL = UtilsApi.BASE_URL_API;

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            Retrofit retrofit = builder.client(httpClient.build()).build();

            client = retrofit.create(BaseApiService.class);

            Call<ReportList> call = client.getReport();

            // Execute the call asynchronously. Get a positive or negative callback.
            call.enqueue(new Callback<ReportList>() {
                @Override
                public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                    Toast.makeText(MainActivity.this, "Load Timeline Success", Toast.LENGTH_SHORT).show();
                    ReportList reportList = response.body();
                    List<ReportItem> listReportItem = reportList.results;

                    saveReportToDb(listReportItem);

                    reportListAdapter.setListReport((ArrayList<ReportItem>) listReportItem);
                    progressBarMain.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<ReportList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Load Timeline Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else{

            List<Report> reportList = mDb.reportDao().getAllReport();
            List<ReportItem> reportItemList = new ArrayList<>();
            for(Report report : reportList){
                ReportItem m = new ReportItem(
                        report.id,
                        report.id_owner,
                        report.address,
                        report.photo,
                        report.description,
                        report.lat,
                        report.lang,
                        report.updated_at,
                        report.status,
                        report.favorite,
                        report.owner,
                        report.type_report
                );
                reportItemList.add(m);
            }

            reportListAdapter.setListReport((ArrayList<ReportItem>) reportItemList);
            progressBarMain.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Kamu tidak terkoneksi internet!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Kami mencoba menampilkan data terakhir ...", Toast.LENGTH_LONG).show();
        }
    }

    public void getListFavoriteReport(RecyclerView rvReportList){

        progressBarMain.setVisibility(View.VISIBLE);

        reportListAdapter = new ReportListAdapter();
        reportListAdapter.setListReport(listReport);
        reportListAdapter.setClickHandler(this);

        rvReportList.setAdapter(reportListAdapter);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            rvReportList.setLayoutManager(new LinearLayoutManager(this));
        }else{
            rvReportList.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if(isConnected()) {
            String API_BASE_URL = UtilsApi.BASE_URL_API;

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            Retrofit retrofit = builder.client(httpClient.build()).build();

            client = retrofit.create(BaseApiService.class);

            Call<ReportList> call = client.getFavoriteReport();

            // Execute the call asynchronously. Get a positive or negative callback.
            call.enqueue(new Callback<ReportList>() {
                @Override
                public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                    Toast.makeText(MainActivity.this, "Load Timeline Success", Toast.LENGTH_SHORT).show();
                    ReportList reportList = response.body();
                    List<ReportItem> listReportItem = reportList.results;

                    saveFavoriteToDb(listReportItem);

                    reportListAdapter.setListReport((ArrayList<ReportItem>) listReportItem);
                    progressBarMain.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<ReportList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Load Timeline Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else{

            List<Report> reportList = mDb.favoriteDao().getAllFavoriteReport();
            List<ReportItem> reportItemList = new ArrayList<>();
            for(Report report : reportList){
                ReportItem m = new ReportItem(
                        report.id,
                        report.id_owner,
                        report.address,
                        report.photo,
                        report.description,
                        report.lat,
                        report.lang,
                        report.updated_at,
                        report.status,
                        report.favorite,
                        report.owner,
                        report.type_report
                );
                reportItemList.add(m);
            }

            reportListAdapter.setListReport((ArrayList<ReportItem>) reportItemList);
            progressBarMain.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Kamu tidak terkoneksi internet!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Kami mencoba menampilkan data terakhir ...", Toast.LENGTH_LONG).show();
        }
    }

    public void reportItemClicked(ReportItem m) {
        Intent detailActivityIntent = new Intent(this, DetailReportActivity.class);
        detailActivityIntent.putExtra("report", m);
        startActivity(detailActivityIntent);
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgView = (ImageView) findViewById(R.id.imgView);
            imgView.setImageBitmap(imageBitmap);
            setPhoto(imageBitmap);
        }
    }

    private void setPhoto(Bitmap bitmapm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] byteArrayImage = baos.toByteArray();
            imagebase64string = Base64.encodeToString(byteArrayImage,Base64.DEFAULT);
            Toast.makeText(this, "Upload Gambar Berhasil!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadToServer(String id_type, String address, String description, Double lat, Double lang){

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        loading = ProgressDialog.show(mContext, null, "Laporan sedangn diproses...", true, false);

        mApiService.reportStore(
                id_type, id, address, imagebase64string, description, lat, lang
        ).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("status").equals("success")){

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_placeholder, timelineFragment)
                                    .commit();

                        } else {
                            // Jika upload gagal
                            String error_message = jsonRESULTS.getString("error");
                            Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext, "Gagal Membuat Laporan! "+response.message().toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(mContext, "Network Connection Problem!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Server Maintenance!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Network Connection Problem!", Toast.LENGTH_SHORT).show();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                loading.dismiss();
            }
        });
    }

}
