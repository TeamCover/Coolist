package com.flipbox.cover.coolist.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.flipbox.cover.coolist.helper.AndroidMultiPartEntity;
import com.flipbox.cover.coolist.helper.SQLiteHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends ActionBarActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Toolbar mToolbar;
    private Button submit;
    private ImageView viewImage;
    private TextView text;
    private EditText nProf,pProf,fProf,tProf,lProf;
    private Spinner jProf;
    private String pictPath;
    SQLiteHandler db;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        pDialog = new ProgressDialog(this);
        db = new SQLiteHandler(this);
        nProf = (EditText)findViewById(R.id.Name);
        pProf = (EditText)findViewById(R.id.phoneNumber);
        fProf = (EditText)findViewById(R.id.facebooklink);
        tProf = (EditText)findViewById(R.id.twitterlink);
        lProf = (EditText)findViewById(R.id.linkedinlink);
        jProf = (Spinner)findViewById(R.id.role);

        loadData();
        ArrayList<String> role = new ArrayList<String>();
        role = db.getAllRole();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, role);
        jProf.setAdapter(adapter);


        viewImage = (ImageView)findViewById(R.id.iProfile);
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        text = (TextView)findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        submit = (Button)findViewById(R.id.bConfirm);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.setMessage("Submit data...");
                pDialog.setCancelable(false);
                pDialog.show();
                new UploadImageToServer().execute();
                uploadData();
            }
        });
    }

    private void uploadData() {
        final String role = jProf.getSelectedItem().toString();
        final int role_id = db.getRoleByDesc(role);
        final String fullName = nProf.getText().toString();
        final String[] splitName = fullName.split("\\s+");
        final String facebook = fProf.getText().toString();
        final String twitter = tProf.getText().toString();
        final String likedin = lProf.getText().toString();
        final String phone = pProf.getText().toString();
        String URL = AppConfig.URL_REGISTER + String.valueOf(db.getUserID());
        StringRequest putReq= new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail, Try again later", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("role_id",String.valueOf(role_id));
                params.put("first_name",splitName[0]);
                params.put("last_name",splitName[1]);
                params.put("facebook",facebook);
                params.put("twitter",twitter);
                params.put("linkedin",likedin);
                params.put("phone",phone);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(putReq);
    }

    private void loadData() {
        String URL = AppConfig.URL_PROFILE+String.valueOf(db.getUserID());
        JsonArrayRequest profReq = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        nProf.setText(obj.getString("first_name")+ " "+obj.get("last_name"));
                        pProf.setText(obj.getString("phone"));
                        fProf.setText(obj.getString("facebook"));
                        lProf.setText(obj.getString("linkedin"));
                        tProf.setText(obj.getString("twitter"));
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
        AppController.getInstance().addToRequestQueue(profReq);
    }


    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filepathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
            pictPath = cursor.getString(columnIndex);
            viewImage.setImageBitmap(BitmapFactory.decodeFile(pictPath));
        }

    }

    private class UploadImageToServer extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://contact.sakadigital.id/api/users/11");
            try {
                File sourceFile = new File(pictPath);
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {

                    }
                });
                entity.addPart("profile_picture", new FileBody(sourceFile, "image/jpg"));
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
