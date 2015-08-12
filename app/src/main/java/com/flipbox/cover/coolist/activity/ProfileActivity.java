package com.flipbox.cover.coolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class ProfileActivity extends ActionBarActivity {
    NetworkImageView imageProfile;
    private Toolbar mToolbar;
    TextView nameProfile,jobProfile,location,phoneNumber,facebook,twitter,linkedin;
    SQLiteHandler db;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public ProfileActivity() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_profile);
        db = new SQLiteHandler(this);
        imageProfile = (NetworkImageView)findViewById(R.id.thumbnail);
        nameProfile = (TextView)findViewById(R.id.Name);
        jobProfile = (TextView)findViewById(R.id.role);
        location = (TextView)findViewById(R.id.posisi);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        facebook = (TextView)findViewById(R.id.facebooklink);
        twitter = (TextView)findViewById(R.id.twitterlink);
        linkedin = (TextView)findViewById(R.id.linkedinlink);

        diplayProfile();
        final FloatingActionButton editButton = (FloatingActionButton)findViewById(R.id.multiple_actions);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditActivity.class);
                startActivity(i);
            }
        });
    }

    private void diplayProfile() {
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
                        if(!obj.getString("profile_picture").contains("upload/users/")){
                            imageProfile.setImageUrl(obj.getString("profile_picture"),imageLoader);
                        }
                        else{
                            imageProfile.setImageUrl("http://contact.sakadigital.id/api/"+obj.getString("profile_picture"),imageLoader);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "Connection Time Out!", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(profReq);
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile,container,false);
        imageProfile = (NetworkImageView)rootView.findViewById(R.id.thumbnail);
        nameProfile = (TextView)rootView.findViewById(R.id.Name);
        jobProfile = (TextView)rootView.findViewById(R.id.role);
        location = (TextView)rootView.findViewById(R.id.posisi);
        phoneNumber = (TextView)rootView.findViewById(R.id.phoneNumber);
        facebook = (TextView)rootView.findViewById(R.id.facebooklink);
        twitter = (TextView)rootView.findViewById(R.id.twitterlink);
        linkedin = (TextView)rootView.findViewById(R.id.linkedinlink);
        final FloatingActionButton actionB = (FloatingActionButton)rootView.findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditActivity.class);
                getActivity().startActivity(i);
            }
        });

        final FloatingActionButton actionA = (FloatingActionButton) rootView.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db == null)
                    db = new SQLiteHandler(getActivity());
                ArrayList<String> location = new ArrayList<String>();
                location = db.getAllLocation();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Location");
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.spinner_resource, null);
                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, location);
                spinner.setAdapter(adapter);
                builder.setView(dialogView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String lokasi = spinner.getSelectedItem().toString();
                                final int status_id = db.getStatusByDesc(lokasi);
                                int id = db.getUserID();
                                String URL = AppConfig.URL_REGISTER + String.valueOf(id);
                                StringRequest putReq= new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String s) {

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(getActivity(), "Fail, Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("status_id",String.valueOf(status_id));
                                        params.put("status_description",lokasi);
                                        return params;
                                    }
                                };
                                AppController.getInstance().addToRequestQueue(putReq);
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        return rootView;
    }*/
}
