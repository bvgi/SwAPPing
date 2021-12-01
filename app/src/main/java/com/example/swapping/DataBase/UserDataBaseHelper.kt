package com.example.swapping

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.DataBase.UserDataBaseHelper.Companion.DATABASE_NAME
import com.example.swapping.DataBase.UserDataBaseHelper.Companion.DATABASE_VERSION
import com.example.swapping.Models.User


class UserDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val USER_TABLE = "User"
        const val REVIEW_TABLE = "Review"
        const val FOLLWEDUSERS_TABLE = "FollowedUsers"

        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "swAPPing.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_USER = "CREATE TABLE $USER_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username VARCHAR(50) NOT NULL, " +
                "Email VARCHAR(255) NOT NULL, " +
                "Name VARCHAR(100) NOT NULL, " +
                "City VARCHAR(50), " +
                "Phone_number INTEGER NOT NULL, " +
                "Password VARCHAR(50) NOT NULL, " +
                "Logged_in BOOLEAN NOT NULL, " +
                "Mean_rate INTEGER)"
        db.execSQL(SQL_CREATE_USER)

        val SQL_CREATE_REVIEW = "CREATE TABLE $REVIEW_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER FOREIGN KEY(user) REFERENCES USER(ID), NOT NULL, " +
                "Reviewer INTEGER FOREIGN KEY(User, REFERENCES USER(ID), " +
                "Rate INTEGER, NOT NULL, " +
                "Description VARCHAR(255))"
        db.execSQL(SQL_CREATE_REVIEW)

        val SQL_CREATE_FOLLOWEDUSERS = "CREATE TABLE $FOLLWEDUSERS_TABLE (" +
                "User INTEGER FOREIGN KEY(User) REFERENCES USER(ID), NOT NULL, " +
                "followed INTEGER FOREIGN KEY(User) REFERENCES USER(ID)"
        db.execSQL(SQL_CREATE_FOLLOWEDUSERS)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS User")
        db.execSQL("DROP TABLE IF EXISTS Review")
        db.execSQL("DROP TABLE IF EXISTS FollowedUsers")
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    fun addUser(user: User){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Username", user.username)
        values.put("Email", user.email)
        values.put("Name", user.name)
        values.put("City", user.city)
        values.put("Phone_number", user.phone_number)
        values.put("Password", user.password)
        values.put("Logged_in", user.logged_in)

        val result = db.insert(USER_TABLE, null, values)
        db.close()
    }

    fun setLoggedIn(userid: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Logged_in", 1)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(userid.toString()))
        db.close()
    }

    fun setLoggedOut(userid: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Logged_in", 0)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(userid.toString()))
        db.close()
    }

    fun updateUser(newUser: User){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Username", newUser.username)
        values.put("Email", newUser.email)
        values.put("Name", newUser.name)
        values.put("City", newUser.city)
        values.put("Phone_number", newUser.phone_number)
        values.put("Password", newUser.password)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(newUser.ID.toString()))
        db.close()
    }

    fun addReview(userId: Int, reviewerId: Int, rate: Int, description: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", userId)
        values.put("Reviewer", reviewerId)
        values.put("Rate", rate)
        values.put("Description", description)

        val result = db.insert(REVIEW_TABLE, null, values)
        db.close()
    }

    fun addFollower(userId: Int, followerId: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", userId)
        values.put("Reviewer", followerId)

        val result = db.insert(FOLLWEDUSERS_TABLE, null, values)
        db.close()
    }

    fun deleteFollower(userId: Int, followerId: Int){
        val db = this.writableDatabase
        val result = db.delete(FOLLWEDUSERS_TABLE, "user = ? and followed = ?", arrayOf(userId.toString(), followerId.toString()))
        db.close()
    }

    fun deleteReview(userId: Int, reviewerId: Int){
        val db = this.writableDatabase
        val result = db.delete(REVIEW_TABLE, "user = ? and reviewer = ?", arrayOf(userId.toString(), reviewerId.toString()))
        db.close()
    }
}