package com.example.jedgar.spcav10;

/**
 * Created by pascal on 15/04/15.
 */
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
