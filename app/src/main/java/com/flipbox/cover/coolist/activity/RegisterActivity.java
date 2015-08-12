package com.flipbox.cover.coolist.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.fragment.RegPassFragment;
import com.flipbox.cover.coolist.fragment.RegisFragment;
import com.flipbox.cover.coolist.fragment.RegisValidFragment;
import com.flipbox.cover.coolist.helper.SQLiteHandler;

public class RegisterActivity extends ActionBarActivity implements RegisFragment.OnFragmentInteractionListener, RegisValidFragment.OnFragmentInteractionListener, RegPassFragment.OnFragmentInteractionListener{

    private android.app.Fragment fragment;
    private Toolbar mToolbar;
    SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new SQLiteHandler(this);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");
        fragment = new RegisFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container_body,fragment);
        ft.addToBackStack(RegisFragment.class.getSimpleName());
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(String email, String name, int role, String url_pict, int id, int company) {
        Fragment validFragment = new RegisValidFragment();
        Bundle args = new Bundle();
        args.putString(RegisValidFragment.ARG_EMAIL,email);
        args.putInt(RegisValidFragment.ARG_ID, id);
        args.putString(RegisValidFragment.ARG_NAME, name);
        args.putInt(RegisValidFragment.ARG_ROLE, role);
        args.putString(RegisValidFragment.ARG_URL_PICT, url_pict);
        args.putInt(RegisValidFragment.ARG_ID_COMPANY,company);
        validFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_body, validFragment);
        transaction.addToBackStack(RegisValidFragment.class.getSimpleName());
        transaction.commit();
    }

    @Override
    public void onFragmentValidInteraction(String email, String name, int id, int company) {
        Fragment passFragment = new RegPassFragment();
        Bundle args = new Bundle();
        args.putString(RegPassFragment.ARG_EMAIL, email);
        args.putString(RegPassFragment.ARG_NAME,name);
        args.putInt(RegPassFragment.ARG_ID, id);
        args.putInt(RegPassFragment.ARG_ID_COMPANY,company);
        passFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_body, passFragment);
        transaction.addToBackStack(RegPassFragment.class.getSimpleName());
        transaction.commit();
    }

    @Override
    public void onFragmentPassInteraction() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
