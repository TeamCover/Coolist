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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RegisFragment extends Fragment {
    ProgressDialog pDialog;

    private OnFragmentInteractionListener mListener;

    public RegisFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regis, container, false);
        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        final EditText txtEmail = (EditText)view.findViewById(R.id.sakaEmail);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                onButtonPressed(email);

            }
        });
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    public void onButtonPressed(final String  email) {
        String tag_string_req = "req_signup";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Checking..");
        pDialog.setCancelable(false);
        pDialog.show();
        String URL = AppConfig.URL_LOGIN+"?email="+email;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("JSON", jsonArray.toString());
                pDialog.dismiss();
                if (jsonArray.length() == 1) {
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(0);
                        String Email = obj.getString("email");
                        int id = obj.getInt("id");
                        String firstName = obj.getString("first_name");
                        String lastName = obj.getString("last_name");
                        String Name = firstName + " " + lastName;
                        int Role = Integer.parseInt(obj.getString("role_id"));
                        String url_pict = obj.getString("profile_picture");
                        int company = obj.getInt("company_id");
                        if (email.equals(Email)) {
                            if (mListener != null) {
                                mListener.onFragmentInteraction(Email,Name,Role,url_pict,id,company);
                            }
                            else{
                                Toast.makeText(getActivity().getBaseContext(), "Email not found!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Email not found!!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Email not found!!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                Log.e("Login ", "Login Error: " + volleyError.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest,tag_string_req);


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
        public void onFragmentInteraction(String email, String name, int role,String url_pict, int id, int company);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getFragmentManager().popBackStackImmediate();
        }

        return super.onOptionsItemSelected(item);
    }
}
