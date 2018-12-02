package com.github.wahyuadepratama.laporinaja;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.wahyuadepratama.laporinaja.Adapter.ReportListAdapter;
import com.github.wahyuadepratama.laporinaja.ApiHelper.BaseApiService;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;
import com.github.wahyuadepratama.laporinaja.Model.ReportList;

import static com.github.wahyuadepratama.laporinaja.LoginActivity.ID;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.NAME;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.EMAIL;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.PHONE;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.STATUS;
import static com.github.wahyuadepratama.laporinaja.LoginActivity.UPDATED_AT;

public class MainActivity extends AppCompatActivity implements ReportListAdapter.OnReportItemClicked{

    private ProfileFragment profileFragment = new ProfileFragment();
    private TimelineFragment timelineFragment = new TimelineFragment();
    private NewReportFragment newReportFragment = new NewReportFragment();

    public SharedPreferences sharedpreferences;
    public String id, name, email, phone, status, updated_at;

    private static final String TAG = "MainActivity";
    ArrayList<ReportItem> listReport = new ArrayList<>();
    ReportListAdapter reportListAdapter;
    RecyclerView rvReportList;
    ProgressBar progressBar;
    BaseApiService client;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_placeholder, profileFragment)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

    public void logout() {
        // update login session ke FALSE dan mengosongkan nilai id dan username
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginActivity.SESSION_STATUS, false);
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

    public void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
    }

    public void setInitRicyclerView(){
        reportListAdapter = new ReportListAdapter();
        reportListAdapter.setListReport(listReport);
        reportListAdapter.setClickHandler(this);

        rvReportList = findViewById(R.id.rv_report_list);
        rvReportList.setAdapter(reportListAdapter);
    }

    public void getListReport(){

//        progressBar.setVisibility(View.VISIBLE);
//        rvReportList.setVisibility(View.INVISIBLE);

        String API_BASE_URL = UtilsApi.BASE_URL_API;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();

        client =  retrofit.create(BaseApiService.class);

        Call<ReportList> call = client.getReport();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                Toast.makeText(MainActivity.this, "Load Data Succesfully", Toast.LENGTH_SHORT).show();
                ReportList reportList = response.body();
                List<ReportItem> listReportItem = reportList.results;
                reportListAdapter.setListReport((ArrayList<ReportItem>) listReportItem);

//                progressBar.setVisibility(View.INVISIBLE);
//                rvReportList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Load Data Failed", Toast.LENGTH_SHORT).show();

//                progressBar.setVisibility(View.INVISIBLE);
//                rvReportList.setVisibility(View.VISIBLE);
            }
        });
    }

    public void reportItemClicked(ReportItem m) {
        Toast.makeText(this, "Go to detail report", Toast.LENGTH_SHORT).show();
//        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
//        detailActivityIntent.putExtra("report", m);
//        startActivity(detailActivityIntent);
    }

}
