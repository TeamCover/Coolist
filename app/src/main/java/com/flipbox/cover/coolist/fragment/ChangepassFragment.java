package com.flipbox.cover.coolist.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangepassFragment extends Fragment {

    EditText cPass, nPass,oPass;
    TextView result;
    Button next;
    ProgressDialog pDialog;
    SQLiteHandler db;

    public ChangepassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_changepass, container, false);
        cPass = (EditText)view.findViewById(R.id.cPassword);
        nPass = (EditText)view.findViewById(R.id.nPassword);
        oPass = (EditText)view.findViewById(R.id.oPassword);
        result = (TextView)view.findViewById(R.id.result);
        next = (Button)view.findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cPass.getText().toString().equals("") || nPass.getText().toString().equals("") || oPass.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplication(),"Please enter the credential!!", Toast.LENGTH_SHORT).show();
                }
                if(cPass.getText().toString().equals(nPass.getText().toString())){
                    updatePassword(nPass.getText().toString());
                }else {
                    result.setText("Password not match!!");
                }
            }
        });


        return view;
    }

    private void updatePassword(final String password){
        db = new SQLiteHandler(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Checking..");
        pDialog.setCancelable(false);
        pDialog.show();
        String URL = AppConfig.URL_REGISTER+String.valueOf(db.getUserID());
        StringRequest putRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();
                result.setText("Password has changed!");
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
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(putRequest);

    }


}
