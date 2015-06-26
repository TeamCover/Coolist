package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class ProfileFragment extends Fragment {
    NetworkImageView imageProfile;
    TextView nameProfile,jobProfile,location,phoneNumber,facebook,twitter,linkedin;
    SQLiteHandler db;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
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
        twitter = (TextView)rootView.findViewById(R.id.twitterlink);
        linkedin = (TextView)rootView.findViewById(R.id.linkedinlink);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String URL = AppConfig.URL_PROFILE+String.valueOf(db.getUserID());
        JsonArrayRequest profReq = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("JSON", jsonArray.toString());
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        nameProfile.setText(obj.getString("first_name")+ " "+obj.get("last_name"));
                        phoneNumber.setText(obj.getString("phone"));
                        facebook.setText(obj.getString("facebook"));
                        location.setText(db.getStatusByKey(obj.getInt("status_id")));
                        jobProfile.setText(db.getRoleByKey(obj.getInt("role_id")));
                        linkedin.setText(obj.getString("linkedin"));
                        twitter.setText(obj.getString("twitter"));
                        if(imageLoader == null)
                            imageLoader = AppController.getInstance().getImageLoader();
                        imageProfile.setImageUrl(obj.getString("profile_picture"),imageLoader);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(profReq);
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
