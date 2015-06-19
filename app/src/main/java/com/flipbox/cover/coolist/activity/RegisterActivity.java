package com.flipbox.cover.coolist.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.fragment.RegPassFragment;
import com.flipbox.cover.coolist.fragment.RegisFragment;
import com.flipbox.cover.coolist.fragment.RegisValidFragment;

public class RegisterActivity extends ActionBarActivity implements RegisFragment.OnFragmentInteractionListener, RegisValidFragment.OnFragmentInteractionListener, RegPassFragment.OnFragmentInteractionListener{

    private android.app.Fragment fragment;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        fragment = new RegisFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container_body,fragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    @Override
    public void onFragmentInteraction(String email, String name, int role, String url_pict) {
        Fragment validFragment = new RegisValidFragment();
        Bundle args = new Bundle();
        args.putString(RegisValidFragment.ARG_EMAIL,email);
        args.putString(RegisValidFragment.ARG_NAME,name);
        args.putInt(RegisValidFragment.ARG_ROLE, role);
        args.putString(RegisValidFragment.ARG_URL_PICT,url_pict);
        validFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_body, validFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentValidInteraction(String email) {
        Fragment passFragment = new RegPassFragment();
        Bundle args = new Bundle();
        args.putString(RegPassFragment.ARG_EMAIL, email);
        passFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_body, passFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentPassInteraction(String uri) {
    }
}
