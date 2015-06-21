package com.flipbox.cover.coolist.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;
import com.flipbox.cover.coolist.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        db = new SQLiteHandler(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    getFetchData();
                    checkLogin(email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }

    private void getFetchData() {
        pDialog.setMessage("Logging in ...");
        showDialog();

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
                Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(statusReq);

    }

    private void checkLogin(final String email, final String password){
        String tag_string_req = "req_login";
        String URL = AppConfig.URL_LOGIN+"?email="+email+"&password="+password;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("JSON", jsonArray.toString());
                hideDialog();
                if (jsonArray.length() == 1) {
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(0);
                        String jsonEmail = obj.getString("email");
                        if (email.equals(jsonEmail)) {
                            db.addUser(obj.getInt("id"), obj.getInt("company_id"));
                            session.setLogin(true);
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Email or Password not match !!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Email or Password not match !!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialog();
                Log.e(TAG, "Login Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest,tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
