package com.example.swapping.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class UserDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val SQL_CREATE_ANNOUCEMENT = "CREATE TABLE Announcement (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER FOREIGN KEY(User) REFERENCES USER(ID), NOT NULL, " +
                "Title VARCHAR(100), NOT NULL, " +
                "Description VARCHAR(255), NOT NULL, " +
                "Voivodeship INTEGER FOREIGN KEY(Voivodeship), NOT NULL, " +
                "City VARCHAR(50), " +
                "Category INTEGER FOREIGN KEY(Category), NOT NULL, " +
                "Status INTEGER FOREIGN KEY(Status), NOT NULL, " +
                "Genre INTEGER FOREIGN KEY(Genre), NOT NULL, " +
                "Year INTEGER, " +
                "Negotiation INTEGER, NOT NULL, " +
                "Archived BOOLEAN NOT NULL, " +
                "Purchaser_id INTEGER FOREIGN KEY(User) REFERENCES USER(ID), " +
                "Image BLOB, " +
                "Published_date INTEGER, NOT NULL)"
        db.execSQL(SQL_CREATE_ANNOUCEMENT)

        val SQL_CREATE_VOIVODESHIPS = "CREATE TABLE Voivodeships (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(30), NOT NULL)"
        db.execSQL(SQL_CREATE_VOIVODESHIPS)

        val SQL_CREATE_CATEGORIES = "CREATE TABLE Categories (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255), NOT NULL)"
        db.execSQL(SQL_CREATE_CATEGORIES)

        val SQL_CREATE_STATUS = "CREATE TABLE Status (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255), NOT NULL)"
        db.execSQL(SQL_CREATE_STATUS)

        val SQL_CREATE_GENRE = "CREATE TABLE Genre (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(50), NOT NULL)"
        db.execSQL(SQL_CREATE_GENRE)

        val SQL_CREATE_LIKED = "CREATE TABLE Liked (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Announcement INTEGER FOREIGN KEY(Announcement) REFERENCES ANNOUNCEMENT(ID), " +
                "User INTEGER FOREIGN KEY(User) REFERENCES USER(ID)"
        db.execSQL(SQL_CREATE_LIKED)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS Announcement")
        db.execSQL("DROP TABLE IF EXISTS Voivodeships")
        db.execSQL("DROP TABLE IF EXISTS Categories")
        db.execSQL("DROP TABLE IF EXISTS Status")
        db.execSQL("DROP TABLE IF EXISTS Genre")
        db.execSQL("DROP TABLE IF EXISTS Liked")
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "swAPPing.db"
    }


}