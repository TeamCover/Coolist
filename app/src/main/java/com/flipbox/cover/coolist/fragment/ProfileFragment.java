package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.helper.SQLiteHandler;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class ProfileFragment extends Fragment {
    NetworkImageView imageProfile;
    TextView nameProfile,jobProfile,location,phoneNumber,facebook;
    SQLiteHandler db;
    public ProfileFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        imageProfile = (NetworkImageView)rootView.findViewById(R.id.thumbnail);
        nameProfile = (TextView)rootView.findViewById(R.id.Name);
        jobProfile = (TextView)rootView.findViewById(R.id.role);
        location = (TextView)rootView.findViewById(R.id.posisi);
        phoneNumber = (TextView)rootView.findViewById(R.id.phoneNumber);
        facebook = (TextView)rootView.findViewById(R.id.facebooklink);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
