package com.github.wahyuadepratama.laporinaja;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class NewReportFragment extends Fragment implements LocationListener{

    TextView getLocationBtn;
    TextView lat, lang;
    EditText address, description;
    Spinner type;
    LocationManager locationManager;
    String stringType, stringAddress, stringDescription;
    Double doubleLat, doubleLang;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("New Report");

        final View view = inflater.inflate(R.layout.fragment_new_report, container, false);

        ImageView button = (ImageView) view.findViewById(R.id.imgView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).takePicture();
            }
        });

        String[] values =
                {"Fasilitas", "Lingkungan",};
        Spinner spinner = (Spinner) view.findViewById(R.id.sTypeReportNew);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        getLocationBtn = (TextView) view.findViewById(R.id.getLocation);
        lat = (TextView)view.findViewById(R.id.tvLatNew);
        lang = (TextView)view.findViewById(R.id.tvLangNew);
        address = (EditText)view.findViewById(R.id.etAddressNew);


        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        Button buttonUpload = (Button) view.findViewById(R.id.btnAddNewReport);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = (EditText) view.findViewById(R.id.etAddressNew);
                stringAddress = address.getText().toString();

                description = (EditText) view.findViewById(R.id.etDescriptionNew);
                stringDescription= description.getText().toString();

                type = (Spinner) view.findViewById(R.id.sTypeReportNew);
                stringType = type.getSelectedItem().toString();
                ((MainActivity) getActivity()).uploadToServer(stringType, stringAddress, stringDescription, doubleLat, doubleLang);
            }
        });

        return view;
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            Toast.makeText(this.getContext(), "Kami sudah mendapatkan lokasi kamu!", Toast.LENGTH_SHORT).show();
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        doubleLat = location.getLatitude();
        doubleLang = location.getLongitude();
        lat.setText("Latitude: " + location.getLatitude());
        lang.setText("Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setText(addresses.get(0).getAddressLine(0));
        }catch(Exception e)
        {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this.getContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
