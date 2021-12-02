package com.example.swapping

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.Models.Review
import com.example.swapping.Models.User


class UserDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val USER_TABLE = "User"
        const val REVIEW_TABLE = "Review"
        const val FOLLOWEDUSERS_TABLE = "FollowedUsers"

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

        val SQL_CREATE_FOLLOWEDUSERS = "CREATE TABLE $FOLLOWEDUSERS_TABLE (" +
                "User INTEGER FOREIGN KEY(User) REFERENCES USER(ID) NOT NULL, " +
                "Followed INTEGER FOREIGN KEY(User) REFERENCES USER(ID) NOT NULL"
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

    // USER FUNCTIONS

    fun addUser(user: User, password: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Username", user.username)
        values.put("Email", user.email)
        values.put("Name", user.name)
        values.put("City", user.city)
        values.put("Phone_number", user.phone_number)
        values.put("Password", password)
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

    fun updateUser(newUser: User, password: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Username", newUser.username)
        values.put("Email", newUser.email)
        values.put("Name", newUser.name)
        values.put("City", newUser.city)
        values.put("Phone_number", newUser.phone_number)
        values.put("Password", password)

        val result = db.update(USER_TABLE, values, "Username = ?", arrayOf(newUser.username.toString()))
        db.close()
    }

    fun getUser(username: String) : User{
        val db = this.readableDatabase

        val getUserQuery = "SELECT Name, City, Phone_number, Mean_rate " +
                "FROM $USER_TABLE" +
                "WHERE Username = $username"

        val cursor = db.rawQuery(getUserQuery, null)

        var name: String = ""
        var email: String = ""
        var city: String = ""
        var phone_number: Int = 0
        var mean_rate: Double = 0.0

        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex("Name"))
            email = cursor.getString(cursor.getColumnIndex("Email"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            phone_number = cursor.getInt(cursor.getColumnIndex("Phone_number"))
            mean_rate = cursor.getDouble(cursor.getColumnIndex("Mean_rate"))
        }

        cursor.close()
        db.close()

        return User(username = username, email = email, name = name, city = city, phone_number = phone_number, mean_rate = mean_rate)
    }

    // REVIEW FUNCTIONS

    fun addReview(review: Review){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", review.user)
        values.put("Reviewer", review.reviewer)
        values.put("Rate", review.rate)
        values.put("Description", review.description)

        val result = db.insert(REVIEW_TABLE, null, values)
        db.close()
    }

    fun deleteReview(userId: Int, reviewerId: Int){
        val db = this.writableDatabase
        val result = db.delete(REVIEW_TABLE, "user = ? and reviewer = ?", arrayOf(userId.toString(), reviewerId.toString()))
        db.close()
    }

    @SuppressLint("Recycle")
    fun getUserReviews(userId: Int) : Array<Review> {
        val db = this.readableDatabase
        var reviews = mutableListOf<Review>()
        var cursor: Cursor? = null

        val getReviewsQuery = "SELECT User, Reviewer, Rate, Description " +
                "FROM $REVIEW_TABLE" +
                "WHERE User = $userId"

        try{
            cursor = db.rawQuery(getReviewsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getReviewsQuery)
            return emptyArray()
        }

        var user: Int
        var reviewer: Int
        var rate: Int
        var description: String

        if(cursor.moveToFirst()){
            do{
                user = cursor.getInt(cursor.getColumnIndex("User"))
                reviewer = cursor.getInt(cursor.getColumnIndex("Reviewer"))
                rate = cursor.getInt(cursor.getColumnIndex("Rate"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                reviews.add(Review(user = user, reviewer = reviewer, rate = rate, description = description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return reviews.toTypedArray()
    }

    // FOLLOWERS FUNCTIONS

    fun addFollower(userId: Int, followerId: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", userId)
        values.put("Reviewer", followerId)

        val result = db.insert(FOLLOWEDUSERS_TABLE, null, values)
        db.close()
    }

    fun deleteFollower(userId: Int, followerId: Int){
        val db = this.writableDatabase
        val result = db.delete(FOLLOWEDUSERS_TABLE, "user = ? and followed = ?", arrayOf(userId.toString(), followerId.toString()))
        db.close()
    }

    fun getFollowers(username: String) : Array<String>{
        val db = this.readableDatabase
        val followers = mutableListOf<String>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT F.Username AS Follower" +
                "FROM $FOLLOWEDUSERS_TABLE" +
                "JOIN $USER_TABLE U ON User = U.ID" +
                "JOIN $USER_TABLE F ON Followed = F.ID" +
                "WHERE U.Username = $username"

        try{
            cursor = db.rawQuery(getReviewsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getReviewsQuery)
            return emptyArray()
        }

        var follower: String

        if(cursor.moveToFirst()){
            do{
                follower = cursor.getString(cursor.getColumnIndex("Follower"))
                followers.add(follower)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return followers.toTypedArray()
    }

    fun getFollowed(username: String) : Array<String>{
        val db = this.readableDatabase
        val followed = mutableListOf<String>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT U.Username AS Followed" +
                "FROM $FOLLOWEDUSERS_TABLE" +
                "JOIN $USER_TABLE U ON User = U.ID" +
                "JOIN $USER_TABLE F ON Followed = F.ID" +
                "WHERE F.Username = $username"

        try{
            cursor = db.rawQuery(getReviewsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getReviewsQuery)
            return emptyArray()
        }

        var user: String

        if(cursor.moveToFirst()){
            do{
                user = cursor.getString(cursor.getColumnIndex("Followed"))
                followed.add(user)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return followed.toTypedArray()
    }


}