package com.my.android_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.my.android_app.constants.ErrorConstants;
import com.my.android_app.constants.SQLConstants;
import com.my.android_app.entity.Date;
import com.my.android_app.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private final SQLiteDatabase db;

    public DBManager(Context context) {
        DatabaseSQLHelper databaseSQLHelper = new DatabaseSQLHelper(context);
        db = databaseSQLHelper.getWritableDatabase();
    }

    public void insertUser(String name, int price, String color, Context context) {
        if (checkIfUserISPresent(name)) {
            Toast.makeText(context, ErrorConstants.NAME_SAME_ERROR, Toast.LENGTH_SHORT);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(SQLConstants.NAME_FIELD, name);
        values.put(SQLConstants.PRICE_FIELD, price);
        values.put(SQLConstants.COLOR_FIELD, color);
        values.put(SQLConstants.DELETE_FIELD, 0);
        db.insert(SQLConstants.CLIENTS_TABLE_NAME, null, values);
    }

    public long insertNewData(String date, String period) {
        ContentValues values = new ContentValues();
        values.put(SQLConstants.DATE_FIELD, date);
        values.put(SQLConstants.TIME_PERIOD_FIELD, period);
        return db.insert(SQLConstants.DATE_TABLE_NAME, null, values);
    }

    public void makeBooking(int userId, int dateId) {
        ContentValues values = new ContentValues();
        values.put(SQLConstants.CLIENTS_ID_FIELD, userId);
        values.put(SQLConstants.DATE_ID_FIELD, dateId);
        db.insert(SQLConstants.BOOKING_TABLE_NAME, null, values);
    }

    public Date getDateByDateAndId(String dateName, int userId) {
        List<Integer> list = getListOfIdByUser(userId);
        for (int id : list) {
            Date date = getDateById(id);
            if (date.getDate().equals(dateName)) {
                return date;
            }
        }
        return null;
    }

    public void updateDeleteStatus(boolean status, String userName) {
        User user = getUserByName(userName);
        int statusInt;
        if (status) {
            statusInt = 1;
        } else {
            statusInt = 0;
        }
        ContentValues values = new ContentValues();
        values.put(SQLConstants.DELETE_FIELD, statusInt);
        values.put(SQLConstants.NAME_FIELD, "Видалений користувач: " + user.getName());

        String selection = SQLConstants.CLIENTS_ID_FIELD + " = ?";
        String[] selectionArgs = { user.getId()+"" };

        db.update(SQLConstants.CLIENTS_TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void updateTimePeriodOfDate(int id, String newPeriod) {
        ContentValues values = new ContentValues();
        values.put(SQLConstants.TIME_PERIOD_FIELD, newPeriod);

        String selection = SQLConstants.DATE_ID_FIELD + " = ?";
        String[] selectionArgs = { id+"" };

        db.update(SQLConstants.DATE_TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Cursor cursor = db.query(SQLConstants.CLIENTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.CLIENTS_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.NAME_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PRICE_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PAYED_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.COLOR_FIELD)));
            int isDelete = cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DELETE_FIELD));
            user.setDelete(isDelete != 0);
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public void updatePriceUser(String name, int price) {
        ContentValues values = new ContentValues();
        values.put(SQLConstants.PRICE_FIELD, price);

        String selection = SQLConstants.NAME_FIELD + " = ?";
        String[] selectionArgs = { name };

        db.update(SQLConstants.CLIENTS_TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void updatePayedPriceUser(String name, int price) {
        ContentValues values = new ContentValues();
        values.put(SQLConstants.PAYED_FIELD, price);

        String selection = SQLConstants.NAME_FIELD + " = ?";
        String[] selectionArgs = { name };

        db.update(SQLConstants.CLIENTS_TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public List<String> getAllColors() {
        List<String> colors = new ArrayList<>();
        List<User> users = getAllUsers();

        for (User user : users) {
            colors.add(user.getColor());
        }
        return colors;
    }

    public User getUserByName(String name) {
        String selection = SQLConstants.NAME_FIELD + " = ?";
        String[] selectionArgs = { name };

        Cursor cursor = db.query(SQLConstants.CLIENTS_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        User user = null;
        while (cursor.moveToNext()) {
            user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.CLIENTS_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.NAME_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PRICE_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PAYED_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.COLOR_FIELD)));
            int isDelete = cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DELETE_FIELD));
            user.setDelete(isDelete != 0);
        }
        cursor.close();

        return user;
    }

    public List<Integer> getListOfIdByUser(int id) {
        String[] projection = { SQLConstants.DATE_ID_FIELD };

        String selection = SQLConstants.CLIENTS_ID_FIELD + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor cursor = db.query(SQLConstants.BOOKING_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        List<Integer> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DATE_ID_FIELD)));
        }
        cursor.close();

        return list;
    }

    public Date getDateById(int id) {
        String selection = SQLConstants.DATE_ID_FIELD + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor cursor = db.query(SQLConstants.DATE_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Date date = null;
        while (cursor.moveToNext()) {
            date = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DATE_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.DATE_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.TIME_PERIOD_FIELD)));
        }
        cursor.close();

        return date;
    }

    public List<Date> getAllDates() {
        Cursor cursor = db.query(SQLConstants.DATE_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Date> list = null;
        while (cursor.moveToNext()) {
            Date date = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DATE_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.DATE_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.TIME_PERIOD_FIELD)));
            list.add(date);
        }
        cursor.close();

        return list;
    }

    public int getUserIdFromBooking(int id) {
        String[] projection = { SQLConstants.CLIENTS_ID_FIELD };

        String selection = SQLConstants.DATE_ID_FIELD + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor cursor = db.query(SQLConstants.BOOKING_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int result = 0;
        while (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.CLIENTS_ID_FIELD));
        }
        cursor.close();

        return result;
    }

    public User getUserById(int id) {
        String selection = SQLConstants.CLIENTS_ID_FIELD + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor cursor = db.query(SQLConstants.CLIENTS_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        User user = null;
        while (cursor.moveToNext()) {
            user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.CLIENTS_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.NAME_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PRICE_FIELD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.PAYED_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.COLOR_FIELD)));
        }
        cursor.close();

        return user;
    }

    public List<Date> getDateByDate(String date1) {
        String selection = SQLConstants.DATE_FIELD + " = ?";
        String[] selectionArgs = { date1 };

        Cursor cursor = db.query(SQLConstants.DATE_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

       List<Date> dates = new ArrayList<>();
        while (cursor.moveToNext()) {
            Date date = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(SQLConstants.DATE_ID_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.DATE_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SQLConstants.TIME_PERIOD_FIELD)));
            dates.add(date);
        }
        cursor.close();

        return dates;
    }

    private boolean checkIfUserISPresent(String name) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void deleteUserTimeBookingFromSpecificPeriod(String time, int userId) {
        StringBuilder builder;
        List<Integer> list = getListOfIdByUser(userId);

        for (Integer i : list) {
            Date date = getDateById(i);
            String[] timePeriods = date.getTime_periods().split(";");

            if (timePeriods.length == 1 && timePeriods[0].equals(time)) {
                deleteBooking(userId, date.getId());
                deleteDate(date.getId());
                return;
            }
            builder = new StringBuilder();
            for (String name: timePeriods) {
                if (name.equals(time)) {
                    continue;
                }
                builder.append(name);
                builder.append(";");
            }
            updateTimePeriodOfDate(date.getId(), builder.toString());
        }
    }

    public void deleteBooking(int userId, int dateId) {
        String selection = SQLConstants.CLIENTS_ID_FIELD + " = ? AND " + SQLConstants.DATE_ID_FIELD + " = ?";
        String[] selectionArgs = { userId+"", dateId+""};

        db.delete(SQLConstants.BOOKING_TABLE_NAME, selection, selectionArgs);
    }

    public void deleteDate(int dateId) {
        String selection = SQLConstants.DATE_ID_FIELD + " = ?";
        String[] selectionArgs = { dateId+""};

        db.delete(SQLConstants.DATE_TABLE_NAME, selection, selectionArgs);
    }
}
