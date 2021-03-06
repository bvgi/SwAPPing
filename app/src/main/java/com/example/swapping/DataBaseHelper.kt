package com.example.swapping

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.Negotiation
import com.example.swapping.Models.Review
import com.example.swapping.Models.User


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val USER_TABLE = "User"
        const val REVIEW_TABLE = "Review"
        const val FOLLOWEDUSERS_TABLE = "FollowedUsers"

        const val ADVERTISEMENT_TABLE = "Announcement"
        const val VOIVODESHIPS_TABLE = "Voivodeships"
        const val CATEGORIES_TABLE = "Categories"
        const val STATUS_TABLE = "Status"
        const val LIKED_TABLE = "Liked"
        const val NEGOTIATION_TABLE = "Negotiations"

        const val DATABASE_VERSION = 5
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

        val SQL_CREATE_AD = "CREATE TABLE $ADVERTISEMENT_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER NOT NULL, " +
                "Title VARCHAR(100) NOT NULL, " +
                "Description VARCHAR(255) NOT NULL, " +
                "Voivodeship INTEGER NOT NULL, " +
                "City VARCHAR(50), " +
                "Category INTEGER NOT NULL, " +
                "Status INTEGER NOT NULL, " +
                "Archived INTEGER NOT NULL, " +
                "Purchaser_id INTEGER, " +
                "Image BLOB, " +
                "Published_date INTEGER NOT NULL, " +
                "FOREIGN KEY(Voivodeship) REFERENCES $VOIVODESHIPS_TABLE(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(Category) REFERENCES $CATEGORIES_TABLE(ID), " +
                "FOREIGN KEY(Status) REFERENCES $STATUS_TABLE(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_AD)

        val SQL_CREATE_NEGOTIATIONS = "CREATE TABLE $NEGOTIATION_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Advertisement INTEGER NOT NULL, " +
                "Owner INTEGER NOT NULL, " + // w??a??ciciel og??oszenia
                "User INTEGER NOT NULL, " + // osoba wykonuj??ca negocjacj??
                "Type INTEGER NOT NULL, " +
                "Offers STRING NOT NULL, " +
                "FOREIGN KEY(Owner) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(Advertisement) REFERENCES $ADVERTISEMENT_TABLE(ID))"
        db.execSQL(SQL_CREATE_NEGOTIATIONS)

        val SQL_CREATE_VOIVODESHIPS = "CREATE TABLE $VOIVODESHIPS_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(30) NOT NULL)"
        db.execSQL(SQL_CREATE_VOIVODESHIPS)

        val SQL_CREATE_CATEGORIES = "CREATE TABLE $CATEGORIES_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255) NOT NULL)"
        db.execSQL(SQL_CREATE_CATEGORIES)

        val SQL_CREATE_STATUS = "CREATE TABLE $STATUS_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255) NOT NULL)"
        db.execSQL(SQL_CREATE_STATUS)

        val SQL_CREATE_LIKED = "CREATE TABLE $LIKED_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Ad INTEGER, " +
                "User INTEGER, " +
                "FOREIGN KEY(Ad) REFERENCES $ADVERTISEMENT_TABLE(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_LIKED)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS $USER_TABLE")
            execSQL("DROP TABLE IF EXISTS $REVIEW_TABLE")
            execSQL("DROP TABLE IF EXISTS $FOLLOWEDUSERS_TABLE")
            execSQL("DROP TABLE IF EXISTS $ADVERTISEMENT_TABLE")
            execSQL("DROP TABLE IF EXISTS $VOIVODESHIPS_TABLE")
            execSQL("DROP TABLE IF EXISTS $CATEGORIES_TABLE")
            execSQL("DROP TABLE IF EXISTS $STATUS_TABLE")
            execSQL("DROP TABLE IF EXISTS $LIKED_TABLE")
            execSQL("DROP TABLE IF EXISTS $NEGOTIATION_TABLE")
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

        val result = db.update(
            USER_TABLE, values, "ID = ?",
            arrayOf(newUser.ID.toString())
        )
        db.close()

        return result
    }

    fun updateMeanRate(userId: Int, meanRate: Double) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Mean_rate", meanRate)

        val result = db.update(
            USER_TABLE, values, "ID = ?",
            arrayOf(userId.toString())
        )
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
        var phoneNumber = ""
        var meanRate = 0.0
        var password = ""

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("ID"))
            name = cursor.getString(cursor.getColumnIndex("Name"))
            email = cursor.getString(cursor.getColumnIndex("Email"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            phoneNumber = cursor.getString(cursor.getColumnIndex("Phone_number"))
            meanRate = cursor.getDouble(cursor.getColumnIndex("Mean_rate"))
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
            phone_number = phoneNumber,
            mean_rate = meanRate,
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
        var phoneNumber = ""
        var meanRate = 0.0
        var password = ""

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("Username"))
            name = cursor.getString(cursor.getColumnIndex("Name"))
            email = cursor.getString(cursor.getColumnIndex("Email"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            phoneNumber = cursor.getString(cursor.getColumnIndex("Phone_number"))
            meanRate = cursor.getDouble(cursor.getColumnIndex("Mean_rate"))
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
            phone_number = phoneNumber,
            mean_rate = meanRate,
            password = password)
    }

    fun getAllUsers() : Array<Pair<String, String>>{
        val db = this.readableDatabase
        val users: MutableList<Pair<String, String>> = mutableListOf()

        val getUserQuery = "SELECT * " +
                "FROM $USER_TABLE"

        val cursor = db.rawQuery(getUserQuery, null)

        val username: String
        val email: String

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("Username"))
            email = cursor.getString(cursor.getColumnIndex("Email"))

            users.add(Pair(username, email))
        }

        cursor.close()
        db.close()

        return users.toTypedArray()
    }

    fun findUsers(string: String) : Array<Pair<Int, String>> {
        val db = this.readableDatabase
        val users: MutableList<Pair<Int, String>> = mutableListOf()

        val getUserQuery = "SELECT * " +
                "FROM $USER_TABLE " +
                "WHERE Username LIKE '%$string%' OR Name LIKE '%$string%'"

        val cursor = db.rawQuery(getUserQuery, null)

        val username: String
        val ID: Int

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("Username"))
            ID = cursor.getInt(cursor.getColumnIndex("ID"))

            users.add(Pair(ID, username))
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
        val cursor: Cursor?

        val getReviewsQuery = "SELECT User, Reviewer, Rate, Description " +
                "FROM $REVIEW_TABLE " +
                "WHERE User = $userId " +
                "ORDER BY ID DESC "

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
        values.put("Followed", followerId)

        val result = db.insert(FOLLOWEDUSERS_TABLE, null, values)

        db.close()

        return result
    }

    fun deleteFollower(userId: Int, followerId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(FOLLOWEDUSERS_TABLE, "User = ? and Followed = ?", arrayOf(userId.toString(), followerId.toString()))

        db.close()

        return result
    }

    fun getFollowers(userId: Int) : Array<Triple<Int, String, String>>{
        val db = this.readableDatabase
        val followers = mutableListOf<Triple<Int, String, String>>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT F.Username AS Username, F.Name AS Name, F.ID AS ID " +
                "FROM $FOLLOWEDUSERS_TABLE " +
                "JOIN $USER_TABLE F ON Followed = F.ID " +
                "WHERE User = $userId"

        try{
            cursor = db.rawQuery(getReviewsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getReviewsQuery)
            return emptyArray()
        }

        var followerUsername: String
        var followerName: String
        var followerID: Int

        if(cursor.moveToFirst()){
            do{
                followerUsername = cursor.getString(cursor.getColumnIndex("Username"))
                followerName = cursor.getString(cursor.getColumnIndex("Name"))
                followerID = cursor.getInt(cursor.getColumnIndex("ID"))
                followers.add(Triple(followerID, followerName, followerUsername))

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return followers.toTypedArray()
    }

    fun getFollowing(userId: Int) : Array<Triple<Int, String, String>>{
        val db = this.readableDatabase
        val following = mutableListOf<Triple<Int, String, String>>()
        val cursor: Cursor?

        val getReviewsQuery = "SELECT U.Username AS Username, U.Name AS Name, U.ID AS ID " +
                "FROM $FOLLOWEDUSERS_TABLE " +
                "JOIN $USER_TABLE U ON User = U.ID " +
                "WHERE Followed = $userId"

        try{
            cursor = db.rawQuery(getReviewsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getReviewsQuery)
            return emptyArray()
        }

        var followerUsername: String
        var followerName: String
        var followerID: Int

        if(cursor.moveToFirst()){
            do{
                followerUsername = cursor.getString(cursor.getColumnIndex("Username"))
                followerName = cursor.getString(cursor.getColumnIndex("Name"))
                followerID = cursor.getInt(cursor.getColumnIndex("ID"))
                following.add(Triple(followerID, followerName, followerUsername))

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return following.toTypedArray()
    }

    // ADDS

    fun addVoivodeships() : Long {
        var result: Long = 0
        val voivodeships = mutableListOf(
            "Dolno??l??skie",
            "Kujawsko-pomorskie",
            "Lubelskie",
            "Lubuskie",
            "????dzkie",
            "Ma??opolskie",
            "Mazowieckie",
            "Opolskie",
            "Podkarpackie",
            "Podlaskie",
            "Pomorskie",
            "??l??skie",
            "??wi??tokrzyskie",
            "Warmi??sko-mazurskie",
            "Wielkopolskie",
            "Zachodniopomorskie"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $VOIVODESHIPS_TABLE")
        db.execSQL("DELETE from sqlite_sequence where name='$VOIVODESHIPS_TABLE'")
        for(name in voivodeships) {
            val values = ContentValues()
            values.put("Name", name)
            result = db.insert(VOIVODESHIPS_TABLE, null, values)

        }
            db.close()
        return result
    }

    fun addCategories() : Long {
        var result: Long = 0

        val categories = mutableListOf(
            "Ksi????ki",
            "Komiksy",
            "Czasopisma",
            "Audiobooki",
            "E-booki",
            "P??yty CD",
            "P??yty winylowe"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $CATEGORIES_TABLE")
        db.execSQL("DELETE from sqlite_sequence where name='$CATEGORIES_TABLE'")
        for (name in categories) {
            val values = ContentValues()
            values.put("Name", name)
            result = db.insert(CATEGORIES_TABLE, null, values)
        }
        db.close()
        return result
    }

    fun addStatuses() : Long {
        var result: Long = 0
        val statuses = mutableListOf(
            "Nowy",
            "U??ywany",
            "Uszkodzony",
            "Powystawowy"
        )
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $STATUS_TABLE")
        db.execSQL("DELETE from sqlite_sequence where name='$STATUS_TABLE'")
        for (name in statuses) {
            val values = ContentValues()
            values.put("Name", name)
            result = db.insert(STATUS_TABLE, null, values)
        }
        db.close()
        return result
    }

    // GETTERS

    fun getCategories() : Array<String>{
        val db = this.readableDatabase
        val categories = mutableListOf<String>()
        val cursor: Cursor?

        val getCategoriesQuery = "SELECT * " +
                "FROM $CATEGORIES_TABLE"

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
                "FROM $VOIVODESHIPS_TABLE"

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
                "FROM $STATUS_TABLE"

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

    // ADVERTISEMENTS

    fun addAnnouncement(ad: Ad) : Long{
        val db = this.writableDatabase
        val values = ContentValues()
        val categoryID = getCategoryID(ad.category)
        val voivodeshipID = getVoivodeshipID(ad.voivodeship)
        val statusID = getStatusID(ad.status)
        values.put("User", ad.user)
        values.put("Title", ad.title)
        values.put("Description", ad.description)
        values.put("Voivodeship", voivodeshipID)
        if(ad.city.isNotBlank())
            values.put("City", ad.city)
        values.put("Category", categoryID)
        values.put("Status", statusID)
        values.put("Archived", ad.archived)
        if (ad.image.isNotEmpty())
            values.put("Image", ad.image)
        values.put("Published_date", ad.published_date)

        val result = db.insert(ADVERTISEMENT_TABLE, null, values)
        db.close()
        println(result)

        return result
    }

    private fun getCategoryID(category: String): Int {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT ID FROM $CATEGORIES_TABLE WHERE Name = '${category}'", null)
        println("SELECT ID FROM $CATEGORIES_TABLE WHERE Name = '${category}'")
        var ID = 0
        if(cursor.moveToFirst()){
            do{
                ID = cursor.getInt(cursor.getColumnIndex("ID"))
            }while (cursor.moveToNext())
        }
        return ID
    }

    private fun getCategoryName(ID: Int): String {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT Name FROM $CATEGORIES_TABLE WHERE ID = $ID", null)
        var name = ""
        if(cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex("Name"))
            }while (cursor.moveToNext())
        }
        return name
    }

    private fun getVoivodeshipID(name: String): Int {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT ID FROM $VOIVODESHIPS_TABLE WHERE Name = '${name}'", null)
        var ID = 0
        if(cursor.moveToFirst()){
            do{
                ID = cursor.getInt(cursor.getColumnIndex("ID"))
            }while (cursor.moveToNext())
        }
        return ID
    }

    private fun getVoivodeshipName(ID: Int): String {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT Name FROM $VOIVODESHIPS_TABLE WHERE ID = $ID", null)
        var name = ""
        if(cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex("Name"))
            }while (cursor.moveToNext())
        }
        return name
    }

    private fun getStatusID(name: String): Int {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT ID FROM $STATUS_TABLE WHERE Name = '${name}'", null)

        var ID = 0
        if(cursor.moveToFirst()){
            do{
                ID = cursor.getInt(cursor.getColumnIndex("ID"))
            }while (cursor.moveToNext())
        }

        return ID
    }

    private fun getStatusName(ID: Int): String {
        val db = this.writableDatabase
        val cursor =
            db.rawQuery("SELECT Name FROM $STATUS_TABLE WHERE ID = $ID", null)
        var name = ""
        if(cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex("Name"))
            }while (cursor.moveToNext())
        }
        return name
    }

    fun updateAnnouncement(ad: Ad) : Int{
        val db = this.writableDatabase
        val values = ContentValues()
        val categoryID = getCategoryID(ad.category)
        val statusID = getStatusID(ad.status)
        val voivodeshipID = getVoivodeshipID(ad.voivodeship)
        values.put("Title", ad.title)
        values.put("Description", ad.description)
        values.put("City", ad.city)
        values.put("Voivodeship", voivodeshipID)
        values.put("Category", categoryID)
        values.put("Status", statusID)
        values.put("Image", ad.image)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(ad.ID.toString()))
        db.close()

        return result
    }

    fun getUserAnnouncements(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Ad>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
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
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(
                    Ad(
                        ID = id,
                        user = user,
                        title = title,
                        description = description,
                        voivodeship = voivodeship,
                        city = city,
                        category = category,
                        status = status,
                        archived = archived,
                        purchaser_id = purchaserId,
                        image = image,
                        published_date = publishedDate
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getNotArchivedAds(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Ad>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE User != $userId " +
                "AND Archived = 0"

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
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(
                    Ad(
                        ID = id,
                        user = user,
                        title = title,
                        description = description,
                        voivodeship = voivodeship,
                        city = city,
                        category = category,
                        status = status,
                        archived = archived,
                        purchaser_id = purchaserId,
                        image = image,
                        published_date = publishedDate
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getUserAnnouncementsNotArchived(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Ad>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE User = $userId " +
                "AND Archived = 0"

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
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(
                    Ad(
                        ID = id,
                        user = user,
                        title = title,
                        description = description,
                        voivodeship = voivodeship,
                        city = city,
                        category = category,
                        status = status,
                        archived = archived,
                        purchaser_id = purchaserId,
                        image = image,
                        published_date = publishedDate
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getPurchasedAnnouncements(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Ad>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE Purchaser_id = $userId AND Archived = 1"

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
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(
                    Ad(
                        ID = id,
                        user = user,
                        title = title,
                        description = description,
                        voivodeship = voivodeship,
                        city = city,
                        category = category,
                        status = status,
                        archived = archived,
                        purchaser_id = purchaserId,
                        image = image,
                        published_date = publishedDate
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getAnnouncements(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE User != $userId"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun getFollowingAds(userID: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery = "SELECT A.* " +
                "FROM $ADVERTISEMENT_TABLE A " +
                "JOIN $FOLLOWEDUSERS_TABLE F ON F.User = A.User " +
                "WHERE F.Followed = $userID " +
                "AND A.Archived = 0"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun getAnnouncement(id: Int) : Ad {
        val db = this.readableDatabase

        val getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
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
        var purchaserId = 0
        var image = byteArrayOf()
        var publishedDate = 0

        if(cursor.moveToFirst()){
            user = cursor.getInt(cursor.getColumnIndex("User"))
            title = cursor.getString(cursor.getColumnIndex("Title"))
            description = cursor.getString(cursor.getColumnIndex("Description"))
            voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
            city = cursor.getString(cursor.getColumnIndex("City"))
            category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
            status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
            archived = cursor.getInt(cursor.getColumnIndex("Archived"))
            purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
            image = cursor.getBlob(cursor.getColumnIndex("Image"))
            publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
        }

        cursor.close()
        db.close()

        return Ad(
            ID = id,
            user = user,
            title = title,
            description = description,
            voivodeship = voivodeship,
            city = city,
            category = category,
            status = status,
            archived = archived,
            purchaser_id = purchaserId,
            image = image,
            published_date = publishedDate
        )
    }

    fun deleteAd(id: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(ADVERTISEMENT_TABLE, "ID = ?", arrayOf(id.toString()))

        db.close()

        return result
    }

    fun findAds(string: String, userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                "AND User != $userId " +
                "AND Archived = 0"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        println(ads.size)

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAds(string: String, userId: Int, sort: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        var getAnnouncementQuery: String = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }


        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsByStatus(string: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()
        val statusID = getStatusID(filter)
        val getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                "AND User != $userId " +
                "AND Archived = 0 " +
                "AND Status = $statusID"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsByStatus(string: String, userId: Int, filter: String, sort: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery: String
        val statusID = getStatusID(filter)

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Status = $statusID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY U.Mean_rate ASC"
            else -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Status = $statusID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY U.Mean_rate DESC"
        }

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsByRate(string: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery =
            if(filter[0] < '5')
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate >= ${filter[0]}"
            else
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate = ${filter[0]}"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsByRate(string: String, userId: Int, filter: String, sort: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        var getAnnouncementQuery = when(sort){
            1 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
            }
            2 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date DESC"
            }
            3 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
            }
            else ->
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate DESC"
        }
        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsCategory(string: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()
        val categoryID = getCategoryID(filter)
        val getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                "AND User != $userId " +
                "AND Archived = 0 " +
                "AND Status = $categoryID"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsCategory(string: String, userId: Int, filter: String, sort: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val getAnnouncementQuery: String
        val categoryID = getCategoryID(filter)

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Category = $categoryID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE (Title LIKE '%$string%' OR Description LIKE '%$string%') " +
                    "AND User != $userId " +
                    "AND Category = $categoryID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Category = $categoryID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE (A.Title LIKE '%$string%' OR A.Description LIKE '%$string%') " +
                    "AND A.User != $userId " +
                    "AND A.Category = $categoryID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ads.toTypedArray()
    }

    fun findAdsByCategory(categoryName: String, userId: Int, sort: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)
        var getAnnouncementQuery = ""

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE Category = $categoryID " +
                "AND User != $userId " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Category = $categoryID " +
                    "AND User != $userId " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Category = $categoryID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Category = $categoryID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByCategory(categoryName: String, userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)
        println("CATEGORY ID : $categoryID")

        var getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE Category = $categoryID " +
                "AND Archived = 0 " +
                "AND User != $userId"


        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByCategoryByStatus(categoryName: String, userId: Int, sort: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)

        var getAnnouncementQuery = ""
        val statusID = getStatusID(filter)

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Category = $categoryID " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Category = $categoryID " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Category = $categoryID " +
                    "AND A.Status = $statusID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Category = $categoryID " +
                    "AND A.Status = $statusID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByCategoryByStatus(categoryName: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)
        println("CATEGORY ID : $categoryID")

        var getAnnouncementQuery = ""
        val statusID = getStatusID(filter)

        getAnnouncementQuery = "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Category = $categoryID " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                "AND Archived = 0"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByCategoryByRate(categoryName: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)
        println("CATEGORY ID : $categoryID")

        var getAnnouncementQuery =
            if(filter[0] < '5')
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Category = $categoryID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate >= ${filter[0]}"
            else
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Category = $categoryID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate = ${filter[0]}"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByCategoryByRate(categoryName: String, userId: Int, sort: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val categoryID = getCategoryID(categoryName)
        println("CATEGORY ID : $categoryID")

        var getAnnouncementQuery = when(sort){
            1 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
            }
            2 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Category = $categoryID " +
                        "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                        "ORDER BY A.Published_date DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date DESC"
            }
            3 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
            }
            else ->
                if(filter[0] < '5')
                    "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Category = $categoryID " +
                        "AND A.User != $userId " +
                        "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                        "ORDER BY U.Mean_rate DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Category = $categoryID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate DESC"
        }

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeship(voivodeship: String, userId: Int, sort: Int?) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeship)

        var getAnnouncementQuery: String

        if(sort != null){
            getAnnouncementQuery = when(sort){
                1 -> "SELECT * " +
                        "FROM $ADVERTISEMENT_TABLE " +
                        "WHERE Voivodeship = $voivodeshipID " +
                        "AND User != $userId " +
                        "AND Archived = 0 " +
                        "ORDER BY Published_date ASC"
                2 -> "SELECT * " +
                        "FROM $ADVERTISEMENT_TABLE " +
                        "WHERE Voivodeship = $voivodeshipID " +
                        "AND User != $userId " +
                        "AND Archived = 0 " +
                        "ORDER BY Published_date DESC"
                3 -> "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Voivodeship = $voivodeshipID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "ORDER BY Mean_rate ASC"
                else -> "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Voivodeship = $voivodeshipID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "ORDER BY Mean_rate DESC"
            }
        } else {
            getAnnouncementQuery = "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND Archived = 0 " +
                    "AND User != $userId"
        }


        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }


        return ads.toTypedArray()
    }

    fun findAdsByVoivodeship(voivodeship: String, userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeship)

        val getAnnouncementQuery = "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND Archived = 0 " +
                    "AND User != $userId"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }
        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByStatus(voivodeshipName: String, userId: Int, sort: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeshipName)
        println("VOIVODESHIP ID : $voivodeshipID")

        var getAnnouncementQuery = ""
        val statusID = getStatusID(filter)

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND User != $userId " +
                    "AND Status = $statusID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Voivodeship = $voivodeshipID " +
                    "AND A.Status = $statusID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Voivodeship = $voivodeshipID " +
                    "AND A.Status = $statusID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByStatus(voivodeshipName: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeshipName)
        println("CATEGORY ID : $voivodeshipID")

        var getAnnouncementQuery = ""
        val statusID = getStatusID(filter)

        getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE Voivodeship = $voivodeshipID " +
                "AND User != $userId " +
                "AND Archived = 0 " +
                "AND Status = $statusID"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByRate(voivodeshipName: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeshipName)

        var getAnnouncementQuery = ""

        getAnnouncementQuery =
            if(filter[0] < '5')
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Voivodeship = $voivodeshipID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate >= ${filter[0]}"
            else
                "SELECT A.* " +
                        "FROM $ADVERTISEMENT_TABLE A " +
                        "JOIN $USER_TABLE U ON A.User = U.ID " +
                        "WHERE A.Voivodeship = $voivodeshipID " +
                        "AND A.User != $userId " +
                        "AND A.Archived = 0 " +
                        "AND U.Mean_rate = ${filter[0]}"

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByRate(voivodeshipName: String, userId: Int, sort: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getCategoryID(voivodeshipName)

        var getAnnouncementQuery = ""

        getAnnouncementQuery = when(sort){
            1 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date ASC"
            }
            2 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY A.Published_date DESC"
            }
            3 -> {
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate ASC"
            }
            else ->
                if(filter[0] < '5')
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate >= ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate DESC"
                else
                    "SELECT A.* " +
                            "FROM $ADVERTISEMENT_TABLE A " +
                            "JOIN $USER_TABLE U ON A.User = U.ID " +
                            "WHERE A.Voivodeship = $voivodeshipID " +
                            "AND A.User != $userId " +
                            "AND U.Mean_rate = ${filter[0]} " +
                            "AND A.Archived = 0 " +
                            "ORDER BY U.Mean_rate DESC"
        }


        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByCategory(voivodeshipName: String, userId: Int, sort: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeshipName)
        println("VOIVODESHIP ID : $voivodeshipID")

        var getAnnouncementQuery = ""
        val categoryID = getCategoryID(filter)

        getAnnouncementQuery = when(sort){
            1 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND User != $userId " +
                    "AND Category = $categoryID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date ASC"
            2 -> "SELECT * " +
                    "FROM $ADVERTISEMENT_TABLE " +
                    "WHERE Voivodeship = $voivodeshipID " +
                    "AND User != $userId " +
                    "AND Category = $categoryID " +
                    "AND Archived = 0 " +
                    "ORDER BY Published_date DESC"
            3 -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Voivodeship = $voivodeshipID " +
                    "AND A.Category = $categoryID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate ASC"
            else -> "SELECT A.* " +
                    "FROM $ADVERTISEMENT_TABLE A " +
                    "JOIN $USER_TABLE U ON A.User = U.ID " +
                    "WHERE A.Voivodeship = $voivodeshipID " +
                    "AND A.Category = $categoryID " +
                    "AND A.User != $userId " +
                    "AND A.Archived = 0 " +
                    "ORDER BY Mean_rate DESC"
        }

        println(getAnnouncementQuery)

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    fun findAdsByVoivodeshipByCategory(voivodeshipName: String, userId: Int, filter: String) : Array<Ad> {
        val db = this.readableDatabase
        val ads = mutableListOf<Ad>()

        val voivodeshipID = getVoivodeshipID(voivodeshipName)
        println("VOIVODESHIP ID : $voivodeshipID")

        var getAnnouncementQuery = ""
        val categoryID = getCategoryID(filter)

        getAnnouncementQuery = "SELECT * " +
                "FROM $ADVERTISEMENT_TABLE " +
                "WHERE Voivodeship = $voivodeshipID " +
                "AND User != $userId " +
                "AND Archived = 0 " +
                "AND Category = $categoryID"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var id: Int
        var user: Int
        var title: String
        var description: String
        var voivodeship: String
        var city: String
        var category: String
        var status: String
        var archived: Int
        var purchaserId: Int
        var image: ByteArray
        var publishedDate: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("ID"))
                user = cursor.getInt(cursor.getColumnIndex("User"))
                title = cursor.getString(cursor.getColumnIndex("Title"))
                description = cursor.getString(cursor.getColumnIndex("Description"))
                voivodeship = getVoivodeshipName(cursor.getInt(cursor.getColumnIndex("Voivodeship")))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = getCategoryName(cursor.getInt(cursor.getColumnIndex("Category")))
                status = getStatusName(cursor.getInt(cursor.getColumnIndex("Status")))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaserId = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                publishedDate = cursor.getInt(cursor.getColumnIndex("Published_date"))
                ads.add(Ad(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    archived = archived,
                    purchaser_id = purchaserId,
                    image = image,
                    published_date = publishedDate
                ))
            } while (cursor.moveToNext())
        }

        return ads.toTypedArray()
    }

    // LIKED ANNOUNCEMENTS

    fun addLiked(userId: Int, announcementId : Int) : Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("Ad", announcementId)
        values.put("User", userId)

        val result = db.insert(LIKED_TABLE, null, values)

        db.close()

        println("LIKED $announcementId, $result")

        return result
    }

    fun deleteLiked(userId: Int, announcementId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(LIKED_TABLE, "User = ? and Ad = ?", arrayOf(userId.toString(), announcementId.toString()))

        db.close()

        println("DISLIKED $announcementId, $result")

        return result
    }

    fun getLiked(userId: Int) : Array<Ad> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Ad>()
        val cursor: Cursor?

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
                val announcementId = cursor.getInt(cursor.getColumnIndex("Ad"))
                announcements.add(getAnnouncement(announcementId))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    // NEGOTIATIONS

    fun startNegotiation(adID: Int, userId: Int, purchaserID: Int, offers: String) : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Advertisement", adID)
        values.put("Owner", userId)
        values.put("Type", 1)
        values.put("User", purchaserID)
        values.put("Offers", offers)

        val result = db.insert(NEGOTIATION_TABLE, null, values)
        db.close()

        println("DB::: RESULT: $result")

        return result
    }

    fun archiveAd(adID: Int, purchaserID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Purchaser_id", purchaserID)
        values.put("Archived", 1)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

    fun acceptNegotiation(negotiationID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Type", 2)

        val result = db.update(NEGOTIATION_TABLE, values, "ID = ?", arrayOf(negotiationID.toString()))
        db.close()

        return result
    }

    fun rejectedNegotiation(negotiationID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Type", 4)

        val result = db.update(NEGOTIATION_TABLE, values, "ID = ?", arrayOf(negotiationID.toString()))
        db.close()

        return result
    }

    fun riseNegotiation(negotiationID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Type", 3)

        val result = db.update(NEGOTIATION_TABLE, values, "ID = ?", arrayOf(negotiationID.toString()))
        db.close()

        return result
    }

    fun updateNegotiation(negotiationID: Int, offers: String) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Type", 1)
        values.put("Offers", offers.toString())

        val result = db.update(NEGOTIATION_TABLE, values, "ID = ?", arrayOf(negotiationID.toString()))
        db.close()

        return result
    }

    fun getUserNegotiations(userID: Int) : Array<Pair<Ad, Negotiation>> {
        val db = this.readableDatabase
        val result = mutableListOf<Pair<Ad, Negotiation>>()
        val cursor: Cursor?

        val getNegotiationsQuery = "SELECT * " +
                "FROM $NEGOTIATION_TABLE " +
                "WHERE Owner = $userID " +
                "OR User = $userID"

        try{
            cursor = db.rawQuery(getNegotiationsQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getNegotiationsQuery)
            return emptyArray()
        }

        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val adID = cursor.getInt(cursor.getColumnIndex("Advertisement"))
                val ownerID = cursor.getInt(cursor.getColumnIndex("Owner"))
                val purchaserID = cursor.getInt(cursor.getColumnIndex("User"))
                val type = cursor.getInt(cursor.getColumnIndex("Type"))
                val offers = cursor.getString(cursor.getColumnIndex("Offers"))
                result.add(Pair(getAnnouncement(adID), Negotiation(
                    ID = ID,
                    adID = adID,
                    ownerID = ownerID,
                    purchaserID = purchaserID,
                    type = type,
                    offers = offers
                )))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return result.toTypedArray()
    }

    fun getNegotiation(ID: Int) : Negotiation {
        val db = this.readableDatabase
        val cursor: Cursor

        val getNegotiationsQuery = "SELECT * " +
                "FROM $NEGOTIATION_TABLE " +
                "WHERE ID = $ID"

        cursor = db.rawQuery(getNegotiationsQuery, null)

        var ID = 0
        var adID = 0
        var ownerID = 0
        var purchaserID = 0
        var type = 0
        var offers = ""
        if(cursor.moveToFirst()){
            ID = cursor.getInt(cursor.getColumnIndex("ID"))
            adID = cursor.getInt(cursor.getColumnIndex("Advertisement"))
            ownerID = cursor.getInt(cursor.getColumnIndex("Owner"))
            purchaserID = cursor.getInt(cursor.getColumnIndex("User"))
            type = cursor.getInt(cursor.getColumnIndex("Type"))
            offers = cursor.getString(cursor.getColumnIndex("Offers"))
        }

        cursor.close()
        db.close()

        return Negotiation(
            ID = ID,
            adID = adID,
            ownerID = ownerID,
            purchaserID = purchaserID,
            type = type,
            offers = offers
        )

    }

    fun deleteNegotiation(adID: Int) : Int {
        val db = this.writableDatabase
        val result = db.delete(NEGOTIATION_TABLE, "Advertisement = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

}