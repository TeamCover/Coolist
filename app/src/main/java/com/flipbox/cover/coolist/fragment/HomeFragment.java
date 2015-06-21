package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.adapter.CustomListAdapter;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;
import com.flipbox.cover.coolist.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
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
                Toast.makeText(getActivity().getApplicationContext(), contact.getFirstName(), Toast.LENGTH_LONG).show();
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
