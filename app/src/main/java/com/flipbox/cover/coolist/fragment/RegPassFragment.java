package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;

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
    private OnFragmentInteractionListener mListener;


    public RegPassFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                String pass = txtPaswsword.getText().toString();
                onButtonPressed(pass,id,id_company);
            }
        });
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
            name.setText(userName);
        }
    }

    public void onButtonPressed(final String password, int id, int id_company) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Checking..");
        pDialog.setCancelable(false);
        pDialog.show();
        String URL = AppConfig.URL_REGISTER+String.valueOf(id);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();
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
                params.put("first_name", "agussss");
                 return params;
             }
        };

        AppController.getInstance().addToRequestQueue(putRequest);

         if (mListener != null) {
            mListener.onFragmentPassInteraction();
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
        // TODO: Update argument type and name
        public void onFragmentPassInteraction();
    }

}
