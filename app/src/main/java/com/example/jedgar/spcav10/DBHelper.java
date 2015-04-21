package com.example.jedgar.spcav10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rania on 26/02/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "SPCA.DB";
    static final int DB_VERSION = 23;

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
    public static final int C_ANIMAL_DECLAWED = 12;
    public static final int C_ANIMAL_DESCRIPTION = 13;
    public static final int C_ANIMAL_PHOTO1 = 14;
    public static final int C_ANIMAL_PHOTO2 = 15;
    public static final int C_ANIMAL_PHOTO3 = 16;


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
    static final String T_PREFERENCES_ID = "_id";
    static final String T_PREFERENCES_APP_STATE = "app_state";
    static final String T_PREFERENCES_NOTICE_ENABLED = "notice_enabled";
    static final String T_PREFERENCES_NOTICE_FREQUENCY = "notice_frequency";
    static final String T_PREFERENCES_DOWNLOAD_IMAGE = "download_image";

    //table favorites
    static final String TABLE_FAVORITE_ANIMALS = "favorites";
    static final String T_FAVORITE_ANIMALS_ANIMAL_ID = "_id";

    public static final int C_PREFERENCES_APP_STATE = 0;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "In onCreate");
        String sql = "CREATE TABLE " + TABLE_ANIMAL + "(" +
                T_ANIMAL_ID              + " INT PRIMARY KEY     NOT NULL," +
                T_ANIMAL_SPECIES         + " TEXT    NOT NULL," +
                T_ANIMAL_NAME            + " TEXT    NOT NULL," +
                T_ANIMAL_AGE             + " INT     NOT NULL," +
                T_ANIMAL_PRIMARY_BREED   + " TEXT," +
                T_ANIMAL_SECONDARY_BREED + " TEXT," +
                T_ANIMAL_SEX             + " char    CHECK( " + T_ANIMAL_SEX + " IN ('Male','Female'))," +
                T_ANIMAL_SIZE            + " char," +
                T_ANIMAL_STERILE         + " char CHECK( " + T_ANIMAL_STERILE + " IN ('Y','N', 'U'))," +
                T_ANIMAL_INTAKE_DATE     + " date," +
                T_ANIMAL_PRIMARY_COLOR   + " TEXT," +
                T_ANIMAL_SECONDARY_COLOR + " TEXT," +
                T_ANIMAL_DESCRIPTION     + " TEXT," +
                T_ANIMAL_DECLAWED        + " char CHECK( " + T_ANIMAL_DECLAWED + " IN ('Yes','No','Both','Front'))," +
                // pour une db digne de ce nom, on crée une autre table pour les champs qui suivent.
                T_ANIMAL_PHOTO1          + " TEXT," +
                T_ANIMAL_PHOTO2          + " TEXT," +
                T_ANIMAL_PHOTO3          + " TEXT" +
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
                T_ADOPTABLE_SEARCH_SPECIES     + " INT    NOT NULL," +
                //T_ADOPTABLE_SEARCH_AGE         + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_AGE_MIN     + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_AGE_MAX     + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_SEX         + " INT    NOT NULL" +
                //T_ADOPTABLE_SEARCH_STERILE     + " INT    NOT NULL," +
                //T_ADOPTABLE_SEARCH_DECLAWED    + " INT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_ADOPTABLE_SEARCH + " table created");

        sql = "CREATE TABLE " + TABLE_PREFERENCES + "(" +
                T_PREFERENCES_APP_STATE        + " int  CHECK(" + T_PREFERENCES_APP_STATE + " IN ('New','InitialLoadDone'))," +
                T_PREFERENCES_NOTICE_ENABLED   + " char CHECK(" + T_PREFERENCES_NOTICE_ENABLED + " IN('Y','N'))," +
                T_PREFERENCES_NOTICE_FREQUENCY + " int  CHECK(" + T_PREFERENCES_NOTICE_FREQUENCY + " IN (1,60,1440,10080))" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_PREFERENCES + " table created");
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_PREFERENCES_APP_STATE, "New");
        cv.put(DBHelper.T_PREFERENCES_NOTICE_ENABLED, "Y");
        cv.put(DBHelper.T_PREFERENCES_NOTICE_FREQUENCY, 1440);
        try {
            db.insertOrThrow(DBHelper.TABLE_PREFERENCES, null, cv);
        } catch (SQLException e) {
            Log.d("DB","Default prefs init failed.  Reason:" + e.getMessage());
        }

        sql = "CREATE TABLE " + TABLE_FAVORITE_ANIMALS + "(" +
                T_FAVORITE_ANIMALS_ANIMAL_ID + " INT PRIMARY KEY     NOT NULL REFERENCES " + TABLE_ANIMAL + "(" + T_ANIMAL_ID + ")" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_FAVORITE_ANIMALS + " table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "In onUpgrade");
        db.execSQL("drop index if exists " + TABLE_ANIMAL_SPECIES_IDX + ";");
        db.execSQL("drop table if exists " + TABLE_ANIMAL + ";");
        db.execSQL("drop table if exists " + TABLE_ANIMAL_PHOTOS + ";");
        db.execSQL("drop table if exists " + TABLE_ADOPTABLE_SEARCH + ";");
        db.execSQL("drop table if exists " + TABLE_PREFERENCES + ";");
        db.execSQL("drop table if exists " + TABLE_FAVORITE_ANIMALS + ";");
        onCreate(db);
    }

    public Cursor getAnimalList(SQLiteDatabase db){
        String sql = "select * from " + TABLE_ANIMAL +
                " order by " + T_ANIMAL_ID + " desc;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getCursorForSelect(SQLiteDatabase db, String sql) {
        return db.rawQuery(sql, null);
    }

    public Cursor getAnimalListOrdered(SQLiteDatabase db, String orderClause){
        String sql = "select * from " + TABLE_ANIMAL +
                " order by " + orderClause + ";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void addAnimal(SQLiteDatabase db, Animal animal) {
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
        db.beginTransaction();
        try {
            db.insertOrThrow(DBHelper.TABLE_ANIMAL, null, cv);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            animal.dump();
            Log.d("DownloadWebAnimalTask","Transaction aborted." + e.getMessage());
        } finally {
            Log.d("DownloadWebAnimalTask","Transaction finished.");
            db.endTransaction();
        }
    }

    public void addToFavoriteList(SQLiteDatabase db, String animalID) {
        Log.d("DB", "FAV:Adding " + animalID);
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID, animalID);
        try {
            db.insertOrThrow(DBHelper.TABLE_FAVORITE_ANIMALS, null, cv);
        } catch (SQLException sq) {
            Log.d("DB","favorite already added");
        }
    }

    public void removeFromFavoriteList(SQLiteDatabase db, String animalID){
        Log.d("DB", "FAV:Removing " + animalID);
        db.delete(DBHelper.TABLE_FAVORITE_ANIMALS,
                DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID + "=" + animalID,
                null);
    }

    public void removeAllFromFavoriteList(SQLiteDatabase db){
        Log.d("DB", "FAV:Clear favorite list.");
        db.delete(DBHelper.TABLE_FAVORITE_ANIMALS,
                null,
                null);
    }

    public Cursor getFavoriteList(SQLiteDatabase db){
        // pas testée
        String sql = "SELECT a." +
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
                T_ANIMAL_DECLAWED + ", " +
                T_ANIMAL_DESCRIPTION + " " +
                " FROM " + TABLE_ANIMAL + " as a, " +
                TABLE_FAVORITE_ANIMALS + " as f " +
                " WHERE a." + T_ANIMAL_ID + " = f." + T_FAVORITE_ANIMALS_ANIMAL_ID + ";";
        Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean isFavorite(SQLiteDatabase db, String animalID){
        // pas testée
        String sql = "SELECT " + T_FAVORITE_ANIMALS_ANIMAL_ID + " FROM " + TABLE_FAVORITE_ANIMALS + " WHERE " + T_FAVORITE_ANIMALS_ANIMAL_ID + " = " + animalID + ";";
        Log.d("DB", sql);
        Cursor c = db.rawQuery(sql, null);

        if (!(c.moveToFirst()) || c.getCount() ==0){
            return false;
        }
        return true;
    }

    public boolean appFirstTime(SQLiteDatabase db) {
        String sql = "SELECT " + T_PREFERENCES_APP_STATE + " FROM " + TABLE_PREFERENCES + ";";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        String app_state = c.getString(C_PREFERENCES_APP_STATE);
        if (app_state.equals("New"))
            return true;
        return false;
    }

    public void appFirstTimeFalse(SQLiteDatabase db) {
        String sql = "UPDATE "  + TABLE_PREFERENCES +  " SET " + T_PREFERENCES_APP_STATE + "=" + "'InitialLoadDone';";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.d("DB","UPDATE appFirstTimeFalse failed.  Reason:" + e.getMessage());
        }
    }

    public static Cursor getSearchCriteria(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + TABLE_ADOPTABLE_SEARCH + ";", null);
    }

    public void deleteAnimalList(SQLiteDatabase db, String where) {
        try {
            db.delete(TABLE_ANIMAL, where, null);
        } catch (SQLException e) {
            Log.d("DB","deleteAnimalList failed.  Reason:" + e.getMessage());
        }
    }

    public void deleteAll(SQLiteDatabase db) {
        String sql = "DELETE FROM " + TABLE_ANIMAL + ";";
        Log.d("DB", sql);
        db.execSQL(sql);
        sql = "DELETE FROM " + TABLE_FAVORITE_ANIMALS + ";";
        Log.d("DB", sql);
        db.execSQL(sql);
        //db.rawQuery(sql, null);
        //sql = "DELETE FROM " + TABLE_ANIMAL_PHOTOS + ";";
        //Log.d("DB", sql);
        //db.execSQL(sql);
        //db.rawQuery(sql, null);
    }
}