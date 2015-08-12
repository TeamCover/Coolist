package com.flipbox.cover.coolist.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordActivity extends ActionBarActivity {

    EditText cPass, nPass,oPass;
    private Toolbar mToolbar;
    TextView result;
    Button next;
    ProgressDialog pDialog;
    SQLiteHandler db;

    public PasswordActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_password);
        cPass = (EditText)findViewById(R.id.cPassword);
        nPass = (EditText)findViewById(R.id.nPassword);
        oPass = (EditText)findViewById(R.id.oPassword);
        result = (TextView)findViewById(R.id.result);
        next = (Button)findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cPass.getText().toString().equals("") || nPass.getText().toString().equals("") || oPass.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Please enter the credential!!", Toast.LENGTH_SHORT).show();
                }
                if (cPass.getText().toString().equals(nPass.getText().toString())) {
                    updatePassword(md5(nPass.getText().toString()));
                    cPass.setText(null);
                    nPass.setText(null);
                    oPass.setText(null);
                } else {
                    result.setText("Password not match!!");
                }
            }
        });

    }

    private void updatePassword(final String password){
        db = new SQLiteHandler(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Checking..");
        pDialog.setCancelable(false);
        pDialog.show();
        String URL = AppConfig.URL_REGISTER+String.valueOf(db.getUserID());
        Log.d("Result", URL);
        StringRequest putRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("Result", s);
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

    private static final String md5(final String s) {
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


}
