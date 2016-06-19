package com.walterrizzifoundation.grizzi.spca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rania on 26/02/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "SPCA.DB";
    static final int DB_VERSION = 47;

    // table AdoptableSearch Results
    static final String TABLE_ANIMAL_MATCHED = "animalMatched";
    static final String T_ANIMAL_MATCHED_ID = "_id";

    // table animal
    static final String TABLE_ANIMAL = "animalDetails";
    static final String T_ANIMAL_ID = "_id";
    static final String T_ANIMAL_SPECIES = "species";
    static final String T_ANIMAL_NAME = "name";
    static final String T_ANIMAL_AGE = "age";
    static final String T_ANIMAL_PRIMARY_BREED = "primaryBreed";
    static final String T_ANIMAL_SECONDARY_BREED = "secondaryBreed";
    static final String T_ANIMAL_SEX = "sex";
    static final String T_ANIMAL_SIZE = "size";
    static final String T_ANIMAL_STERILE = "sterile";
    static final String T_ANIMAL_INTAKE_DATE = "intake_date";
    static final String T_ANIMAL_PRIMARY_COLOR = "primaryColor";
    static final String T_ANIMAL_SECONDARY_COLOR = "secondaryColor";
    static final String T_ANIMAL_DECLAWED = "declawed";
    static final String T_ANIMAL_DESCRIPTION = "description";
    static final String T_ANIMAL_PHOTO1 = "photo1";
    static final String T_ANIMAL_PHOTO2 = "photo2";
    static final String T_ANIMAL_PHOTO3 = "photo3";
    static final String TABLE_ANIMAL_SPECIES_IDX = "speciesIdx";
    static final String T_ANIMAL_AVAILABLE = "available";
    static final String T_ANIMAL_FAVORITE = "favorite";


    // Indexes des champs dans les cursors
    // si c'est pas les bons indexes, corriger stp.
    public static final int C_ANIMAL_ID = 0;
    public static final int C_ANIMAL_SPECIES = 1;
    public static final int C_ANIMAL_NAME = 2;
    public static final int C_ANIMAL_AGE = 3;
    public static final int C_ANIMAL_PRIMARY_BREED = 4;
    public static final int C_ANIMAL_SECONDARY_BREED = 5;
    public static final int C_ANIMAL_SEX = 6;
    public static final int C_ANIMAL_SIZE = 7;
    public static final int C_ANIMAL_STERILE = 8;
    public static final int C_ANIMAL_INTAKE_DATE = 9;
    public static final int C_ANIMAL_PRIMARY_COLOR = 10;
    public static final int C_ANIMAL_SECONDARY_COLOR = 11;
    public static final int C_ANIMAL_DECLAWED = 13;
    public static final int C_ANIMAL_DESCRIPTION = 12;
    public static final int C_ANIMAL_PHOTO1 = 14;
    public static final int C_ANIMAL_PHOTO2 = 15;
    public static final int C_ANIMAL_PHOTO3 = 16;
    public static final int C_ANIMAL_AVAILABLE = 17;
    public static final int C_ANIMAL_FAVORITE = 18;
    // table photos
    static final String TABLE_ANIMAL_PHOTOS = "animalPhotos";
    static final String T_ANIMAL_PHOTOS_ANIMAL_ID = "_id";
    static final String T_ANIMAL_PHOTOS_PHOTO_ID = "photo_id";
    static final String T_ANIMAL_PHOTOS_PATH = "photoPath";

    // table search params
    static final String TABLE_ADOPTABLE_SEARCH = "adoptableSearchParams";
    //static final String T_ADOPTABLE_SEARCH_ID = "_id";
    static final String T_ADOPTABLE_SEARCH_SPECIES = "species";
    static final String T_ADOPTABLE_SEARCH_NAME = "name";
    static final String T_ADOPTABLE_SEARCH_AGE = "age";
    static final String T_ADOPTABLE_SEARCH_AGE_MIN = "age_min";
    static final String T_ADOPTABLE_SEARCH_AGE_MAX = "age_max";
    static final String T_ADOPTABLE_SEARCH_BREED = "breed";
    static final String T_ADOPTABLE_SEARCH_SEX = "sex";
    static final String T_ADOPTABLE_SEARCH_SIZE = "size";
    static final String T_ADOPTABLE_SEARCH_COLOR = "color";
    static final String T_ADOPTABLE_SEARCH_STERILE = "sterile";
    static final String T_ADOPTABLE_SEARCH_INTAKE_DATE = "intake_date";
    static final String T_ADOPTABLE_SEARCH_DECLAWED  = "declawed";

    //static final int C_ADOPTABLE_SEARCH_ID = "_id";
    static final int C_ADOPTABLE_SEARCH_SPECIES = 0;
    //static final int C_ADOPTABLE_SEARCH_NAME = "name";
    //static final int C_ADOPTABLE_SEARCH_AGE = "age";
    static final int C_ADOPTABLE_SEARCH_AGE_MIN = 1;
    static final int C_ADOPTABLE_SEARCH_AGE_MAX = 2;
    //static final int C_ADOPTABLE_SEARCH_BREED = "breed";
    static final int  C_ADOPTABLE_SEARCH_SEX = 3;
    //static final int C_ADOPTABLE_SEARCH_SIZE = "size";
    //static final int C_ADOPTABLE_SEARCH_COLOR = "color";
    //static final int C_ADOPTABLE_SEARCH_STERILE = "sterile";
    //static final int C_ADOPTABLE_SEARCH_INTAKE_DATE = "intake_date";
    //static final int C_ADOPTABLE_SEARCH_DECLAWED  = "declawed";

    //table preferences
    static final String TABLE_PREFERENCES = "preferences";
    static final String T_PREFERENCES_APP_STATE = "app_state";
    static final String T_PREFERENCES_NOTICE_ENABLED = "notice_enabled";
    static final String T_PREFERENCES_NOTICE_FREQUENCY = "notice_frequency";
    static final String T_PREFERENCES_SESSION_MODE = "session_mode";
    static final String T_PREFERENCES_SESSION_MODE_ONLINE = "onLine";
    static final String T_PREFERENCES_SESSION_MODE_OFFLINE = "offLine";
    static final String T_PREFERENCES_LAST_NOTIFICATION_DATE = "lastNotif";

    static final int  C_PREFERENCES_APP_STATE        = 0;
    static final int  C_PREFERENCES_NOTICE_ENABLED   = 1;
    static final int  C_PREFERENCES_NOTICE_FREQUENCY = 2;
    static final int  C_PREFERENCES_SESSION_MODE     = 3;
    static final int  C_PREFERENCES_LAST_NOTIFICATION_DATE = 4;


    //table favorites
    //static final String TABLE_FAVORITE_ANIMALS = "favorite_animals";
    //static final String T_FAVORITE_ANIMALS_ANIMAL_ID = "_id";
    //static final String T_FAVORITE_ANIMALS_AVAILABLE = "available";

    //table favorites
    /*
    static final String TABLE_NEW_ANIMALS = "new_animals";
    static final String T_NEW_ANIMALS_ANIMAL_ID = "_id";
    static final String T_NEW_ANIMALS_DATE = "indate";

    static final String CURSOR_NAME_NEW_ANIMALS = "New";
    */
    static final String CURSOR_NAME_FAVORITE_ANIMALS = "Favorites";
    static final String CURSOR_NAME_NOTIFICATIONS = "Notifications";
    static final String CURSOR_NAME_SEARCH_ANIMALS = "Search";
    static HashMap<String, Cursor> cursors;
    static boolean firstTimeIn = true;

    private static DBHelper instance;
    private static Context appcontext;

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DBHelper(context);
            cursors = new HashMap<String, Cursor>();
            appcontext = context;
        }
        return instance;
    }
    private DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException {
        Log.d("DB", "In onCreate");
        String sql = "CREATE TABLE " + TABLE_ANIMAL + "(" +
                T_ANIMAL_ID + " INT PRIMARY KEY     NOT NULL," +
                T_ANIMAL_SPECIES + " TEXT    NOT NULL," +
                T_ANIMAL_NAME + " TEXT    NOT NULL," +
                T_ANIMAL_AGE + " INT     NOT NULL," +
                T_ANIMAL_PRIMARY_BREED + " TEXT," +
                T_ANIMAL_SECONDARY_BREED + " TEXT," +
                T_ANIMAL_SEX + " char    CHECK( " + T_ANIMAL_SEX + " IN ('Male','Female', 'Unknown'))," +
                T_ANIMAL_SIZE + " char," +
                T_ANIMAL_STERILE + " char CHECK( " + T_ANIMAL_STERILE + " IN ('Y','N', 'U'))," +
                T_ANIMAL_INTAKE_DATE + " date," +
                T_ANIMAL_PRIMARY_COLOR + " TEXT," +
                T_ANIMAL_SECONDARY_COLOR + " TEXT," +
                T_ANIMAL_DESCRIPTION + " TEXT," +
                T_ANIMAL_DECLAWED + " char CHECK( " + T_ANIMAL_DECLAWED + " IN ('Yes','No','Both','Front'))," +
                // pour une db digne de ce nom, on crée une autre table pour les champs qui suivent.
                T_ANIMAL_PHOTO1 + " TEXT," +
                T_ANIMAL_PHOTO2 + " TEXT," +
                T_ANIMAL_PHOTO3 + " TEXT, " +
                T_ANIMAL_AVAILABLE + " char CHECK(" + T_ANIMAL_AVAILABLE + " IN('Y','N')), " +
                T_ANIMAL_FAVORITE + " char CHECK(" + T_ANIMAL_FAVORITE + " IN('Y','N'))" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_ANIMAL + " table created");

        sql = "CREATE INDEX " + TABLE_ANIMAL_SPECIES_IDX + " ON " + TABLE_ANIMAL + "('" + T_ANIMAL_SPECIES + "')";
        db.execSQL(sql);
        Log.d("DB", "Index " + TABLE_ANIMAL_SPECIES_IDX + " created");

        /* Cette table qu'on crée pour les photos dans une db qui aspire à être digne de ce nom.
        sql = "CREATE TABLE " + TABLE_ANIMAL_PHOTOS + "(" +
                T_ANIMAL_PHOTOS_ANIMAL_ID + " INT  NOT NULL REFERENCES " + TABLE_ANIMAL + "(" + T_ANIMAL_ID + ")," +
                T_ANIMAL_PHOTOS_PHOTO_ID  + " INT  NOT NULL," +
                T_ANIMAL_PHOTOS_PATH      + " TEXT         NOT NULL, " +
                "PRIMARY KEY ('" + T_ANIMAL_PHOTOS_ANIMAL_ID + "', '" + T_ANIMAL_PHOTOS_PHOTO_ID + "')" +
              ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_ANIMAL_PHOTOS + " table created");
        */

        sql = "CREATE TABLE " + TABLE_ADOPTABLE_SEARCH + "(" +
                T_ADOPTABLE_SEARCH_SPECIES + " INT    NOT NULL," +
                //T_ADOPTABLE_SEARCH_AGE         + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_AGE_MIN + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_AGE_MAX + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_SEX + " INT    NOT NULL" +
                //T_ADOPTABLE_SEARCH_STERILE     + " INT    NOT NULL," +
                //T_ADOPTABLE_SEARCH_DECLAWED    + " INT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_ADOPTABLE_SEARCH + " table created");

        sql = "CREATE TABLE " + TABLE_PREFERENCES + "(" +
                T_PREFERENCES_APP_STATE + " int  CHECK(" + T_PREFERENCES_APP_STATE + " IN ('New','InitialLoadDone')), " +
                T_PREFERENCES_NOTICE_ENABLED + " char CHECK(" + T_PREFERENCES_NOTICE_ENABLED + " IN('Y','N')), " +
                T_PREFERENCES_NOTICE_FREQUENCY + " int  CHECK(" + T_PREFERENCES_NOTICE_FREQUENCY + " IN (30,60,3600,21600,43200,86400,604800)), " +
                T_PREFERENCES_SESSION_MODE + " int  CHECK(" + T_PREFERENCES_SESSION_MODE + " IN ('" + T_PREFERENCES_SESSION_MODE_ONLINE + "','" + T_PREFERENCES_SESSION_MODE_OFFLINE + "')), " +
                T_PREFERENCES_LAST_NOTIFICATION_DATE + " TEXT " +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_PREFERENCES + " table created");
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_APP_STATE, "New");
        cv.put(DBHelper.T_PREFERENCES_NOTICE_ENABLED, "Y");
        cv.put(DBHelper.T_PREFERENCES_NOTICE_FREQUENCY, 86400);
        cv.put(DBHelper.T_PREFERENCES_SESSION_MODE, T_PREFERENCES_SESSION_MODE_ONLINE);
        cv.put(DBHelper.T_PREFERENCES_LAST_NOTIFICATION_DATE, "20000101000000");
        db.insertOrThrow(DBHelper.TABLE_PREFERENCES, null, cv);

        /*
        sql = "CREATE TABLE " + TABLE_FAVORITE_ANIMALS + "(" +
                T_FAVORITE_ANIMALS_ANIMAL_ID + " INT PRIMARY KEY     NOT NULL REFERENCES " + TABLE_ANIMAL + "(" + T_ANIMAL_ID + "), " +
                T_FAVORITE_ANIMALS_AVAILABLE + " char CHECK(" + T_FAVORITE_ANIMALS_AVAILABLE + " IN('Y','N'))" +
                ")";
        db.execSQL(sql);
        Log.d("DB", TABLE_FAVORITE_ANIMALS + " table created");

        sql = "CREATE TABLE " + TABLE_NEW_ANIMALS + "(" +
                T_NEW_ANIMALS_ANIMAL_ID + " INT PRIMARY KEY     NOT NULL REFERENCES " + TABLE_ANIMAL + "(" + T_ANIMAL_ID + ") ON DELETE CASCADE, " +
                T_NEW_ANIMALS_DATE      + " TEXT " +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_NEW_ANIMALS + " table created");
*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("DB", "In onUpgrade");
        db.execSQL("drop index if exists " + TABLE_ANIMAL_SPECIES_IDX + ";");
        db.execSQL("drop table if exists " + TABLE_ANIMAL + ";");
        db.execSQL("drop table if exists " + TABLE_ANIMAL_PHOTOS + ";");
        db.execSQL("drop table if exists " + TABLE_ADOPTABLE_SEARCH + ";");
        db.execSQL("drop table if exists " + TABLE_PREFERENCES + ";");
        //db.execSQL("drop table if exists " + TABLE_FAVORITE_ANIMALS + ";");
        //db.execSQL("drop table if exists " + TABLE_NEW_ANIMALS + ";");
        onCreate(db);
    }

    public Cursor getAnimalList(SQLiteDatabase db){
        String sql = "select * from " + TABLE_ANIMAL +
                " order by " + T_ANIMAL_ID + " desc;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getCursorByName(String cursorName) {
        return cursors.get(cursorName);
    }

    public void setCursorForSelect(SQLiteDatabase db, String sql, String cursorName) {
        Cursor c = db.rawQuery(sql, null);
        cursors.put(cursorName, c);
    }

    public Cursor getCursorForSelect(String cursorName) {
        return cursors.get(cursorName);
    }

    public Cursor getCursorForSelect(SQLiteDatabase db, String sql) {
        return db.rawQuery(sql, null);
    }

    public Cursor getAnimalListOrdered(SQLiteDatabase db, String orderClause){
        String sql = "select * from " + TABLE_ANIMAL +
                " order by " + orderClause + ";";
        Log.d("SQL", sql);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void addAnimal(SQLiteDatabase db, Animal animal) throws SQLException {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_ANIMAL_ID, animal.id);
        cv.put(DBHelper.T_ANIMAL_SPECIES, animal.species);
        cv.put(DBHelper.T_ANIMAL_NAME, animal.name);
        cv.put(DBHelper.T_ANIMAL_AGE, animal.age);
        cv.put(DBHelper.T_ANIMAL_PRIMARY_BREED, animal.primaryBreed);
        cv.put(DBHelper.T_ANIMAL_SECONDARY_BREED, animal.secondaryBreed);
        cv.put(DBHelper.T_ANIMAL_SEX, animal.sex);
        cv.put(DBHelper.T_ANIMAL_SIZE, animal.size);
        cv.put(DBHelper.T_ANIMAL_STERILE, animal.sterile);
        cv.put(DBHelper.T_ANIMAL_INTAKE_DATE, animal.intake_date);
        cv.put(DBHelper.T_ANIMAL_PRIMARY_COLOR, animal.primaryColor);
        cv.put(DBHelper.T_ANIMAL_SECONDARY_COLOR, animal.secondaryColor);
        cv.put(DBHelper.T_ANIMAL_DESCRIPTION, animal.description);
        cv.put(DBHelper.T_ANIMAL_DECLAWED, animal.declawed);
        if (animal.photo1 != null) cv.put(DBHelper.T_ANIMAL_PHOTO1, animal.photo1);
        if (animal.photo2 != null) cv.put(DBHelper.T_ANIMAL_PHOTO2, animal.photo2);
        if (animal.photo3 != null) cv.put(DBHelper.T_ANIMAL_PHOTO3, animal.photo3);
        cv.put(DBHelper.T_ANIMAL_AVAILABLE, animal.available);
        cv.put(DBHelper.T_ANIMAL_FAVORITE, animal.favorite);
        db.beginTransaction();
        try {
            String now = getDate();
            Log.d("INSERTING", "NEW id:" + animal.id + " indate:" + now);
            db.insertOrThrow(DBHelper.TABLE_ANIMAL, null, cv);
            cv.clear();
            //cv.put(DBHelper.T_NEW_ANIMALS_ANIMAL_ID, animal.id);
            //cv.put(DBHelper.T_NEW_ANIMALS_DATE, now);
            //db.insertOrThrow(DBHelper.TABLE_NEW_ANIMALS, null, cv);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            animal.dump();
            Log.d("DownloadWebAnimalTask","Transaction aborted." + e.getMessage());
            throw e;
        } finally {
            Log.d("DownloadWebAnimalTask","Transaction finished.");
            db.endTransaction();
        }
    }

    public void addToFavoriteList(SQLiteDatabase db, String animalID) throws SQLException {
        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_FAVORITE,"Y");
        db.update(DBHelper.TABLE_ANIMAL, data, T_ANIMAL_ID + "=" + animalID, null);
    }

    public void removeFromFavoriteList(SQLiteDatabase db, String animalID){
        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_FAVORITE,"N");
        db.update(DBHelper.TABLE_ANIMAL, data, T_ANIMAL_ID + "=" + animalID, null);
        Log.d("DB", "FAV:Removing " + animalID);

    }

    public void removeAllFromFavoriteList(SQLiteDatabase db){
        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_FAVORITE,"N");
        db.update(DBHelper.TABLE_ANIMAL, data, null, null);
    }


    public void makeAllUnavailable(SQLiteDatabase db){
        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_AVAILABLE,"N");
        db.update(DBHelper.TABLE_ANIMAL, data, null, null);
    }

    public void makeAvailable(SQLiteDatabase db, String animalID){
        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_AVAILABLE,"Y");
        db.update(DBHelper.TABLE_ANIMAL, data, T_ANIMAL_ID + "=" + animalID, null);
    }


    public String getFavoriteListSQL() {
        Log.d("DB", "FAV:call get fav list sql");
        return "SELECT " +
                T_ANIMAL_ID + ", " +
                T_ANIMAL_SPECIES + ", " +
                T_ANIMAL_NAME + ", " +
                T_ANIMAL_AGE + ", " +
                T_ANIMAL_PRIMARY_BREED + ", " +
                T_ANIMAL_SECONDARY_BREED + ", " +
                T_ANIMAL_SEX + ", " +
                T_ANIMAL_SIZE + ", " +
                T_ANIMAL_STERILE + ", " +
                T_ANIMAL_INTAKE_DATE + ", " +
                T_ANIMAL_PRIMARY_COLOR + ", " +
                T_ANIMAL_SECONDARY_COLOR + ", " +
                T_ANIMAL_DESCRIPTION + ", " +
                T_ANIMAL_DECLAWED + ", " +
                T_ANIMAL_PHOTO1 + ", " +
                T_ANIMAL_PHOTO2 + ", " +
                T_ANIMAL_PHOTO3 + ", " +
                T_ANIMAL_AVAILABLE + " " +
                " FROM " + TABLE_ANIMAL + //" as a, " +
                //TABLE_FAVORITE_ANIMALS + " as f " +
                " WHERE " + T_ANIMAL_FAVORITE + " = 'Y';"; // + T_ANIMAL_ID + ";";


    }

    public void setCursorForFavoriteList(SQLiteDatabase db){
        Cursor c = getFavoriteList(db);
        cursors.put("Favorites", c);
    }

    public Cursor getCursorForFavoriteList() {
        return cursors.get("Favorites");
    }

    public Cursor getFavoriteList(SQLiteDatabase db){
        // pas testée
        String sql = getFavoriteListSQL();
        //Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean isFavorite(SQLiteDatabase db, String animalID){
        // pas testée
        //String sql = "SELECT " + T_FAVORITE_ANIMALS_ANIMAL_ID + " FROM " + TABLE_FAVORITE_ANIMALS + " WHERE " + T_FAVORITE_ANIMALS_ANIMAL_ID + " = " + animalID + ";";
        //Log.d("DB", sql);
        Cursor c = getFavoriteAnimal(db, animalID);
        if (!(c.moveToFirst()) || c.getCount() ==0){
            return false;
        }
        return true;
    }
/*
    public String getNewAnimalsListSQL() {
        return "SELECT a." +
                T_ANIMAL_ID + ", " +
                T_ANIMAL_SPECIES + ", " +
                T_ANIMAL_NAME + ", " +
                T_ANIMAL_AGE + ", " +
                T_ANIMAL_PRIMARY_BREED + ", " +
                T_ANIMAL_SECONDARY_BREED + ", " +
                T_ANIMAL_SEX + ", " +
                T_ANIMAL_SIZE + ", " +
                T_ANIMAL_STERILE + ", " +
                T_ANIMAL_INTAKE_DATE + ", " +
                T_ANIMAL_PRIMARY_COLOR + ", " +
                T_ANIMAL_SECONDARY_COLOR + ", " +
                T_ANIMAL_DESCRIPTION + ", " +
                T_ANIMAL_DECLAWED + ", " +
                T_ANIMAL_PHOTO1 + ", " +
                T_ANIMAL_PHOTO2 + ", " +
                T_ANIMAL_PHOTO3 + " " +
                " FROM " + TABLE_ANIMAL + " as a, " +
                TABLE_NEW_ANIMALS + " as n " +
                " WHERE n." + T_NEW_ANIMALS_ANIMAL_ID + " = a." + T_ANIMAL_ID + ";";
    }*/
    public void updateAnimalPictures(SQLiteDatabase db, String animalID, String photo1, String photo2, String photo3)
    {
        String tableName = TABLE_ANIMAL;
//        if (sender.equals(CURSOR_NAME_NEW_ANIMALS))
//        {
//
//        }
//        else if(sender.equals()) {
//            tableName = TABLE_FAVORITE_ANIMALS;
//        }
//        else if(sender.equals()) {
//        }
//        else if(sender.equals()) {
//        }

        ContentValues data=new ContentValues();
        data.put(T_ANIMAL_PHOTO1,photo1);
        data.put(T_ANIMAL_PHOTO2,photo2);
        data.put(T_ANIMAL_PHOTO3,photo3);
        db.update(tableName, data, T_ANIMAL_ID + "=" + animalID, null);
    }
/*
    public Cursor getNewAnimalsList(SQLiteDatabase db){
        // pas testée
        String sql = getNewAnimalsListSQL();
        //Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void removeAllFromNewList(SQLiteDatabase db){
        Log.d("DB", "NEW:Clear new list.");
        db.delete(DBHelper.TABLE_NEW_ANIMALS,
                null,
                null);
    }

    public void setCursorForNewAnimalsList(SQLiteDatabase db){
        Cursor c = getNewAnimalsList(db);
        cursors.put(CURSOR_NAME_NEW_ANIMALS, c);
    }

    public Cursor getCursorForNewAnimalsList() {
        return cursors.get(CURSOR_NAME_NEW_ANIMALS);
    }
*/
    public String getAnimalSQL(String id, String[] extraFields) {
        return  "SELECT " +
                T_ANIMAL_ID + ", " +
                T_ANIMAL_SPECIES + ", " +
                T_ANIMAL_NAME + ", " +
                T_ANIMAL_AGE + ", " +
                T_ANIMAL_PRIMARY_BREED + ", " +
                T_ANIMAL_SECONDARY_BREED + ", " +
                T_ANIMAL_SEX + ", " +
                T_ANIMAL_SIZE + ", " +
                T_ANIMAL_STERILE + ", " +
                T_ANIMAL_INTAKE_DATE + ", " +
                T_ANIMAL_PRIMARY_COLOR + ", " +
                T_ANIMAL_SECONDARY_COLOR + ", " +
                T_ANIMAL_DESCRIPTION + ", " +
                T_ANIMAL_DECLAWED + ", " +
                T_ANIMAL_PHOTO1 + ", " +
                T_ANIMAL_PHOTO2 + ", " +
                T_ANIMAL_PHOTO3 + " " +
                " FROM " + TABLE_ANIMAL +
                " WHERE " + T_ANIMAL_ID + "='" + id + "';";
    }

    public Cursor getAnimal(SQLiteDatabase db, String animalID) {
        String sql = getAnimalSQL(animalID, null);
        //Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public String getFavoriteAnimalSQL(String favID) {
        return "SELECT " +
                T_ANIMAL_ID + ", " +
                T_ANIMAL_SPECIES + ", " +
                T_ANIMAL_NAME + ", " +
                T_ANIMAL_AGE + ", " +
                T_ANIMAL_PRIMARY_BREED + ", " +
                T_ANIMAL_SECONDARY_BREED + ", " +
                T_ANIMAL_SEX + ", " +
                T_ANIMAL_SIZE + ", " +
                T_ANIMAL_STERILE + ", " +
                T_ANIMAL_INTAKE_DATE + ", " +
                T_ANIMAL_PRIMARY_COLOR + ", " +
                T_ANIMAL_SECONDARY_COLOR + ", " +
                T_ANIMAL_DESCRIPTION + ", " +
                T_ANIMAL_DECLAWED + ", " +
                T_ANIMAL_PHOTO1 + ", " +
                T_ANIMAL_PHOTO2 + ", " +
                T_ANIMAL_PHOTO3 + ", " +
                T_ANIMAL_AVAILABLE + " " +
                " FROM " + TABLE_ANIMAL + //" as a, " +
                // TABLE_FAVORITE_ANIMALS + " as f " +
                " WHERE " + T_ANIMAL_ID + "='" + favID + "'" + //+ "' " +
                " AND " + T_ANIMAL_FAVORITE + " = 'Y';"; // + T_ANIMAL_ID + ";";
    }

    public Cursor getFavoriteAnimal(SQLiteDatabase db, String favID) {
        String sql = getFavoriteAnimalSQL(favID);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
/*
    public boolean isNew(SQLiteDatabase db, String animalID){
        // pas testée
        String sql = "SELECT " + T_NEW_ANIMALS_ANIMAL_ID + " FROM " + TABLE_NEW_ANIMALS + " WHERE " + T_NEW_ANIMALS_ANIMAL_ID + " = " + animalID + ";";
        //Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);
        if (!(c.moveToFirst()) || c.getCount() ==0){
            return false;
        }
        return true;
    }
    public void addToNewList(SQLiteDatabase db, String animalID) throws SQLException {
        // pas testée
        Log.d("DB", "NEW:Adding " + animalID);
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_NEW_ANIMALS_ANIMAL_ID, animalID);
        try {
            db.insertOrThrow(DBHelper.TABLE_NEW_ANIMALS, null, cv);
        } catch (SQLException e) {
            Log.d("DB", e.getMessage());
            throw e;
        }
    }

    public void removeFromNewList(SQLiteDatabase db, String animalID){
        Log.d("DB", "NEW:Removing " + animalID);
        db.delete(DBHelper.TABLE_NEW_ANIMALS,
                DBHelper.T_NEW_ANIMALS_ANIMAL_ID + "=" + animalID,
                null);
    }

*/
    public boolean appFirstTime(SQLiteDatabase db) {
        String sql = "SELECT " + T_PREFERENCES_APP_STATE + " FROM " + TABLE_PREFERENCES + ";";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        String app_state = c.getString(C_PREFERENCES_APP_STATE);
        if (app_state.equals("New"))
            return true;
        return false;
    }

    public void setSessionModeOffLine(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_SESSION_MODE, T_PREFERENCES_SESSION_MODE_OFFLINE);
        db.update(DBHelper.TABLE_PREFERENCES, cv, null, null);
    }

    public void setSessionModeOnLineIfStart(SQLiteDatabase db) {
        if (firstTimeIn) {
            setSessionModeOnLine(db);
            firstTimeIn = false;
        }
    }
    public void setSessionModeOnLine(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_SESSION_MODE, T_PREFERENCES_SESSION_MODE_ONLINE);
        db.update(DBHelper.TABLE_PREFERENCES, cv, null, null);
    }

    public Cursor getPreferences(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + TABLE_PREFERENCES + ";", null);
    }

    public void setNotifications(SQLiteDatabase db, String noticeEnabled, int interval) {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_NOTICE_ENABLED, noticeEnabled);
        cv.put(DBHelper.T_PREFERENCES_NOTICE_FREQUENCY, interval);
        db.update(DBHelper.TABLE_PREFERENCES, cv, null, null);
    }

    public String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        Date now = Calendar.getInstance().getTime();
        String dateStr = df.format(now);
        Log.d("DBHelper", "date is:" + dateStr);
        return df.format(now);
    }
    public void setNotificationLastCall(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_LAST_NOTIFICATION_DATE, getDate());
        db.update(DBHelper.TABLE_PREFERENCES, cv, null, null);
    }

    public String getNotificationLastCall(SQLiteDatabase db) {
        Cursor c = getPreferences(db);
        if (c == null)
            return null;
        if (c.isAfterLast()) {
            return null;
        }
        c.moveToFirst();
        return c.getString(C_PREFERENCES_LAST_NOTIFICATION_DATE);
    }

    public void appFirstTimeFalse(SQLiteDatabase db)  throws SQLException {
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_APP_STATE, "InitialLoadDone");
        db.update(DBHelper.TABLE_PREFERENCES, cv, null, null);
    }

    public static Cursor getSearchCriteria(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + TABLE_ADOPTABLE_SEARCH + ";", null);
    }

    public void deleteAnimalList(SQLiteDatabase db, String where) throws SQLException {
        try {
            db.delete(TABLE_ANIMAL, where, null);
        } catch (SQLException e) {
            Log.d("DB","deleteAnimalList failed.  Reason:" + e.getMessage());
            throw e;
        }
    }


/*
    public void deleteAll(SQLiteDatabase db) {
        String sql = "DELETE FROM " + TABLE_ANIMAL + ";";
        Log.d("DB", sql);
        db.delete(DBHelper.TABLE_ANIMAL, null, null);
        //db.execSQL(sql);
        sql = "DELETE FROM " + TABLE_FAVORITE_ANIMALS + ";";
        Log.d("DB", sql);
        db.delete(DBHelper.TABLE_FAVORITE_ANIMALS, null, null);
        //db.execSQL(sql);
        //db.rawQuery(sql, null);
        //sql = "DELETE FROM " + TABLE_ANIMAL_PHOTOS + ";";
        //Log.d("DB", sql);
        //db.execSQL(sql);
        //db.rawQuery(sql, null);
    }*/
}
