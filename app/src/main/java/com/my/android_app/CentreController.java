package com.my.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.my.android_app.adapters.CustomListAdapter;
import com.my.android_app.adapters.CustomTimeAdapter;
import com.my.android_app.constants.ErrorConstants;
import com.my.android_app.database.DBManager;
import com.my.android_app.entity.Date;
import com.my.android_app.entity.User;
import com.my.android_app.util.Util;

import java.util.ArrayList;
import java.util.List;

public class CentreController extends AppCompatActivity {

    private DBManager dbManager;
    private CustomListAdapter adapter;
    private ArrayAdapter<String> arrayAdapter;

    //-----Clients-----

    private ListView customListView;
    private EditText name;
    private EditText price;
    private TextView moneyView;

    //-----Statistic-----

    private TextView titleStat;
    private TextView symbolStat;
    private EditText payedAmount;
    private TextView priceOneLesson;
    private TextView debt;
    private TextView payedAmountAllTime;
    private TextView lessonsAmount;

    //-----Calendar-----

    private CalendarView calendarView;
    private TextView dataText;
    private Spinner spinner;
    private Spinner spinnerNames;
    private ListView timeView;
    private TextView allMoney;

    public CentreController() {

    }

    private void initClients() {
        customListView = (ListView) findViewById(R.id.ListLayout);
        adapter = new CustomListAdapter(getApplicationContext(), new ArrayList<>());
        name = (EditText) findViewById(R.id.ClientsName);
        price = (EditText) findViewById(R.id.editTextTextPersonName2);
        moneyView = (TextView) findViewById(R.id.ArrearsPrice);
    }

    private void initStatistic() {
        titleStat = (TextView) findViewById(R.id.titleStat);
        symbolStat = (TextView) findViewById(R.id.symbolStat);
        payedAmount = (EditText) findViewById(R.id.editTextTextPersonName2);
        priceOneLesson = (TextView) findViewById(R.id.PriceOnLesson);
        debt = (TextView) findViewById(R.id.DeptInt);
        payedAmountAllTime = (TextView) findViewById(R.id.SumAllTime);
        lessonsAmount = (TextView) findViewById(R.id.CountLessons);
    }

    private void initCalendar() {
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        allMoney = (TextView) findViewById(R.id.AllTimeMoney);
    }

    private void initTimeSpace() {
        dataText = (TextView) findViewById(R.id.Date);
        spinner = (Spinner) findViewById(R.id.spinner);
        timeView = (ListView) findViewById(R.id.TimeView);
        spinnerNames = (Spinner) findViewById(R.id.spinner1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getApplicationContext());
        setContentView(R.layout.calendar_main);
        initCalendar();

        updateCalendar();
    }

    private void updateAdapterContent() {
        List<User> users = dbManager.getAllUsers();

        adapter.setUsers(Util.getCorrectUserList(users));

        customListView.setAdapter(adapter);
        customListView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView name = (TextView) view.findViewById(R.id.title);
            TextView imageView = (TextView) view.findViewById(R.id.symbol);

            setContentView(R.layout.statistics_main);

            initStatistic();

            updateStatistic(name.getText().toString(), imageView);
        });

        moneyView.setText(Util.countTotalForCurrentUsersMoney(users));
        clearFields();
    }

    private void updateStatistic(String name, TextView imageView) {
        titleStat.setText(name);
        symbolStat.setText(imageView.getText());
        symbolStat.setTextColor(imageView.getTextColors());

        User user = dbManager.getUserByName(name);

        priceOneLesson.setText(user.getPrice() + "");
        payedAmountAllTime.setText(user.getPayedPrice() + "");

        if (payedAmount.getText().toString().isEmpty()) {
            updatePayedPrice(user.getPayedPrice(), name);
        } else {
            int newPrice = Integer.parseInt(payedAmount.getText().toString());
            updatePayedPrice(user.getPayedPrice() + newPrice, name);
        }
    }

    private void updatePayedPrice(int price, String name) {
        dbManager.updatePayedPriceUser(name, price);
        payedAmountAllTime.setText(price + "");
        payedAmount.setText("");

        User user = dbManager.getUserByName(name);
        int lessonsAmountValue = Util.countAmountOfLessons(dbManager, user.getId());
        lessonsAmount.setText(lessonsAmountValue +"");
        int debtPrice = (user.getPayedPrice() - (user.getPrice() * lessonsAmountValue));
        if (debtPrice > 0) {
            debt.setText("+" + debtPrice);
        } else {
            debt.setText(debtPrice + "");
        }

    }

    private void updateCalendar() {
        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            setContentView(R.layout.time_space_main);
            updateTimeSpace(day, month, year);
        });
        allMoney.setText(Util.countTotalMoney(dbManager));
    }

    private void updateTimeSpace(int day, int month, int year) {
        String dateParam = day + "." + month + "." + year;
        initTimeSpace();

        dataText.setText(dateParam);
        CustomTimeAdapter customTimeAdapter = new CustomTimeAdapter(getApplicationContext(), dbManager.getDateByDate(dateParam), dbManager);
        arrayAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                customTimeAdapter.getList());
         spinner.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Util.getUsersNames(Util.getCorrectUserList(dbManager.getAllUsers())));
        spinnerNames.setAdapter(arrayAdapter);
        timeView.setAdapter(customTimeAdapter);

    }

    private void clearFields() {
        name.setText("");
        price.setText("");
    }

    public void calendarButton(View view) {
        setContentView(R.layout.calendar_main);

        initCalendar();

        updateCalendar();
    }

    public void clientsButton(View view) {
        setContentView(R.layout.clients_main);

        initClients();

        updateAdapterContent();
    }

    public void exitButton(View view) {
        finish();
        System.exit(0);
    }

    public void addUserButton(View view) {
        String userName = name.getText().toString();
        if (userName.length() == 0) {
            Toast.makeText(this, ErrorConstants.USER_NAME_FIELD_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        if (price.getText().toString().isEmpty()) {
            Toast.makeText(this, ErrorConstants.PAYED_PRICE_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> colors = dbManager.getAllColors();
        String color = Util.generateColor();
        while (colors.contains(color)) {
            color = Util.generateColor();
        }

        int priceTemp = Util.getCorrectPriceIntValue(price);
        if (priceTemp < 0) {
            Toast.makeText(this, ErrorConstants.ERROR_PRICE_LOWER_THEN_ZERO, Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.insertUser(name.getText().toString(), priceTemp, color, getApplicationContext());
        updateAdapterContent();
    }

    public void deleteUserButton(View view) {
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, ErrorConstants.USER_NAME_FIELD_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.updateDeleteStatus(true, name.getText().toString());
        updateAdapterContent();
    }

    public void updateUserButton(View view) {
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, ErrorConstants.USER_NAME_FIELD_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        int priceTemp = Util.getCorrectPriceIntValue(price);
        if (priceTemp < 0) {
            Toast.makeText(this, ErrorConstants.ERROR_PRICE_LOWER_THEN_ZERO, Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.updatePriceUser(name.getText().toString(), priceTemp);
        updateAdapterContent();
    }

    public void addUserOnTimePeriod(View view) {
        User user = dbManager.getUserByName(spinnerNames.getSelectedItem().toString());

        Date date = dbManager.getDateByDateAndId(dataText.getText().toString(), user.getId());
        if (date == null) {
            dbManager.makeBooking(user.getId(), (int) dbManager.insertNewData(dataText.getText().toString(), spinner.getSelectedItem().toString()));
        } else {
            String newPeriod = date.getTime_periods() + ";" + spinner.getSelectedItem().toString();
            dbManager.updateTimePeriodOfDate(date.getId(), newPeriod);
        }

        String[] time = dataText.getText().toString().split("\\.");
        updateTimeSpace(Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]));
    }

    public void updatePayedPriceButton(View view) {
        if (payedAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, ErrorConstants.PAYED_PRICE_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbManager.getUserByName(titleStat.getText().toString());
        updatePayedPrice(Integer.parseInt(payedAmount.getText().toString()) + user.getPayedPrice(), user.getName());
        payedAmount.setText("");
    }

    public void delete(View view) {
        User user = dbManager.getUserByName(spinnerNames.getSelectedItem().toString());
        dbManager.deleteUserTimeBookingFromSpecificPeriod(spinner.getSelectedItem().toString(), user.getId());

        String[] time = dataText.getText().toString().split("\\.");
        updateTimeSpace(Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]));
    }
}