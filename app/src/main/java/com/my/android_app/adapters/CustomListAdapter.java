package com.my.android_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.android_app.R;
import com.my.android_app.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private final Context context;
    private List<User> users;
    private final LayoutInflater inflater;

    public CustomListAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (users.isEmpty()) {
            return view;
        }
        if (users.get(i).getName() == null || users.get(i).getColor() == null ) {
            return view;
        }
        view = inflater.inflate(R.layout.mylist, null);
        TextView name = (TextView) view.findViewById(R.id.title);
        TextView imageView = (TextView) view.findViewById(R.id.symbol);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
        name.setText(users.get(i).getName());
        subtitle.setText(users.get(i).getPrice()+"");
        String[] colorsArray = users.get(i).getColor().split(";");
        imageView.setText("●");
        imageView.setTextColor(Color.rgb(Integer.parseInt(colorsArray[0]), Integer.parseInt(colorsArray[1]), Integer.parseInt(colorsArray[2])));
        return view;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
