package com.flipbox.cover.coolist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.cover.coolist.R;
import com.flipbox.cover.coolist.app.AppController;
import com.flipbox.cover.coolist.model.Contact;

import java.util.List;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class CustomListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Contact> contactList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Contact> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater== null)
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
            convertView = inflater.inflate(R.layout.contact_item, null);
        if(imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView)convertView.findViewById(R.id.thumbnail);
        TextView nama = (TextView)convertView.findViewById(R.id.nama);
        TextView job = (TextView)convertView.findViewById(R.id.job);

        Contact m = contactList.get(position);

        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        nama.setText(m.getFirstName() + " " + m.getLastName());
        job.setText(m.getPhone());
        return convertView;
    }

    public Contact getDataPosition(int position){
        return contactList.get(position);
    }
}
