package com.example.jedgar.spcav10;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pascal on 01/04/15.
 * cat, birds, dog, even rats are animals not all animal have claws and have breeding species kept track of.
 */

public class Animal {
    public String id;
    public String name;
    public String species;
    public String sex;
    public String primaryBreed;
    public String secondaryBreed;
    public String age;
    public String size;
    public String primaryColor;
    public String secondaryColor;
    public String sterile;
    public String declawed;
    public String intake_date;
    public String description;
    public String photo1;
    public String photo2;
    public String photo3;

    public Animal(){
    }

    public void dump() {
        Log.d("Animal INFO:", "ID:" + this.id + " Name:" + this.name + " species:" + this.species + " sex:" + this.sex +
                " primaryBreed:" + this.primaryBreed + " age:" + this.age + " size:" + this.size + " primaryColor:" + this.primaryColor +
                " sterile:" + this.sterile + " declawed:" + this.declawed + " intake_date:" + this.intake_date);
        Log.d("           :", "ID:" + this.id + " photo1:" + this.photo1);
        Log.d("           :", "ID:" + this.id + " photo2:" + this.photo2);
        Log.d("           :", "ID:" + this.id + " photo3:" + this.photo3);
    }
};
