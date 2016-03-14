package net.appifiedtech.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.appifiedtech.marshmallowchanges.R;
import net.appifiedtech.model.Contact;

import java.util.ArrayList;

/**
 * Created by Priyabrat on 14-03-2016.
 */
public class ContactAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Contact> contactsList;

    public ContactAdapter(Activity activity,ArrayList<Contact> contactsList){
        this.activity = activity;
        this.contactsList = contactsList;
    }
    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView textViewName;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contactsList.get(position);
        String name = contact.getName();
        String phone = contact.getPhone();
        viewHolder.textViewName.setText(name/*"Name: "+name+" Phone: "+phone*/);
        return convertView;
    }
}
