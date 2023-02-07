package com.my.android_app.util;

import android.widget.EditText;

import com.my.android_app.database.DBManager;
import com.my.android_app.entity.Date;
import com.my.android_app.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    private static final Random random = new Random();

    private Util() {}

    public static String generateColor() {
        String color = random.nextInt(256) +
                ";" +
                random.nextInt(256) +
                ";" +
                random.nextInt(256);
        return color;
    }

    public static String countTotalForCurrentUsersMoney(List<User> users) {
        int money = 0;
        for (User user: users) {
            if (!user.isDelete()) {
                money += user.getPayedPrice();
            }
        }
        return money + "";
    }

    public static String countTotalMoney(DBManager dbManager) {
        List<User> users = dbManager.getAllUsers();

        if (users == null) {
            return "0";
        }

        int money = 0;
        for (User user: users) {
            money += user.getPayedPrice();
        }
        return money + "";
    }

    public static int getCorrectPriceIntValue(EditText price) {
        String priceInt = price.getText().toString();

        if (priceInt.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(priceInt);
        } catch (Exception e) {
            return -1;
        }
    }

    public static List<String> getUsersNames(List<User> users) {
        List<String> list = new ArrayList<>();
        for (User user : users) {
            list.add(user.getName());
        }

        return list;
    }

    public static int countAmountOfLessons(DBManager dbManager, int userid) {
        List<Integer> list = dbManager.getListOfIdByUser(userid);
        int counter = 0;
        for (int id : list) {
            Date date = dbManager.getDateById(id);
            String[] lessons = date.getTime_periods().split(";");
            counter += lessons.length;
        }
        return counter;
    }

    public static List<User> getCorrectUserList(List<User> list) {
        List<User> tempList = new ArrayList<>();
        for (User user : list) {
            if (!user.isDelete()) {
                tempList.add(user);
            }
        }
        return tempList;
    }
}
