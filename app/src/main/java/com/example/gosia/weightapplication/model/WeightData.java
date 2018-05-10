package com.example.gosia.weightapplication.model;

import com.example.gosia.weightapplication.WeightDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Gosia on 05.03.2018.
 */
@Table(database = WeightDatabase.class)
public class WeightData extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String weight;
    @Column
    String lastDayWeightMeasurement;
    @Column
    int dayNumber;

    public WeightData() {

    }

    public WeightData(String date, String weight, int dayNumber) {
        this.lastDayWeightMeasurement = date;
        this.weight = weight;
        this.dayNumber = dayNumber;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLastDayWeightMeasurement() {
        return lastDayWeightMeasurement;
    }

    public void setLastDayWeightMeasurement(String lastDayWeightMeasurement) {
        this.lastDayWeightMeasurement = lastDayWeightMeasurement;
    }

    public int getId() {
        return id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    @Override
    public String toString() {
        return "id: " + this.id + "\n" +
                " weight: " + this.weight + "\n" +
                " lastDayWeightMeasurement: " + this.lastDayWeightMeasurement + "\n" +
                " dayNumber" + this.dayNumber + "\n";

    }
}
