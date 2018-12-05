package com.github.wahyuadepratama.laporinaja;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    public TextView emailProfile, nameProfile, phoneProfile, updatedAtProfile, statusProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Profile");

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            View view = inflater.inflate(R.layout.landscape_fragment_profile, container, false);

            emailProfile = view.findViewById(R.id.etEmailProfileLandscape);
            nameProfile = view.findViewById(R.id.etNameProfileLandscape);
            phoneProfile = view.findViewById(R.id.etPhoneProfileLandscape);
            updatedAtProfile = view.findViewById(R.id.etUpdatedAtProfileLandscape);
            statusProfile = view.findViewById(R.id.etStatusProfileLandscape);

            ((MainActivity) getActivity()).setProfile(emailProfile, nameProfile, phoneProfile, updatedAtProfile, statusProfile);

            Button button = (Button) view.findViewById(R.id.logoutLandscape);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((MainActivity) getActivity()).logout();
                }
            });
            return view;
        }else{
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

            emailProfile = view.findViewById(R.id.etEmailProfile);
            nameProfile = view.findViewById(R.id.etNameProfile);
            phoneProfile = view.findViewById(R.id.etPhoneProfile);
            updatedAtProfile = view.findViewById(R.id.etUpdatedAtProfile);
            statusProfile = view.findViewById(R.id.etStatusProfile);

            ((MainActivity) getActivity()).setProfile(emailProfile, nameProfile, phoneProfile, updatedAtProfile, statusProfile);

            Button button = (Button) view.findViewById(R.id.logout);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((MainActivity) getActivity()).logout();
                }
            });
            return view;
        }
    }

}
