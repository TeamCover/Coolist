package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;
import com.flipbox.cover.coolist.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class RegPassFragment extends Fragment {

    public static String ARG_EMAIL = "email";
    public static final String ARG_NAME = "name";
    public static final String ARG_ID = "id";
    public static final String ARG_ID_COMPANY= "id_company";
    private Button btnNext;
    private EditText txtPaswsword;

    private String email,userName;
    private int id, id_company;

    ProgressDialog pDialog;
    SessionManager sessionManager;
    SQLiteHandler db;
    private OnFragmentInteractionListener mListener;


    public RegPassFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getFragmentManager().popBackStackImmediate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getActivity());
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_pass, container, false);
        btnNext = (Button)view.findViewById(R.id.btnNext);
        txtPaswsword = (EditText)view.findViewById(R.id.userPass);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = md5(txtPaswsword.getText().toString());
                onButtonPressed(pass,id,id_company);
            }
        });
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            email = getArguments().getString(ARG_EMAIL);
            userName = getArguments().getString(ARG_NAME);
            id = getArguments().getInt(ARG_ID);
            id_company = getArguments().getInt(ARG_ID_COMPANY);
            TextView name = (TextView)getView().findViewById(R.id.Username);
            name.setText("oh, hello there "+userName+"!");
        }
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void onButtonPressed(final String password, final int id, final int id_company) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Checking..");
        pDialog.setCancelable(false);
        pDialog.show();
        String URL = AppConfig.URL_REGISTER+String.valueOf(id);
        StringRequest putRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                db.deleteItem();
                // Get data company
                JsonArrayRequest comReq = new JsonArrayRequest(AppConfig.URL_COMPANY, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("JSON", jsonArray.toString());
                        for(int i=0; i<jsonArray.length(); i++){
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                db.addCompany(obj.getInt("id"),obj.getString("name"),obj.getString("address"),obj.getString("token"));
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
                AppController.getInstance().addToRequestQueue(comReq);

                // Get data Role
                JsonArrayRequest roleReq = new JsonArrayRequest(AppConfig.URL_ROLE, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("JSON", jsonArray.toString());
                        for(int i=0; i<jsonArray.length(); i++){
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                db.addRole(obj.getInt("id"), obj.getString("name"));;
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
                AppController.getInstance().addToRequestQueue(roleReq);

                // Get data Status
                JsonArrayRequest statusReq = new JsonArrayRequest(AppConfig.URL_STATUS, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("JSON", jsonArray.toString());
                        for(int i=0; i<jsonArray.length(); i++){
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                db.addStatus(obj.getInt("id"), obj.getString("name"));
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
                AppController.getInstance().addToRequestQueue(statusReq);
                db.addUser(id,id_company);
                sessionManager.setLogin(true);
                pDialog.dismiss();
                if (mListener != null) {

                    mListener.onFragmentPassInteraction();
                }

                Log.d("Response", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                Log.d("Response", volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", password);
                params.put("registered","1");
                return params;
             }
        };

        AppController.getInstance().addToRequestQueue(putRequest);


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
        // TODO: Update argument type and name
        public void onFragmentPassInteraction();
    }

}
