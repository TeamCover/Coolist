package com.flipbox.cover.coolist.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppController;

public class DetailActivity extends ActionBarActivity {

    public static String DESC_NAME = "Name";
    public static String DESC_IMAGE = "Image";
    public static String DESC_ROLE = "Role";
    public static String DESC_STATUS = "Status";
    public static String DESC_FACEBOOK = "Facebook";
    public static String DESC_TWITTER = "Twitter";
    public static String DESC_LINKEDIN = "Linkedin";
    public static String DESC_EMAIL = "Email";
    public static String DESC_HANDPHONE = "Phone";

    private Toolbar mToolbar;
    TextView name,role,status,twitter,facebook,email,handphone,linkedin;
    NetworkImageView imageView;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextView)findViewById(R.id.Name);
        imageView = (NetworkImageView)findViewById(R.id.thumbnail);
        role = (TextView)findViewById(R.id.role);
        status = (TextView)findViewById(R.id.posisi);
        handphone = (TextView)findViewById(R.id.phoneNumber);
        facebook = (TextView)findViewById(R.id.facebooklink);
        linkedin = (TextView)findViewById(R.id.linkedinlink);
        twitter = (TextView)findViewById(R.id.twitterlink);
        email = (TextView)findViewById(R.id.emaillink);

        Bundle i = getIntent().getExtras();
        name.setText(i.getString(DESC_NAME));
        getSupportActionBar().setTitle(i.getString(DESC_NAME));
        if(imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(i.getString(DESC_IMAGE), imageLoader);
        role.setText(i.getString(DESC_ROLE));
        status.setText(i.getString(DESC_STATUS));
        handphone.setText(i.getString(DESC_HANDPHONE));
        facebook.setText(i.getString(DESC_FACEBOOK));
        linkedin.setText(i.getString(DESC_LINKEDIN));
        twitter.setText(i.getString(DESC_TWITTER));
        email.setText(i.getString(DESC_EMAIL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
