package com.example.swapping.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.Models.Ad
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

        val SQL_CREATE_AD = "CREATE TABLE $ADVERTISEMENT_TABLE (" +
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
                "FOREIGN KEY(Voivodeship) REFERENCES $VOIVODESHIPS_TABLE(ID), " +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "FOREIGN KEY(Category) REFERENCES $CATEGORIES_TABLE(ID), " +
                "FOREIGN KEY(Status) REFERENCES $STATUS_TABLE(ID)," +
                "FOREIGN KEY(User) REFERENCES $USER_TABLE(ID))"
        db.execSQL(SQL_CREATE_AD)

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
            execSQL("DROP TABLE IF EXISTS User")
            execSQL("DROP TABLE IF EXISTS Review")
            execSQL("DROP TABLE IF EXISTS FollowedUsers")
            execSQL("DROP TABLE IF EXISTS Ad")
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

        val result = db.update(USER_TABLE, values, "ID = ?",
            arrayOf(newUser.ID.toString())
        )
        db.close()

        return result
    }

    fun updateMeanRate(userId: Int, meanRate: Double) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Mean_rate", meanRate)

        val result = db.update(USER_TABLE, values, "ID = ?",
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
            result = db.insert(VOIVODESHIPS_TABLE, null, values)

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
            result = db.insert(CATEGORIES_TABLE, null, values)
        }
        db.close()
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
        values.put("User", ad.user)
        values.put("Title", ad.title)
        values.put("Description", ad.description)
        values.put("Voivodeship", ad.voivodeship)
        if(ad.city.isNotBlank())
            values.put("City", ad.city)
        values.put("Category", ad.category)
        values.put("Status", ad.status)
        values.put("Negotiation", ad.negotiation)
        values.put("Archived", ad.archived)
        if (ad.image.isNotEmpty())
            values.put("Image", ad.image)
        values.put("Published_date", ad.published_date)

        val result = db.insert(ADVERTISEMENT_TABLE, null, values)
        db.close()

        return result
    }

    fun updateAnnouncement(ad: Ad) : Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Title", ad.title)
        values.put("Description", ad.description)
        values.put("City", ad.city)
        values.put("Voivodeship", ad.voivodeship)
        values.put("Category", ad.category)
        values.put("Status", ad.status)
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
                voivodeship = cursor.getString(cursor.getColumnIndex("Voivodeship"))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = cursor.getString(cursor.getColumnIndex("Category"))
                status = cursor.getString(cursor.getColumnIndex("Status"))
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
                voivodeship = cursor.getString(cursor.getColumnIndex("Voivodeship"))
                city = cursor.getString(cursor.getColumnIndex("City"))
                category = cursor.getString(cursor.getColumnIndex("Category"))
                status = cursor.getString(cursor.getColumnIndex("Status"))
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
            voivodeship = cursor.getString(cursor.getColumnIndex("Voivodeship"))
            city = cursor.getString(cursor.getColumnIndex("City"))
            category = cursor.getString(cursor.getColumnIndex("Category"))
            status = cursor.getString(cursor.getColumnIndex("Status"))
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

    fun deleteAnnouncement(id: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(ADVERTISEMENT_TABLE, "ID = ?", arrayOf(id.toString()))

        db.close()

        return result
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

    fun startNegotiation(adID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Negotiation", 1)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

    fun acceptedNegotiation(adID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Negotiation", 2)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

    fun rejectedNegotiation(adID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Negotiation", -1)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

    fun restartNegotiation(adID: Int) : Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Negotiation", 0)

        val result = db.update(ADVERTISEMENT_TABLE, values, "ID = ?", arrayOf(adID.toString()))
        db.close()

        return result
    }

}