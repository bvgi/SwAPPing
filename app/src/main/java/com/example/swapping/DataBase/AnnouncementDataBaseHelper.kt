package com.example.swapping.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.swapping.DataBase.UserDataBaseHelper.Companion.USER_TABLE
import com.example.swapping.Models.Announcement


class AnnouncementDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val SQL_CREATE_ANNOUCEMENT = "CREATE TABLE $ANNOUNCEMENT_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User INTEGER FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), NOT NULL, " +
                "Title VARCHAR(100), NOT NULL, " +
                "Description VARCHAR(255), NOT NULL, " +
                "Voivodeship INTEGER FOREIGN KEY(Voivodeship) REFERENCES $VOIVODESHIPS_TABLE(ID), NOT NULL, " +
                "City VARCHAR(50), " +
                "Category INTEGER FOREIGN KEY(Category) REFERENCES $CATEGORIES_TABLE(ID), NOT NULL, " +
                "Status INTEGER FOREIGN KEY(Status) REFERENCES $STATUS_TABLE(ID), NOT NULL, " +
                "Genre INTEGER FOREIGN KEY(Genre) REFERENCES $GENRE_TABLE(ID), NOT NULL, " +
                "Year INTEGER, " +
                "Negotiation INTEGER, NOT NULL, " +
                "Archived INTEGER NOT NULL, " +
                "Purchaser_id INTEGER FOREIGN KEY(User) REFERENCES $USER_TABLE(ID), " +
                "Image BLOB, " +
                "Published_date INTEGER, NOT NULL)"
        db.execSQL(SQL_CREATE_ANNOUCEMENT)

        val SQL_CREATE_VOIVODESHIPS = "CREATE TABLE $VOIVODESHIPS_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(30), NOT NULL)"
        db.execSQL(SQL_CREATE_VOIVODESHIPS)
        addVoivodeships()

        val SQL_CREATE_CATEGORIES = "CREATE TABLE $CATEGORIES_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255), NOT NULL)"
        db.execSQL(SQL_CREATE_CATEGORIES)
        addCategories()

        val SQL_CREATE_STATUS = "CREATE TABLE $STATUS_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(255), NOT NULL)"
        db.execSQL(SQL_CREATE_STATUS)
        addStatuses()

        val SQL_CREATE_GENRE = "CREATE TABLE $GENRE_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR(50), NOT NULL)"
        db.execSQL(SQL_CREATE_GENRE)

        val SQL_CREATE_LIKED = "CREATE TABLE $LIKED_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Announcement INTEGER FOREIGN KEY(Announcement) REFERENCES $ANNOUNCEMENT_TABLE(ID), " +
                "User INTEGER FOREIGN KEY(User) REFERENCES $USER_TABLE(ID)"
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
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "swAPPing.db"

        const val ANNOUNCEMENT_TABLE = "Announcement"
        const val VOIVODESHIPS_TABLE = "Voivodeships"
        const val CATEGORIES_TABLE = "Categories"
        const val STATUS_TABLE = "Status"
        const val GENRE_TABLE = "Genre"
        const val LIKED_TABLE = "Liked"
    }


    // ADDS

    private fun addVoivodeships() : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        val voivodeships = mutableListOf(
            "dolnośląskie",
            "kujawsko-pomorskie",
            "lubelskie",
            "lubuskie",
            "łódzkie",
            "małopolskie",
            "mazowieckie",
            "opolskie",
            "podkarpackie",
            "podlaskie",
            "pomorskie",
            "śląskie",
            "świętokrzyskie",
            "warmińsko-mazurskie",
            "wielkopolskie",
            "zachodniopomorskie"
        )
        for(name in voivodeships){
            values.put("Name", name)
        }

        val result = db.insert(VOIVODESHIPS_TABLE, null, values)

        db.close()

        return result
    }

    private fun addCategories() : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        val categories = mutableListOf(
            "Książki",
            "Komiksy",
            "Czasopisma",
            "Audiobooki",
            "E-booki",
            "Płyty CD",
            "Płyty winylowe"
        )
        for(name in categories){
            values.put("Name", name)
        }

        val result = db.insert(CATEGORIES_TABLE, null, values)

        db.close()

        return result
    }

    private fun addStatuses() : Long {
        val db = this.writableDatabase
        val values = ContentValues()
        val statuses = mutableListOf(
            "Nowy",
            "Używany",
            "Uszkodzony",
            "Powystawowy"
        )
        for(name in statuses){
            values.put("Name", name)
        }

        val result = db.insert(STATUS_TABLE, null, values)

        db.close()

        return result
    }

    fun addGenre(genreName: String) : Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("Name", genreName)

        val result = db.insert(GENRE_TABLE, null, values)

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

    fun getGenres() : Array<String>{
        val db = this.readableDatabase
        val genres = mutableListOf<String>()
        val cursor: Cursor?

        val getGenresQuery = "SELECT * " +
                "FROM $GENRE_TABLE"

        try{
            cursor = db.rawQuery(getGenresQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(getGenresQuery)
            return emptyArray()
        }

        if(cursor.moveToFirst()){
            do{
                genres.add(cursor.getString(cursor.getColumnIndex("Name")))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return genres.toTypedArray()
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

    // ANNOUNCEMENTS

    fun addAnnouncement(announcement: Announcement){
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
        values.put("Genre", announcement.genre)
        if(announcement.year.isNotBlank())
            values.put("Year", announcement.year)
        if (announcement.image.isNotEmpty())
            values.put("Image", announcement.image)
        values.put("Published_date", announcement.published_date)

        val result = db.insert(ANNOUNCEMENT_TABLE, null, values)
        db.close()
    }

    fun updateAnnouncement(announcement: Announcement){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Title", announcement.title)
        values.put("Description", announcement.description)
        values.put("Status", announcement.status)
        values.put("Genre", announcement.genre)
        if(announcement.year.isNotBlank())
            values.put("Year", announcement.year)
        if (announcement.image.isNotEmpty())
            values.put("Image", announcement.image)

        val result = db.update(USER_TABLE, values, "ID = ?", arrayOf(announcement.ID.toString()))
        db.close()
    }

    fun getUserAnnouncements(userId: Int) : Array<Announcement> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Announcement>()
        val cursor: Cursor?

        val getAnnouncementsQuery = "SELECT * " +
                "FROM $ANNOUNCEMENT_TABLE" +
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
        var genre: String
        var year: String
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
                genre = cursor.getString(cursor.getColumnIndex("Genre"))
                year = cursor.getString(cursor.getColumnIndex("Year"))
                archived = cursor.getInt(cursor.getColumnIndex("Archived"))
                purchaser_id = cursor.getInt(cursor.getColumnIndex("Purchaser_id"))
                image = cursor.getBlob(cursor.getColumnIndex("Image"))
                published_date = cursor.getInt(cursor.getColumnIndex("Published_date"))

                announcements.add(Announcement(
                    ID = id,
                    user = user,
                    title = title,
                    description = description,
                    voivodeship = voivodeship,
                    city = city,
                    category = category,
                    status = status,
                    genre = genre,
                    year = year,
                    archived = archived,
                    purchaser_id = purchaser_id,
                    image = image,
                    published_date = published_date
                ))

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return announcements.toTypedArray()
    }

    fun getAnnouncement(id: Int) : Announcement{
        val db = this.readableDatabase
        val announcement: Announcement

        val getAnnouncementQuery = "SELECT * " +
                "FROM $ANNOUNCEMENT_TABLE" +
                "WHERE ID = $id"

        val cursor = db.rawQuery(getAnnouncementQuery, null)

        var user = 0
        var title = ""
        var description = ""
        var voivodeship = ""
        var city = ""
        var category = ""
        var status = ""
        var genre = ""
        var year = ""
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
                genre = cursor.getString(cursor.getColumnIndex("Genre"))
                year = cursor.getString(cursor.getColumnIndex("Year"))
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
            genre = genre,
            year = year,
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

        val result = db.insert(LIKED_TABLE, null, values)

        db.close()

        return result
    }

    fun deleteLiked(userId: Int, announcementId: Int) : Int {
        val db = this.writableDatabase

        val result = db.delete(LIKED_TABLE, "User = ? and Announcement = ?", arrayOf(userId.toString(), announcementId.toString()))

        db.close()

        return result
    }

    fun getLiked(userId: Int) : Array<Announcement> {
        val db = this.readableDatabase
        val announcements = mutableListOf<Announcement>()
        var cursor: Cursor? = null

        val geLikedQuery = "SELECT * " +
                "FROM $LIKED_TABLE" +
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