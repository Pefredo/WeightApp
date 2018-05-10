package com.example.gosia.weightapplication;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Gosia on 05.03.2018.
 */
@Database(name = WeightDatabase.NAME, version = WeightDatabase.VERSION)
public class WeightDatabase {

    public static final String NAME = "WeightDataBase";

    //update when change table
    public static final int VERSION = 1;
}
