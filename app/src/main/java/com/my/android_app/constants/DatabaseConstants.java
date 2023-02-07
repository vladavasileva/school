package com.my.android_app.constants;

import static com.my.android_app.constants.SQLConstants.*;

public class DatabaseConstants {
    private DatabaseConstants() {}

    public static final String DATABASE_NAME = "calendarBooking";
    public static final int DATABASE_VERSION = 12;

    public static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CLIENTS_TABLE_NAME + ";";
    public static final String DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + DATE_TABLE_NAME + ";";
    public static final String DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " + BOOKING_TABLE_NAME + ";";

    public static final String CREATE_ENTRIES_CLIENTS =
            "CREATE TABLE IF NOT EXISTS " + CLIENTS_TABLE_NAME
            + " (" + CLIENTS_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
            + NAME_FIELD + " VARCHAR(45) NOT NULL, "
            + PRICE_FIELD + " INT NOT NULL, "
            + PAYED_FIELD + " INT NULL, "
            + COLOR_FIELD + " VARCHAR(45) NULL, "
            + DELETE_FIELD + " INT NOT NULL);";


    public static final String CREATE_ENTRIES_DATE = "CREATE TABLE IF NOT EXISTS " + DATE_TABLE_NAME
            + " (" + DATE_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
            + DATE_FIELD + " VARCHAR(45) NOT NULL, "
            + TIME_PERIOD_FIELD + " VARCHAR(45) NOT NULL);";

    public static final String CREATE_ENTRIES_BOOKING = "CREATE TABLE IF NOT EXISTS " + BOOKING_TABLE_NAME
            + " (" + BOOKING_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
            + CLIENTS_ID_FIELD + " INTEGER REFERENCES " + CLIENTS_TABLE_NAME + " (" + CLIENTS_ID_FIELD +"), "
            + DATE_ID_FIELD + " INTEGER REFERENCES " + DATE_TABLE_NAME + " (" + DATE_ID_FIELD + "));";
}
