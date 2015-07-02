package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.activity.DetailActivity;
import com.flipbox.cover.coolist.adapter.CustomListAdapter;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;
import com.flipbox.cover.coolist.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class HomeFragment extends Fragment {
    private List<Contact> contactList = new ArrayList<Contact>();
    private ListView listView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    SQLiteHandler db;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        rootView.findViewById(R.id.set_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                StringRequest putReq= new StringRequest(Method.POST,URL, new Response.Listener<String>() {

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
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteHandler(getActivity());
        listView = (ListView)getView().findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = adapter.getDataPosition(position);
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(DetailActivity.DESC_NAME,contact.getFirstName()+" "+contact.getLastName());
                i.putExtra(DetailActivity.DESC_FACEBOOK, contact.getFacebook());
                i.putExtra(DetailActivity.DESC_EMAIL, contact.getEmail());
                i.putExtra(DetailActivity.DESC_HANDPHONE, contact.getPhone());
                i.putExtra(DetailActivity.DESC_ROLE, contact.getRole_id());
                i.putExtra(DetailActivity.DESC_LINKEDIN, contact.getLinkedin());
                i.putExtra(DetailActivity.DESC_TWITTER, contact.getTwitter());
                i.putExtra(DetailActivity.DESC_STATUS, contact.getStatus_id());
                i.putExtra(DetailActivity.DESC_IMAGE, contact.getThumbnailUrl());
                getActivity().startActivity(i);
            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading..");
        pDialog.show();
        String URL = AppConfig.URL_CONTACT+String.valueOf(db.getUserCompany());
        JsonArrayRequest contactReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("JSON",jsonArray.toString());
                        hidePDialog();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                Contact contact = new Contact();
                                contact.setFirstName(obj.getString("first_name"));
                                contact.setLastName(obj.getString("last_name"));
                                contact.setPhone(obj.getString("phone"));
                                contact.setThumbnailUrl(obj.getString("profile_picture"));
                                contact.setFacebook(obj.getString("facebook"));
                                contact.setLinkedin(obj.getString("linkedin"));
                                contact.setTwitter(obj.getString("twitter"));
                                contact.setEmail(obj.getString("email"));
                                contact.setRole_id(db.getRoleByKey(obj.getInt("role_id")));
                                contact.setCompany_id(db.getCompanyByKey(obj.getInt("company_id")));
                                contact.setStatus_id(db.getStatusByKey(obj.getInt("status_id")));
                                contactList.add(contact);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hidePDialog();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(contactReq);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private  void hidePDialog(){
        if(pDialog != null){
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
