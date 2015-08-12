package com.flipbox.cover.coolist.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.adapter.CustomListAdapter;
import com.flipbox.cover.coolist.app.AppConfig;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.helper.SQLiteHandler;
import com.flipbox.cover.coolist.helper.SessionManager;
import com.flipbox.cover.coolist.model.Contact;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity{

    private Toolbar mToolbar;
    private List<Contact> contactList;
    private ListView listView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    SessionManager sessionManager;
    SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        db = new SQLiteHandler(this);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        displayList("");
        final FloatingActionButton addAction = (FloatingActionButton)findViewById(R.id.multiple_actions);
        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db == null)
                    db = new SQLiteHandler(getApplicationContext());
                ArrayList<String> location = new ArrayList<String>();
                location = db.getAllLocation();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Location");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.spinner_resource, null);
                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, location);
                spinner.setAdapter(adapter);
                builder.setView(dialogView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String lokasi = spinner.getSelectedItem().toString();
                                final int status_id = db.getStatusByDesc(lokasi);
                                int id = db.getUserID();
                                String URL = AppConfig.URL_REGISTER + String.valueOf(id);
                                StringRequest putReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String s) {
                                        Toast.makeText(getApplication(), "Location Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(getApplication(), "Fail, Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("status_id", String.valueOf(status_id));
                                        params.put("status_description", lokasi);
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
    }

    private void displayList(final String Query) {
        if(db==null)
            db = new SQLiteHandler(this);
        contactList = new ArrayList<Contact>();
        listView = (ListView)findViewById(R.id.list);
        adapter = new CustomListAdapter(this, contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = adapter.getDataPosition(position);
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra(DetailActivity.DESC_NAME, contact.getFirstName() + " " + contact.getLastName());
                i.putExtra(DetailActivity.DESC_FACEBOOK, contact.getFacebook());
                i.putExtra(DetailActivity.DESC_EMAIL, contact.getEmail());
                i.putExtra(DetailActivity.DESC_HANDPHONE, contact.getPhone());
                i.putExtra(DetailActivity.DESC_ROLE, contact.getRole_id());
                i.putExtra(DetailActivity.DESC_LINKEDIN, contact.getLinkedin());
                i.putExtra(DetailActivity.DESC_TWITTER, contact.getTwitter());
                i.putExtra(DetailActivity.DESC_STATUS, contact.getStatus_id());
                i.putExtra(DetailActivity.DESC_IMAGE, contact.getThumbnailUrl());
                startActivity(i);
            }
        });

        String URL = AppConfig.URL_CONTACT+String.valueOf(db.getUserCompany());
        JsonArrayRequest contactReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("JSON", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                if(obj.getString("first_name").contains(Query) || obj.getString("last_name").contains(Query) && obj.getInt("id")!=db.getUserCompany()){
                                    Contact contact = new Contact();
                                    contact.setFirstName(obj.getString("first_name"));
                                    contact.setLastName(obj.getString("last_name"));
                                    contact.setPhone(obj.getString("phone"));
                                    if(!obj.getString("profile_picture").contains("upload/users/")){
                                        contact.setThumbnailUrl(obj.getString("profile_picture"));
                                    }
                                    else{
                                        contact.setThumbnailUrl("http://contact.sakadigital.id/api/"+obj.getString("profile_picture"));
                                    }
                                    contact.setFacebook(obj.getString("facebook"));
                                    contact.setLinkedin(obj.getString("linkedin"));
                                    contact.setTwitter(obj.getString("twitter"));
                                    contact.setEmail(obj.getString("email"));
                                    contact.setRole_id(db.getRoleByKey(obj.getInt("role_id")));
                                    contact.setCompany_id(db.getCompanyByKey(obj.getInt("company_id")));
                                    contact.setStatus_id(db.getStatusByKey(obj.getInt("status_id")));
                                    contactList.add(contact);
                                }
                                else if(Query.equals("") && obj.getInt("id")!=db.getUserCompany()){
                                    Contact contact = new Contact();
                                    contact.setFirstName(obj.getString("first_name"));
                                    contact.setLastName(obj.getString("last_name"));
                                    contact.setPhone(obj.getString("phone"));
                                    if(!obj.getString("profile_picture").contains("upload/users/")){
                                        contact.setThumbnailUrl(obj.getString("profile_picture"));
                                    }
                                    else{
                                        contact.setThumbnailUrl("http://contact.sakadigital.id/api/"+obj.getString("profile_picture"));
                                    }
                                    contact.setFacebook(obj.getString("facebook"));
                                    contact.setLinkedin(obj.getString("linkedin"));
                                    contact.setTwitter(obj.getString("twitter"));
                                    contact.setEmail(obj.getString("email"));
                                    contact.setRole_id(db.getRoleByKey(obj.getInt("role_id")));
                                    contact.setCompany_id(db.getCompanyByKey(obj.getInt("company_id")));
                                    contact.setStatus_id(db.getStatusByKey(obj.getInt("status_id")));
                                    contactList.add(contact);
                                }
                                else{
                                    Toast.makeText(getBaseContext(),"Not Found!", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "Connection interrupted!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(contactReq);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterdata(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterdata(s);
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filterdata("");
                return false;
            }
        });
        return true;
    }

    private void filterdata(String s) {
        displayList(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id==R.id.logout){
            sessionManager.setLogin(false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.editProfile){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        }else if (id==R.id.editPassword){
            Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
