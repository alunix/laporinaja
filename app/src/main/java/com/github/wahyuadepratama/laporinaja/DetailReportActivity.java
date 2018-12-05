package com.github.wahyuadepratama.laporinaja;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;
import com.github.wahyuadepratama.laporinaja.Model.ReportItem;

public class DetailReportActivity extends AppCompatActivity {

    ImageView photoDetail;
    TextView etType, etLocation, etLat, etLang, etOwner, etUpdatedAt, etStatus, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            ReportItem report = intent.getParcelableExtra("report");
            etType.setText(report.getType_report());
            etLocation.setText(String.valueOf(report.getAddress()));
            etLat.setText(String.valueOf(report.getLat()));
            etLang.setText(String.valueOf(report.getLang()));
            etOwner.setText(String.valueOf(report.getOwner()));
            etUpdatedAt.setText(String.valueOf(report.getUpdated_at()));
            etStatus.setText(String.valueOf(report.getStatus()));
            etDescription.setText(report.getDescription());

            String url = UtilsApi.BASE_URL_IMAGE + report.getPhoto();
            Glide.with(this)
                    .load(url)
                    .into(photoDetail);

        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void shareText(View view){
        String data = etDescription.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data + "More info: laporinaja.com/2192123912/11/2018/");
        if (shareIntent.resolveActivity(getPackageManager()) != null){
            startActivity(shareIntent);
        }
    }

}
