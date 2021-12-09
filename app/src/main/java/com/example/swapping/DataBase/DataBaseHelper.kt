package com.example.swapping.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.Models.Announcement
import com.example.swapping.Models.Review
import com.example.swapping.Models.User


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val USER_TABLE = "User"
        const val REVIEW_TABLE = "Review"
        const val FOLLOWEDUSERS_TABLE = "FollowedUsers"

        const val ANNOUNCEMENT_TABLE = "Announcement"
        const val VOIVODESHIPS_TABLE = "Voivodeships"
        const val CATEGORIES_TABLE = "Categories"
        const val STATUS_TABLE = "Status"
        const val GENRE_TABLE = "Genre"
        const val LIKED_TABLE = "Liked"

        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "swAPPing.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_USER = "CREATE TABLE $USER_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username VARCHAR(50) NOT NULL, " +
                "Email VARCHAR(255) NOT NULL, " +
                "Name VARCHAR(100) NOT NULL, " +
                "City VARCHAR(50), " +
                "Phone_number VARCHAR(15) NOT NULL, " +
                "Password VARCHAR(50) NOT NULL, " +
                "Logged_in BOOLEAN NOT NULL, " +
                "Mean_rate INTEGER)"
        db.execSQL(SQL_CREATE_USER)

        val SQL_CREATE_REVIEW = "CREATE TABLE $REVIEW_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER NOT NULL, " +
                "Reviewer INTEGER, " +
                "Rate INTEGER NOT NULL, " +
                "Description VARCHAR(255), " +
                "FOREIGN KEY (User) REFERENCES $USER_TABLE (ID)," +
                "FOREIGN KEY (Reviewer) REFERENCES $USER_TABLE (ID))"
        db.execSQL(SQL_CREATE_REVIEW)

        val SQL_CREATE_FOLLOWEDUSERS = "CREATE TABLE $FOLLOWEDUSERS_TABLE (" +
                "User INTEGER NOT NULL, " +
                "Followed INTEGER NOT NULL, " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(Followed) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_FOLLOWEDUSERS)

        val SQL_CREATE_ANNOUCEMENT = "CREATE TABLE ${DataBaseHelper.ANNOUNCEMENT_TABLE} (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER NOT NULL, " +
                "Title VARCHAR(100) NOT NULL, " +
                "Description VARCHAR(255) NOT NULL, " +
                "Voivodeship INTEGER NOT NULL, " +
                "City VARCHAR(50), " +
                "Category INTEGER NOT NULL, " +
                "Status INTEGER NOT NULL, " +
                "Negotiation INTEGER NOT NULL, " +
                "Archived INTEGER NOT NULL, " +
                "Purchaser_id INTEGER, " +
                "Image BLOB, " +
                "Published_date INTEGER NOT NULL, " +
                "FOREIGN KEY(Voivodeship) REFERENCES ${DataBaseHelper.VOIVODESHIPS_TABLE}(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(Category) REFERENCES ${DataBaseHelper.CATEGORIES_TABLE}(ID), " +
                "FOREIGN KEY(Status) REFERENCES ${DataBaseHelper.STATUS_TABLE}(ID)," +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_ANNOUCEMENT)
        println("ANNOUNCEMENT")

        val SQL_CREATE_VOIVODESHIPS = "CREATE TABLE ${DataBaseHelper.VOIVODESHIPS_TABLE} (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(30) NOT NULL)"
        db.execSQL(SQL_CREATE_VOIVODESHIPS)
        println("VOIVODESHIP")

        val SQL_CREATE_CATEGORIES = "CREATE TABLE ${DataBaseHelper.CATEGORIES_TABLE} (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255) NOT NULL)"
        db.execSQL(SQL_CREATE_CATEGORIES)
        println("CATEGORIES")

        val SQL_CREATE_STATUS = "CREATE TABLE ${DataBaseHelper.STATUS_TABLE} (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255) NOT NULL)"
        db.execSQL(SQL_CREATE_STATUS)

        val SQL_CREATE_LIKED = "CREATE TABLE ${DataBaseHelper.LIKED_TABLE} (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Announcement INTEGER, " +
                "User INTEGER, " +
                "FOREIGN KEY(Announcement) REFERENCES ${DataBaseHelper.ANNOUNCEMENT_TABLE}(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_LIKED)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS User")
            execSQL("DROP TABLE IF EXISTS Review")
            execSQL("DROP TABLE IF EXISTS FollowedUsers")
            execSQL("DROP TABLE IF EXISTS Announcement")
            execSQL("DROP TABLE IF EXISTS Voivodeships")
            execSQL("DROP TABLE IF EXISTS Categories")
            execSQL("DROP TABLE IF EXISTS Status")
            execSQL("DROP TABLE IF EXISTS Genre")
            execSQL("DROP TABLE IF EXISTS Liked")
            onCreate(this)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    // USER FUNCTIONS

    fun addUser(user: User) : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        with(values) {
            put("Username", user.username)
            put("Email", user.email)
            put("Name", user.name)
            put("City", user.city)
            put("Phone_number", user.phone_number)
            put("Password", user.password)
            put("Logged_in", user.logged_in)
        }

        val result = db.insert(USER_TABLE, null, values)
        db.close()

        return result
    }

    fun setLoggedIn(userid: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Logged_in", 1)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(userid.toString()))

        db.close()

        return result
    }

    fun setLoggedOut(userid: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Logged_in", 0)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(userid.toString()))
        db.close()

        return result
    }

    fun updateUser(newUser: User) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Username", newUser.username)
        values.put("Email", newUser.email)
        values.put("Name", newUser.name)
        values.put("City", newUser.city)
        values.put("Phone_number", newUser.phone_number)
        values.put("Password", newUser.password)

        val result = db.update(Companion.USER_TABLE, values, "Username = ?", arrayOf(newUser.username))
        db.close()

        return result
    }

    fun getUserByUsername(username: String) : User {
        val db = this.readableDatabase

        val getUserQuery = "SELECT * " +
                "FROM $USER_TABLE " +
                "WHERE Username = \'" + username + "\'"

        val cursor = db.rawQuery(getUserQuery, null)

        var id = -1
        var name = ""
        var email = ""
        var city = ""
        var phone_number = ""
        var mean_rate = 0.0
        var password = ""

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("ID"))
            name = cursor.getString(cursor.getColumnIndex("Name"))
            email = cursor.getString(cursor.getColumnIndex("Email"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            phone_number = cursor.getString(cursor.getColumnIndex("Phone_number"))
            mean_rate = cursor.getDouble(cursor.getColumnIndex("Mean_rate"))
            password = cursor.getString(cursor.getColumnIndex("Password"))
        }

        cursor.close()
        db.close()

        return User(
            ID = id,
            username = username,
            email = email,
            name = name,
            city = city,
            phone_number = phone_number,
            mean_rate = mean_rate,
            password = password)
    }

    fun getUserById(id: Int) : User {
        val db = this.readableDatabase

        val getUserQuery = "SELECT * " +
                "FROM $USER_TABLE " +
                "WHERE ID = $id"

        val cursor = db.rawQuery(getUserQuery, null)

        var username = ""
        var name = ""
        var email = ""
        var city = ""
        var phone_number = ""
        var mean_rate = 0.0
        var password = ""

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("Username"))
            name = cursor.getString(cursor.getColumnIndex("Name"))
            email = cursor.getString(cursor.getColumnIndex("Email"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            phone_number = cursor.getString(cursor.getColumnIndex("Phone_number"))
            mean_rate = cursor.getDouble(cursor.getColumnIndex("Mean_rate"))
            password = cursor.getString(cursor.getColumnIndex("Password"))
        }

        cursor.close()
        db.close()

        return User(
            ID = id,
            username = username,
            email = email,
            name = name,
            city = city,
            phone_number = phone_number,
            mean_rate = mean_rate,
            password = password)
    }

    fun getAllUsers() : Array<Pair<String, String>>{
        val db = this.readableDatabase
        val users: MutableList<Pair<String, String>> = mutableListOf()

        val getUserQuery = "SELECT * " +
                "FROM $USER_TABLE"

        val cursor = db.rawQuery(getUserQuery, null)

        var username: String
        var email: String

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("Username"))
            email = cursor.getString(cursor.getColumnIndex("Email"))

            users.add(Pair(username, email))
        }

        cursor.close()
        db.close()

        return users.toTypedArray()
    }

    // REVIEW FUNCTIONS

    fun addReview(review: Review) : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", review.user)
        values.put("Reviewer", review.reviewer)
        values.put("Rate", review.rate)
        values.put("Description", review.description)

        val result = db.insert(REVIEW_TABLE, null, values)

        db.close()

        return result
    }

    fun deleteReview(userId: Int, reviewerId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(REVIEW_TABLE, "user = ? and reviewer = ?", arrayOf(userId.toString(), reviewerId.toString()))

        db.close()

        return result
    }

    @SuppressLint("Recycle")
    fun getUserReviews(userId: Int) : Array<Review> {
        val db = this.readableDatabase
        val reviews = mutableListOf<Review>()
        var cursor: Cursor?

        val getReviewsQuery = "SELECT User, Reviewer, Rate, Description " +
                "FROM $REVIEW_TABLE " +
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

    fun addFollower(userId: Int, followerId: Int) : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", userId)
        values.put("Reviewer", followerId)

        val result = db.insert(FOLLOWEDUSERS_TABLE, null, values)

        db.close()

        return result
    }

    fun deleteFollower(userId: Int, followerId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(FOLLOWEDUSERS_TABLE, "user = ? and followed = ?", arrayOf(userId.toString(), followerId.toString()))

        db.close()

        return result
    }

    fun getFollowers(userId: String) : Array<String>{
        val db = this.readableDatabase
        val followers = mutableListOf<String>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT F.Username AS Follower" +
                "FROM $FOLLOWEDUSERS_TABLE" +
                "JOIN $USER_TABLE F ON Followed = F.ID " +
                "WHERE User = $userId"

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

    fun getFollowed(userId: String) : Array<String>{
        val db = this.readableDatabase
        val followed = mutableListOf<String>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT U.Username AS Followed" +
                "FROM $FOLLOWEDUSERS_TABLE" +
                "JOIN $USER_TABLE U ON User = U.ID " +
                "WHERE Followed = $userId"

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

    // ADDS

    fun addVoivodeships() : Long {
        var result: Long = 0
        val voivodeships = mutableListOf(
            "Dolnośląskie",
            "Kujawsko-pomorskie",
            "Lubelskie",
            "Lubuskie",
            "Łódzkie",
            "Małopolskie",
            "Mazowieckie",
            "Opolskie",
            "Podkarpackie",
            "Podlaskie",
            "Pomorskie",
            "Śląskie",
            "Świętokrzyskie",
            "Warmińsko-mazurskie",
            "Wielkopolskie",
            "Zachodniopomorskie"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $VOIVODESHIPS_TABLE")
        for(name in voivodeships) {

            val values = ContentValues()

            values.put("Name", name)

            result = db.insert(DataBaseHelper.VOIVODESHIPS_TABLE, null, values)

        }
            db.close()
        return result
    }

    fun addCategories() : Long {
        var result: Long = 0

        val categories = mutableListOf(
            "Książki",
            "Komiksy",
            "Czasopisma",
            "Audiobooki",
            "E-booki",
            "Płyty CD",
            "Płyty winylowe"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $CATEGORIES_TABLE")
        for (name in categories) {
            val values = ContentValues()
            values.put("Name", name)
            println(values)
            result = db.insert(DataBaseHelper.CATEGORIES_TABLE, null, values)
        }
        db.close()

        println("CATEGORIES ADDED")
        return result
    }

    fun addStatuses() : Long {
        var result: Long = 0
        val statuses = mutableListOf(
            "Nowy",
            "Używany",
            "Uszkodzony",
            "Powystawowy"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $STATUS_TABLE")
        for (name in statuses) {
            val values = ContentValues()
            values.put("Name", name)
            result = db.insert(DataBaseHelper.STATUS_TABLE, null, values)
        }
        db.close()
        println("STATUSES ADDED")
        return result
    }

    // GETTERS

    fun getCategories() : Array<String>{
        val db = this.readableDatabase
        val categories = mutableListOf<String>()
        val cursor: Cursor?

        val getCategoriesQuery = "SELECT * " +
                "FROM ${DataBaseHelper.CATEGORIES_TABLE}"

        try{
            cursor = db.rawQuery(getCategoriesQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getCategoriesQuery)
            return emptyArray()
        }


        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(cursor.getColumnIndex("Name")))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return categories.toTypedArray()
    }

    fun getVoivodeships() : Array<String>{
        val db = this.readableDatabase
        val voivodeships = mutableListOf<String>()
        val cursor: Cursor?

        val getVoivodeshipsQuery = "SELECT * " +
                "FROM ${DataBaseHelper.VOIVODESHIPS_TABLE}"

        try{
            cursor = db.rawQuery(getVoivodeshipsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getVoivodeshipsQuery)
            return emptyArray()
        }

        if(cursor.moveToFirst()){
            do{
                voivodeships.add(cursor.getString(cursor.getColumnIndex("Name")))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return voivodeships.toTypedArray()
    }

    fun getStatuses() : Array<String>{
        val db = this.readableDatabase
        val statuses = mutableListOf<String>()
        val cursor: Cursor?

        val getStatusQuery = "SELECT * " +
                "FROM ${DataBaseHelper.STATUS_TABLE}"

        try{
            cursor = db.rawQuery(getStatusQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getStatusQuery)
            return emptyArray()
        }

        if(cursor.moveToFirst()){
            do{
                statuses.add(cursor.getString(cursor.getColumnIndex("Name")))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return statuses.toTypedArray()
    }

    // ANNOUNCEMENTS

    fun addAnnouncement(announcement: Announcement) : Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("User", announcement.user)
        values.put("Title", announcement.title)
        values.put("Description", announcement.description)
        values.put("Voivodeship", announcement.voivodeship)
        if(announcement.city.isNotBlank())
            values.put("City", announcement.city)
        values.put("Category", announcement.category)
        values.put("Status", announcement.status)
        values.put("Negotiation", announcement.negotiation)
        values.put("Archived", announcement.archived)
        if (announcement.image.isNotEmpty())
            values.put("Image", announcement.image)
        values.put("Published_date", announcement.published_date)

        val result = db.insert(ANNOUNCEMENT_TABLE, null, values)
        db.close()

        return result
    }

    fun updateAnnouncement(announcement: Announcement) : Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Title", announcement.title)
        values.put("Description", announcement.description)
        values.put("Status", announcement.status)
        if (announcement.image.isNotEmpty())
            values.put("Image", announcement.image)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(announcement.ID.toString()))
        db.close()

        return result
    }

    fun getUserAnnouncements(userId: Int) : Array<Announcement> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Announcement>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ANNOUNCEMENT_TABLE " +
                "WHERE User = $userId"

        try{
            cursor = db.rawQuery(getAnnouncementsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getAnnouncementsQuery)
            return emptyArray()
        }

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaser_id: Int
        var image: ByteArray
        var published_date: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = cursor.getString(cursor.getColumnIndex("Voivodeship"))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = cursor.getString(cursor.getColumnIndex("Category"))
                status = cursor.getString(cursor.getColumnIndex("Status"))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaser_id = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                published_date = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(
                    Announcement(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaser_id,
                    image = image,
                    published_date = published_date
                )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getAnnouncement(id: Int) : Announcement {
        val db = this.readableDatabase

        val getAnnouncementQuery = "SELECT * " +
                "FROM $ANNOUNCEMENT_TABLE " +
                "WHERE ID = $id"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var user = 0
        var title = ""
        var description = ""
        var voivodeship = ""
        var city = ""
        var category = ""
        var status = ""
        var archived = 0
        var purchaser_id = 0
        var image = byteArrayOf()
        var published_date = 0

        if(cursor.moveToFirst()){
            user = cursor.getInt(cursor.getColumnIndex("User"))
            title = cursor.getString(cursor.getColumnIndex("Title"))
            description = cursor.getString(cursor.getColumnIndex("Description"))
            voivodeship = cursor.getString(cursor.getColumnIndex("Voivodeship"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            category = cursor.getString(cursor.getColumnIndex("Category"))
            status = cursor.getString(cursor.getColumnIndex("Status"))
            archived = cursor.getInt(cursor.getColumnIndex("Archived"))
            purchaser_id = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
            image = cursor.getBlob(cursor.getColumnIndex("Image"))
            published_date = cursor.getInt(cursor.getColumnIndex("Published_date"))
        }

        cursor.close()
        db.close()

        return Announcement(
            ID = id,
            user = user,
            title = title,
            description = description,
            voivodeship = voivodeship,
            city = city,
            category = category,
            status = status,
            archived = archived,
            purchaser_id = purchaser_id,
            image = image,
            published_date = published_date
        )
    }

    // LIKED ANNOUNCEMENTS

    fun addLiked(userId: Int, announcementId : Int) : Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("Announcement", announcementId)
        values.put("User", userId)

        val result = db.insert(DataBaseHelper.LIKED_TABLE, null, values)

        db.close()

        return result
    }

    fun deleteLiked(userId: Int, announcementId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(DataBaseHelper.LIKED_TABLE, "User = ? and Announcement = ?", arrayOf(userId.toString(), announcementId.toString()))

        db.close()

        return result
    }

    fun getLiked(userId: Int) : Array<Announcement> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Announcement>()
        var cursor: Cursor?

        val geLikedQuery = "SELECT * " +
                "FROM $LIKED_TABLE " +
                "WHERE User = $userId"

        try{
            cursor = db.rawQuery(geLikedQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(geLikedQuery)
            return emptyArray()
        }

        if(cursor.moveToFirst()){
            do{
                val announcementId = cursor.getInt(cursor.getColumnIndex("Announcement"))
                announcements.add(getAnnouncement(announcementId))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }


}