package com.example.jedgar.spca;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by pascal on 15/04/15.
 */
public class SearchCriteria implements Parcelable {
    public static final int SPECIES_DOG = 1;         // 00001
    public static final int SPECIES_CAT = 2;         // 00010
    public static final int SPECIES_RABBIT = 4;      // 00100
    public static final int SPECIES_SMALLFURRY = 8;  // 01000
    public static final int SPECIES_BIRD = 16;  // 10000
    public static final int SPECIES_ALL  = 31;       // 11111

    public static final int SEX_MALE  = 1;       // 1111
    public static final int SEX_FEMALE= 2;       // 1111
    public static final int SEX_ALL   = 3;

    public static final int BOOL_YES  = 1;       // 1111
    public static final int BOOL_NO   = 2;       // 1111

    int species;        //('Dog = 1', 'Cat = 2', 'Rabbit = 4', 'Small&Furry = 8')
    int ageGroup;       //('Adult = 1', 'Young = 2', 'Baby = 3')
    int sex;            // ('Male = 1','Female = 2')
    int sterile;        // ('Y = 1','N = 2')
    int declawed;       //('Y = 1','N = 2')
    int ageMin;
    int ageMax;
    String lastCallDate;

    public void dump() {
        Log.d("SC", "species:" + species + " ageMin:" + ageMin + " ageMax:" + ageMax + " sex:" + sex);
    }

    public SearchCriteria() {
        zeroAll();
    }

    void zeroAll() {
        species = 0;
        ageGroup = 0;
        sex = 0;
        sterile = 0;
        declawed = 0;
        ageMin = 0;
        ageMax = 0;
    }

    public SearchCriteria(SQLiteDatabase db) {
        zeroAll();
        Cursor c = DBHelper.getSearchCriteria(db);
        if (c.moveToFirst()) {
            species = c.getInt(DBHelper.C_ADOPTABLE_SEARCH_SPECIES);
            ageMin = c.getInt(DBHelper.C_ADOPTABLE_SEARCH_AGE_MIN);
            ageMax = c.getInt(DBHelper.C_ADOPTABLE_SEARCH_AGE_MAX);
            sex = c.getInt(DBHelper.C_ADOPTABLE_SEARCH_SEX);
        }
        Log.d("SC", "SearchCriteria(SQLiteDatabase db)");
        dump();
    }

    void searchDeclawed() {
        declawed ^= BOOL_YES;
    }
    void searchNonDeclawed() {declawed ^= BOOL_NO;}
    void searchSteriles() {
        sterile ^= BOOL_YES;
    }
    void searchNonSteriles() {sterile ^= BOOL_NO;}
    void searchMales() {
        sex ^= SEX_MALE;
    }
    void searchFemales() {
        sex ^= SEX_FEMALE;
    }
    void searchDogs() {
        species ^= SPECIES_DOG;
    }
    void searchCats() {
        species ^= SPECIES_CAT;
    }
    void searchRabbits() {
        species ^= SPECIES_RABBIT;
    }
    void searchSmallFurry() {
        species ^= SPECIES_SMALLFURRY;
    }
    void searchBirds() {
        species ^= SPECIES_BIRD;
    }
    void setAgeMin(int min) {ageMin = min;}
    void setAgeMax(int max) {ageMax = max;}

    void setSearchCriteria() {
        if (species == SPECIES_ALL)
            species = 0;
        if (sex == 0x03)
            sex = 0;
        if (sterile == 0x03)
            sterile = 0;
        if (declawed == 0x03)
            declawed = 0;
    }

    void saveSearchCriteria(SQLiteDatabase db) {
        String sql = "SELECT * FROM " + DBHelper.TABLE_ADOPTABLE_SEARCH + ";";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            Log.d("SearchCriteria", "Updating: " +
                    " species:" + c.getInt(DBHelper.C_ADOPTABLE_SEARCH_SPECIES) +
                    " ageMin:" + c.getInt(DBHelper.C_ADOPTABLE_SEARCH_AGE_MIN) +
                    " ageMax:" + c.getInt(DBHelper.C_ADOPTABLE_SEARCH_AGE_MAX) +
                    " sex:" + c.getInt(DBHelper.C_ADOPTABLE_SEARCH_SEX));

            Log.d("SearchCriteria", "Updating to: " + " species:" + species + " ageMin:" + ageMin + " ageMax:" + ageMax + " sex:" + sex);
            sql = "UPDATE " + DBHelper.TABLE_ADOPTABLE_SEARCH + " SET " +
                    DBHelper.T_ADOPTABLE_SEARCH_SPECIES + "=" + species + ", " +
                    DBHelper.T_ADOPTABLE_SEARCH_AGE_MIN + "=" + ageMin + ", " +
                    DBHelper.T_ADOPTABLE_SEARCH_AGE_MAX + "=" + ageMax + ", " +
                    DBHelper.T_ADOPTABLE_SEARCH_SEX + "=" + sex +
                    //DBHelper.T_ADOPTABLE_SEARCH_STERILE + "=" + sterile + ", " +
                    //DBHelper.T_ADOPTABLE_SEARCH_DECLAWED + "=" + declawed + ", " +
                    ";";
            db.execSQL(sql);
            Log.d("SearchCriteria", "Updating: " + sql);
        }
        else {
            Log.d("SearchCriteria", "INSERT");
            ContentValues cv = new ContentValues();
            cv.clear();
            cv.put(DBHelper.T_ADOPTABLE_SEARCH_SPECIES, species);
            cv.put(DBHelper.T_ADOPTABLE_SEARCH_AGE_MIN, ageMin);
            cv.put(DBHelper.T_ADOPTABLE_SEARCH_AGE_MAX, ageMax);
            cv.put(DBHelper.T_ADOPTABLE_SEARCH_SEX, sex);
            Log.d("SearchCriteria", "Inserting: " + " species:" + species + " ageMin:" + ageMin + " ageMax:" + ageMax + " sex:" + sex);
            try {
                db.insertOrThrow(DBHelper.TABLE_ADOPTABLE_SEARCH, null, cv);
            } catch (SQLException e) {
                Log.d("SearchCriteria", "Insert failed.  Reason:" + e.getMessage());
            }
        }
    }

    public String getWhereFromPrefs() {
        int reqAgeMax;

        if (ageMax == 0 || ageMax == 24)
            reqAgeMax = 1000;
        else
            reqAgeMax = ageMax;

        String where = " WHERE " + DBHelper.T_ANIMAL_AGE + ">=" + ageMin +
                " AND " + DBHelper.T_ANIMAL_AGE + "<=" + reqAgeMax;

        boolean and_or = false;

        if (species != 0 && species != SPECIES_ALL) {
            where += " AND (";
            if ((species & SPECIES_DOG) == SPECIES_DOG) {
                where += " " + DBHelper.T_ANIMAL_SPECIES + "='Dog'";
                and_or = true;
            }
            if ((species & SPECIES_CAT) == SPECIES_CAT) {
                if (and_or)
                    where += " OR";
                where += " " + DBHelper.T_ANIMAL_SPECIES + "='Cat'";
                and_or = true;
            }
            if ((species & SPECIES_RABBIT) == SPECIES_RABBIT) {
                if (and_or)
                    where += " OR";
                where += " " + DBHelper.T_ANIMAL_SPECIES + "='Rabbit'";
                and_or = true;
            }
            if((species & SPECIES_BIRD) == SPECIES_BIRD){
                if (and_or)
                    where += " OR";
                where += " " + DBHelper.T_ANIMAL_SPECIES + "='Bird'";
                and_or = true;
            }
            if ((species & SPECIES_SMALLFURRY) == SPECIES_SMALLFURRY) {
                if (and_or)
                    where += " OR";
                where += " (" +
                        DBHelper.T_ANIMAL_SPECIES + "!='Dog' AND " +
                        DBHelper.T_ANIMAL_SPECIES + "!='Cat' AND " +
                        DBHelper.T_ANIMAL_SPECIES + "!='Bird' AND " +
                        DBHelper.T_ANIMAL_SPECIES + "!='Rabbit')";
                and_or = true;
            }
            where += ")";
        }
        if (sex != 0 && sex != SEX_ALL) {
            where += " AND ";
            if ((sex & SEX_FEMALE) == SEX_FEMALE)
                where += " " + DBHelper.T_ADOPTABLE_SEARCH_SEX + "='Female'";
            if ((sex & SEX_MALE) == SEX_MALE)
                where += " " + DBHelper.T_ADOPTABLE_SEARCH_SEX + "='Male'";
        }

        where += " AND " + DBHelper.T_ANIMAL_AVAILABLE + " ='Y'";
        return where;
    }

    public String getSelectCommand() {

        //dump();
        String where = getWhereFromPrefs();

        Log.d("SC", "SELECT * FROM " + DBHelper.TABLE_ANIMAL + where + ";");
        return "SELECT * FROM " + DBHelper.TABLE_ANIMAL + where + ";";
    }
/*
    public String getCommandForNotifs() {
        String where = getWhereFromPrefs();
        where += " AND n." + DBHelper.T_NEW_ANIMALS_ANIMAL_ID + " = a." + DBHelper.T_ANIMAL_ID +
                 " AND n." + DBHelper.T_NEW_ANIMALS_DATE + " > (SELECT " + DBHelper.T_PREFERENCES_LAST_NOTIFICATION_DATE + " FROM " + DBHelper.TABLE_PREFERENCES + ");";
        return "SELECT * FROM " + DBHelper.TABLE_ANIMAL + " as a, " +
                                  DBHelper.TABLE_NEW_ANIMALS + " as n " + where;
    }

    public String getCommandForNotifs(String from, String to) {
        String where = getWhereFromPrefs();
        where += " AND n." + DBHelper.T_NEW_ANIMALS_ANIMAL_ID + " = a." + DBHelper.T_ANIMAL_ID +
                " AND n." + DBHelper.T_NEW_ANIMALS_DATE + " > '" + from + "' AND n." + DBHelper.T_NEW_ANIMALS_DATE + " <= '" + to + "';";
        return "SELECT * FROM " + DBHelper.TABLE_ANIMAL + " as a, " +
                DBHelper.TABLE_NEW_ANIMALS + " as n " + where;
    }

    public String getCommandForNew() {
        String where = getWhereFromPrefs();
        where += " AND n." + DBHelper.T_NEW_ANIMALS_ANIMAL_ID + " = a." + DBHelper.T_ANIMAL_ID + ";";
        return "SELECT * FROM " + DBHelper.TABLE_ANIMAL + " as a, " +
                DBHelper.TABLE_NEW_ANIMALS + " as n " + where;
    }
*/
    public SearchCriteria (Parcel in){
        int[] data = new int[4];

        in.readIntArray(data);
        this.sex = data[0];
        this.species = data[1];
        this.ageMax = data[2];
        this.ageMin = data[3];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SearchCriteria createFromParcel(Parcel in) {
            return new SearchCriteria(in);
        }

        public SearchCriteria[] newArray(int size) {
            return new SearchCriteria[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sex);
        dest.writeInt(species);
        dest.writeInt(ageMax);
        dest.writeInt(ageMin);
    }
};
