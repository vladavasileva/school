package com.my.android_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.android_app.R;
import com.my.android_app.database.DBManager;
import com.my.android_app.entity.Date;
import com.my.android_app.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CustomTimeAdapter extends BaseAdapter {
    private final List<String> list;
    private final List<Date> dates;
    private final DBManager dbManager;
    private final LayoutInflater inflater;

    public CustomTimeAdapter(Context context, List<Date> dates, DBManager dbManager) {
        list = new ArrayList<>();
        int startPoint = 8;
        for (int i = 8; i < 19; i++) {
            list.add(startPoint + ":00");
            startPoint++;
        }
        this.dates = dates;
        this.dbManager = dbManager;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
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
        view = inflater.inflate(R.layout.mylist_clients_timespace, null);
        TextView time = (TextView) view.findViewById(R.id.Time);
        TextView symbol = (TextView) view.findViewById(R.id.symbol);
        TextView names = (TextView) view.findViewById(R.id.name);

        time.setText(list.get(i));

        for (Date date : dates) {
            if (date != null) {
                String[] periods = date.getTime_periods().split(";");

                for (String period : periods) {
                    if (list.get(i).equals(period)) {
                        User user = dbManager.getUserById(dbManager.getUserIdFromBooking(date.getId()));
                        names.setText(user.getName());
                        String[] colorsArray = user.getColor().split(";");
                        symbol.setText("●");
                        symbol.setTextColor(Color.rgb(Integer.parseInt(colorsArray[0]), Integer.parseInt(colorsArray[1]), Integer.parseInt(colorsArray[2])));
                    }
                }
            } else {
                names.setText(" ");
            }
        }

        return view;
    }

    public List<String> getList() {
        return list;
    }
}
