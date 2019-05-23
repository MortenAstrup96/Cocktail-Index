package com.example.application.cocktailindex.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "Ingredient",
        foreignKeys = {
        @ForeignKey(
                entity = Cocktail.class,
                parentColumns = "id",
                childColumns = "cocktail_fk"
        )})
public class Ingredient implements Serializable, Comparable {

    @PrimaryKey
    public int id = UUID.randomUUID().hashCode();

    @ColumnInfo(name = "number")
    private int number;

    @ColumnInfo(name = "cocktail_fk")
    private int cocktailIdFk;

    @ColumnInfo(name = "ingredient")
    private String ingredient;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "measurement")
    private String measurement;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getCocktailIdFk() {
        return cocktailIdFk;
    }

    public void setCocktailIdFk(int cocktailIdFk) {
        this.cocktailIdFk = cocktailIdFk;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Ingredient(String ingredient, String amount, String measurement) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.measurement = measurement;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        return ((Ingredient)o).getNumber()-getNumber();
    }
}
