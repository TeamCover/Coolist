package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppController;

public class RegisValidFragment extends Fragment {

    public static final String ARG_EMAIL = "email";
    public static final String ARG_NAME = "name";
    public static final String ARG_ROLE = "role";
    public static final String ARG_URL_PICT = "url_pict";



    private String email,url_picture,name;
    private int role;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private OnFragmentInteractionListener mListener;


    public RegisValidFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regis_valid, container, false);
        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(email);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arg = getArguments();
        if(arg!=null){
            email = arg.getString(ARG_EMAIL);
            name = arg.getString(ARG_NAME);
            role = arg.getInt(ARG_ROLE);
            url_picture = arg.getString(ARG_URL_PICT);
            TextView uName = (TextView)getView().findViewById(R.id.Name);
            NetworkImageView thumbnail = (NetworkImageView)getView().findViewById(R.id.thumbnail);
            TextView uRole = (TextView)getView().findViewById(R.id.role);
            if(imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            thumbnail.setImageUrl(url_picture,imageLoader);
            uName.setText(name);
            uRole.setText(String.valueOf(role));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {

        if (mListener != null) {
            mListener.onFragmentValidInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentValidInteraction(String uri);
    }

}
