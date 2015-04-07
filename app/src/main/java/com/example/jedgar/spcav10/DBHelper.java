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
    static final int DB_VERSION = 17;

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


    // table photos
    static final String TABLE_ANIMAL_PHOTOS = "animalPhotos";
    static final String T_ANIMAL_PHOTOS_ANIMAL_ID = "_id";
    static final String T_ANIMAL_PHOTOS_PHOTO_ID = "photo_id";
    static final String T_ANIMAL_PHOTOS_PATH = "photoPath";

    // table search params
    static final String TABLE_ADOPTABLE_SEARCH = "adoptableSearchParams";
    static final String T_ADOPTABLE_SEARCH_ID = "_id";
    static final String T_ADOPTABLE_SEARCH_SPECIES = "species";
    static final String T_ADOPTABLE_SEARCH_NAME = "name";
    static final String T_ADOPTABLE_SEARCH_AGE = "age";
    static final String T_ADOPTABLE_SEARCH_BREED = "breed";
    static final String T_ADOPTABLE_SEARCH_SEX = "sex";
    static final String T_ADOPTABLE_SEARCH_SIZE = "size";
    static final String T_ADOPTABLE_SEARCH_COLOR = "color";
    static final String T_ADOPTABLE_SEARCH_STERILE = "sterile";
    static final String T_ADOPTABLE_SEARCH_INTAKE_DATE = "intake_date";
    static final String T_ADOPTABLE_SEARCH_DECLAWED  = "declawed";

    //table preferences
    static final String TABLE_PREFERENCES = "preferences";
    static final String T_PREFERENCES_ID = "_id";
    static final String T_PREFERENCES_STATE = "state";
    static final String T_PREFERENCES_NOTICE_ENABLED = "notice_enabled";
    static final String T_PREFERENCES_NOTICE_FREQUENCY = "notice_frequency";
    static final String T_PREFERENCES_DOWNLOAD_IMAGE = "download_image";

    //table favorites
    static final String TABLE_FAVORITE_ANIMALS = "favorites";
    static final String T_FAVORITE_ANIMALS_ANIMAL_ID = "_id";

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
                T_ADOPTABLE_SEARCH_AGE         + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_SEX         + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_STERILE     + " INT    NOT NULL," +
                T_ADOPTABLE_SEARCH_DECLAWED    + " INT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_ADOPTABLE_SEARCH + " table created");

        sql = "CREATE TABLE " + TABLE_PREFERENCES + "(" +
                T_PREFERENCES_STATE            + " int  CHECK(state IN ('New','Default','Set'))," +
                T_PREFERENCES_NOTICE_ENABLED   + " char CHECK(notice_enabled IN('Y','N'))," +
                T_PREFERENCES_NOTICE_FREQUENCY + " int  CHECK(notice_frequency IN (1,60,1440,10080))," +
                T_PREFERENCES_DOWNLOAD_IMAGE   + " text check (download_image IN ('All','First Only', 'None'))" +
                ");";
        db.execSQL(sql);
        Log.d("DB", TABLE_PREFERENCES + " table created");

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
                " order by " + T_ANIMAL_NAME + " asc;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void addAnimal(SQLiteDatabase db, com.example.jedgar.spcav10.Animal animal) {
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
        db.insertOrThrow(DBHelper.TABLE_FAVORITE_ANIMALS, null, cv);
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

/*
public class SearchCriteria {
    public static final int SPECIES_DOG = 1;         // 0001
    public static final int SPECIES_CAT = 2;         // 0010
    public static final int SPECIES_RABBIT = 4;      // 0100
    public static final int SPECIES_SMALLFURRY = 8;  // 1000
    public static final int SPECIES_ALL  = 15;       // 1111

    public static final int SEX_MALE  = 1;       // 1111
    public static final int SEX_FEMALE= 2;       // 1111

    public static final int BOOL_YES  = 1;       // 1111
    public static final int BOOL_NO   = 2;       // 1111

    int species;        //('Dog = 1', 'Cat = 2', 'Rabbit = 4', 'Small&Furry = 8')
    int ageGroup;       //('Adult = 1', 'Young = 2', 'Baby = 3')
    int sex;            // ('Male = 1','Female = 2')
    int sterile;        // ('Y = 1','N = 2')
    int declawed;       //('Y = 1','N = 2')

    SearchCriteria() {
        species = 0;
        ageGroup = 0;
        sex = 0;
        sterile = 0;
        declawed = 0;
    }

    void searchDeclawed() {
        declawed = (declawed & BOOL_YES) ^ BOOL_YES;
    }
    void searchNonDeclawed() {
        declawed = (declawed & BOOL_NO) ^ BOOL_NO;
    }
    void searchSteriles() {
        sterile = (sterile & BOOL_YES) ^ BOOL_YES;
    }
    void searchNonSteriles() {
        sterile = (sterile & BOOL_NO) ^ BOOL_NO;
    }
    void searchMales() {
        sex = (sex & SEX_MALE) ^ SEX_MALE;
    }
    void searchFemales() {
        sex = (sex & SEX_FEMALE) ^ SEX_FEMALE;
    }
    void searchDogs() {
        species = (species & SPECIES_DOG) ^ SPECIES_DOG;
    }
    void searchCats() {
        species = (species & SPECIES_CAT) ^ SPECIES_CAT;
    }
    void searchRabbits() {
        species = (species & SPECIES_RABBIT) ^ SPECIES_RABBIT;
    }
    void searchSmallFurry() {
        species = (species & SPECIES_SMALLFURRY) ^ SPECIES_SMALLFURRY;
    }

    void setSearchCriteria() {
        if (species == 0xFF)
            species = 0;
        if (sex == 0x03)
            sex = 0;
        if (sterile == 0x03)
            sterile = 0;
        if (declawed == 0x03)
            declawed = 0;
    }
};
*/